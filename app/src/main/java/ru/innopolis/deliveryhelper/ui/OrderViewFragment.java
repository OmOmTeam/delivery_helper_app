package ru.innopolis.deliveryhelper.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.BarcodeReaderActivity;

import java.util.ArrayList;

import ru.innopolis.deliveryhelper.OrderViewMVC;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.controller.OrderViewController;

import static ru.innopolis.deliveryhelper.model.PlainConsts.barcode_checker_regexp;

public class OrderViewFragment extends Fragment implements OrderViewMVC.View, RoutingListener, OnMapReadyCallback {

    private static final String TAG = "OrderViewFragment";
    private OrderViewMVC.Controller controller;

    // UI elements
    TableLayout orderDetails;
    Button cancelButton;
    ProgressBar progressBar;
    View assignedPanel;
    Button actionButton;

    // list element color tint switching mechanism
    private boolean variator;

    // stored data for associated order
    private String customerName;
    private String customerPhone;
    private String orderId;
    private Button assignedPanelButton;
    private Button assignedPanelInfo;

    // maps objects
    private View mapsPlaceholder;
    GoogleMap map;
    ArrayList<Marker> markers;
    LatLng start, end;

    // barcode reader wrapper info
    static final int BARCODE_READER_ACTIVITY_REQUEST = 1;  // The request code
    private String codeChecker = barcode_checker_regexp;


    /**
     * View constructor
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // setting title on top of page
        try {
            getActivity().setTitle("Order View");

        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }

        View view = inflater.inflate(R.layout.fragment_orderview, container, false);

        // create controller
        controller = new OrderViewController(this);


        // find elements in ui
        assignedPanel = view.findViewById(R.id.order_assigned_panel);
        orderDetails = view.findViewById(R.id.order_view_details);
        cancelButton = view.findViewById(R.id.order_view_cancel);
        actionButton = view.findViewById(R.id.order_view_action);
        assignedPanelButton = view.findViewById(R.id.order_confirmation);
        assignedPanelInfo = view.findViewById(R.id.order_message);
        progressBar = view.findViewById(R.id.details_progress);
        mapsPlaceholder = view.findViewById(R.id.maps_placeholder);

        // initialize default values
        variator = true;
        customerName = "";
        customerPhone = "";
        orderId = "0";

        // get extra from caller
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            orderId = bundle.getString("ORDER_ID_KEY", "0");
            Log.d(TAG, orderId);
        } else {
            Log.e(TAG, "Bundle is null");
        }

        // assign action for cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ContainerActivity) getActivity()).returnToPreviousFragment();
            }
        });


        return view;
    }

    /**
     * this function is called when view is resumed
     */
    @Override
    public void onResume() {
        super.onResume();
        controller.loadDetailList(orderId);
    }

    /**
     * Invoke loading of map with given coordinate pairs
     *
     * @param start Starting coordinate
     * @param end   Destination coordinate
     */
    public void loadMap(String start, String end) {
        showMapsPlaceholder(true);
        if (start == null || end == null) {
            showNotification(getString(R.string.navigation_error_message));
            return;
        }
        this.start = locationFromString(start);
        this.end = locationFromString(end);
        SupportMapFragment mf = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction()
                .add(R.id.map_window, mf)
                .commit();
        mf.getMapAsync(this);
    }

    /**
     * Reset the view and show loading state
     */
    @Override
    public void resetLoad() {
        showMapsPlaceholder(true);
        showProgressBar(true);
        orderDetails.removeAllViews();

    }

    /**
     * Invoke opening of calling menu in system
     *
     * @param number desired phone number
     */
    @Override
    public void openCaller(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(String.format("tel:%s", number)));
        startActivity(intent);
    }

    /**
     * Convert string to 'latlng' location with defined rule for ';' separator
     *
     * @param s string containing location
     * @return converted LatLng object
     */
    public LatLng locationFromString(String s) {
        if (s == null) {
            return null;
        }
        String sa[] = s.split(";");
        return new LatLng(Double.parseDouble(sa[0]), Double.parseDouble(sa[1]));
    }

    /**
     * this function is called when map api has finished loading resources for map
     *
     * @param googleMap map object
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        drawRoute(start, end);
    }

    /**
     * Create routing request for map api
     *
     * @param start start coordinate
     * @param end   end coordinate
     */
    private void drawRoute(LatLng start, LatLng end) {
        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(start, end)
                .key(getString(R.string.google_maps_api_key))
                .build();
        routing.execute();
    }

    /**
     * Add single entity to details list
     *
     * @param key   Name of entity, shown in left column
     * @param value Value of entity, shown in right column
     */
    public void addDetailEntity(String key, String value) {
        TableRow row = new TableRow(getContext());
        Resources resource = getContext().getResources();
        if (resource != null) {

            if (variator) {
                row.setBackgroundColor(resource.getColor(R.color.colorShaded));
            }
        }
        variator = !variator;

        row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 0f));

        TextView keyView = new TextView(getContext());
        TextView valueView = new TextView(getContext());

        keyView.setText(key);
        valueView.setText(value);
        final int pad = 7;
        keyView.setPadding(pad, pad, pad, pad);
        valueView.setPadding(pad, pad, pad, pad);

        // text size is relative so no need to extract the values
        keyView.setTextSize(17);
        valueView.setTextSize(17);

        keyView.setTypeface(null, Typeface.BOLD);
        valueView.setTypeface(null, Typeface.ITALIC);

        keyView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f));
        valueView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 3f));

        row.addView(keyView);
        row.addView(valueView);
        orderDetails.addView(row);
    }

    /**
     * Set a visibility of spinning progress bar indication, used to show that loading is in progress
     *
     * @param visibility true to show, false to hide
     */
    @Override
    public void showProgressBar(boolean visibility) {
        if (visibility) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void setActionState(int state) {
        try {
            setAssignedPanelState(state);
            if (state == 0) {
                actionButton.setText(getString(R.string.accept_naming));
                actionButton.setEnabled(true);
                actionButton.setVisibility(View.VISIBLE);
                actionButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorGreen));
                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        controller.acceptOrder(orderId);
                    }
                });
            } else if (state == 1) {
                actionButton.setText(getString(R.string.decline_naming));
                actionButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorPrimaryDark));
                actionButton.setEnabled(true);
                actionButton.setVisibility(View.VISIBLE);
                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        controller.cancelOrder(orderId);
                    }
                });
            } else if (state == 2) {
                actionButton.setText(getString(R.string.contact_naming));
                actionButton.setVisibility(View.VISIBLE);
                actionButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorAccent));
                actionButton.setEnabled(true);
                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCallSelector(customerName, customerPhone);
                    }
                });
            } else if (state == 3) {
                actionButton.setVisibility(View.INVISIBLE);
                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // do nothing
                    }
                });
            }
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void setAssignedPanelState(int state) {
        if (state == 0) {
            assignedPanelButton.setText(getString(R.string.available_naming));
            assignedPanelButton.setEnabled(false);
            assignedPanelInfo.setText(Html.fromHtml(getString(R.string.press_accept_info_message)));
            assignedPanelInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNotification(getString(R.string.accept_order_notif_message));
                }
            });
        } else if (state == 1) {
            assignedPanelButton.setText(getString(R.string.pick_naming));
            assignedPanelButton.setEnabled(true);
            assignedPanelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    receiveOrder();
                }
            });
            assignedPanelInfo.setText(Html.fromHtml(getString(R.string.press_locate_ware_message)));
            assignedPanelInfo.setEnabled(true);
            assignedPanelInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri navigationIntentUri = Uri.parse(String.format("google.navigation:q=%s,%s", start.latitude, start.longitude));//creating intent with latlng
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });
        } else if (state == 2) {
            assignedPanelButton.setText(getString(R.string.seliver_naming));
            assignedPanelButton.setEnabled(true);
            assignedPanelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deliverOrder();
                }
            });
            assignedPanelInfo.setEnabled(true);
            assignedPanelInfo.setText(Html.fromHtml(getString(R.string.press_locate_dest_message)));
            assignedPanelInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri navigationIntentUri = Uri.parse(String.format("google.navigation:q=%s,%s", end.latitude, end.longitude));//creating intent with latlng
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });
        } else if (state == 3) {
            assignedPanelButton.setText(getString(R.string.closed_naming));
            assignedPanelButton.setEnabled(true);
            assignedPanelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // do nothing
                }
            });
            assignedPanelInfo.setEnabled(true);
            assignedPanelInfo.setText(Html.fromHtml(getString(R.string.order_unavailable_cancel_message)));
            assignedPanelInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNotification(getString(R.string.order_unavailable_status_message));
                }
            });
        }
    }

    /**
     * Set customer data for this object corresponding to object it is assigned currently
     *
     * @param name  Name of customer
     * @param phone Phone number of customer
     */
    @Override
    public void setCustomer(String name, String phone) {
        this.customerName = name;
        this.customerPhone = phone;
    }

    /**
     * Make a call to invoke notification panel
     *
     * @param message - string that user should read
     */
    @Override
    public void showNotification(String message) {
        try {
            ((ContainerActivity) getActivity()).showNotification(message);
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Construct a panel for order pick confirmation
     */
    public void receiveOrder() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(getString(R.string.confirm_parcel_pick_query_message));
        final EditText input = new EditText(getContext());
        input.setGravity(Gravity.CENTER);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        alert.setView(input);
        alert.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                controller.pickOrder(orderId, input.getText().toString());
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Put actions for CANCEL button here, or leave in blank
            }
        });
        alert.setNeutralButton("SCAN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent launchIntent = BarcodeReaderActivity.getLaunchIntent(getContext(), true, false);
                startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);
            }
        });
        alert.show();
    }

    /**
     * collect results from activity 'launched for result'
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == BARCODE_READER_ACTIVITY_REQUEST && data != null) {
            Barcode barcode = data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE);
            if (checkCodeFormat(barcode.rawValue)) {
                // notification is shown from controller that processes the operation
            } else {
                showNotification(getString(R.string.invalid_barcode_format_message));
            }
            controller.pickOrder(orderId, barcode.rawValue);

        }

    }

    /**
     * Construct panel for the order deliver confirmation
     */
    public void deliverOrder() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(getString(R.string.enter_code_query_message));
        final EditText input = new EditText(getContext());
        input.setGravity(Gravity.CENTER);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        alert.setView(input);
        alert.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                controller.deliverOrder(orderId, input.getText().toString());
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // no action is required
            }
        });
        alert.setNeutralButton("SEND CODE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                controller.validateRecipient(orderId);
            }
        });
        alert.show();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        showNotification(getString(R.string.routing_failed_message));
    }

    /**
     * this method is called when routing is initiated
     */
    @Override
    public void onRoutingStart() {
        // do nothing due to initial state of view is constructed by other fragment facilities
    }

    /**
     * This function is called when routing request is fulfilled and maps service retured data
     */
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        try {
            // collect route segments
            showMapsPlaceholder(false);
            ArrayList<Polyline> polylines;
            polylines = new ArrayList<>();

            for (int i = 0; i < route.size(); i++) {
                PolylineOptions polyOptions = new PolylineOptions();
                polyOptions.color(getResources().getColor(R.color.colorBlue));
                polyOptions.width(10 + i * 3);
                polyOptions.addAll(route.get(i).getPoints());
                Polyline polyline = map.addPolyline(polyOptions);
                polylines.add(polyline);
            }

            // Start marker
            MarkerOptions options = new MarkerOptions();
            options.position(start);
            options.title(getString(R.string.warehouse_map_tag));
            map.addMarker(options);

            // End marker
            options = new MarkerOptions();
            options.position(end);
            options.title(getString(R.string.destination_map_tag));
            options.icon(getMarkerIcon("#840f82"));
            map.addMarker(options);

            // find route bounds to fit on map
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Route r : route) {
                for (LatLng l : r.getPoints())
                    builder.include(l);
            }
            LatLngBounds bounds = builder.build();

            // set padding so route won't contact edges of view
            int padding = 100; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            map.animateCamera(cu);
        } catch (Exception e) {
            // Ignore exeptions since they arise on async call returning to empty fragment
        }
    }

    /**
     * Construct icon marker icon
     *
     * @param color hex color representation
     * @return
     */
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }


    @Override
    public void onRoutingCancelled() {
        // ignore due to this option is not possible
    }

    /**
     * while map is loading, set overlay with map icon
     *
     * @param visibility
     */
    void showMapsPlaceholder(boolean visibility) {
        if (visibility) {
            mapsPlaceholder.setVisibility(View.VISIBLE);
        } else {
            mapsPlaceholder.setVisibility(View.GONE);
        }
    }

    /**
     * Construct a menu with calling options
     *
     * @param recipientName   name of recipient
     * @param recipientNumber phone number of recipient
     */
    public void showCallSelector(String recipientName, String recipientNumber) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Contact");
        View innerView = LayoutInflater.from(alert.getContext()).inflate(R.layout.caller_manu_snippet, null);
        TextView recipientNameView = innerView.findViewById(R.id.recipient_name);
        TextView recipientNumberView = innerView.findViewById(R.id.recipient_number);
        Button recipientCallButton = innerView.findViewById(R.id.recipient_call_button);
        Button recipientSMSButton = innerView.findViewById(R.id.recipient_sms_button);
        Button supportCallButton = innerView.findViewById(R.id.support_call_button);

        recipientNameView.setText(recipientName);
        recipientNumberView.setText(recipientNumber);

        recipientCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCaller(recipientNumber);
            }
        });
        recipientSMSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", recipientNumber);
                smsIntent.putExtra("sms_body", R.string.default_sms_message);
                try {
                    getContext().startActivity(Intent.createChooser(smsIntent, "SMS:"));
                } catch (NullPointerException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        supportCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.getSupportNumber();
            }
        });
        alert.setView(innerView);
        alert.show();
    }

    /**
     * Check if given value matches application defined code format
     *
     * @param val value to be checker
     * @return true if it matches the format, false otherwise
     */
    public boolean checkCodeFormat(String val) {
        return val.matches(codeChecker);
    }
}
