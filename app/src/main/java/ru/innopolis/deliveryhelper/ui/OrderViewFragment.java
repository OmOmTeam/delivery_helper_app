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

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;

import ru.innopolis.deliveryhelper.OrderViewMVC;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.controller.OrderViewController;

public class OrderViewFragment extends Fragment implements OrderViewMVC.View, RoutingListener, OnMapReadyCallback {

    private static final String TAG = "OrderViewFragment";
    private OrderViewMVC.Controller controller;

    TableLayout orderDetails;
    Button cancelButton;
    ProgressBar progressBar;
    View assignedPanel;
    private boolean variator;

    private int actionState;
    Button actionButton;

    private String customer_name;
    private String customer_phone;
    private String order_id;
    private Button assignedPanelButton;
    private Button assignedPanelInfo;

    private View mapsPlaceholder;
    GoogleMap map;
    ArrayList<Marker> markers;
    LatLng start, end;
    static final int BARCODE_READER_ACTIVITY_REQUEST = 1;  // The request code
    private String codeChecker = "\\d{8}";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Order View");
        View view = inflater.inflate(R.layout.fragment_orderview, container, false);

        controller = new OrderViewController(this);
        variator = true;
        assignedPanel = view.findViewById(R.id.order_assigned_panel);
        orderDetails = view.findViewById(R.id.order_view_details);
        cancelButton = view.findViewById(R.id.order_view_cancel);
        actionButton = view.findViewById(R.id.order_view_action);
        assignedPanelButton = view.findViewById(R.id.order_confirmation);
        assignedPanelInfo = view.findViewById(R.id.order_message);
        progressBar = view.findViewById(R.id.details_progress);
        mapsPlaceholder = view.findViewById(R.id.maps_placeholder);
        customer_name = "";
        customer_phone = "";

        Bundle bundle = this.getArguments();
        order_id = "0";
        if (bundle != null) {
            order_id = bundle.getString("ORDER_ID_KEY", "0");
            Log.d("ORDER_DET", order_id);
        } else {
            Log.e(TAG, "Bundle is null");
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ContainerActivity) getActivity()).returnToPreviousFragment();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        controller.loadDetailList(order_id);
    }

    public void loadMap(String start, String end) {
        showMapsPlaceholder(true);
        if (start == null || end == null) {
            //show error panel
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

    @Override
    public void resetLoad() {
        showMapsPlaceholder(true);
        showProgressBar(true);
        orderDetails.removeAllViews();

    }

    @Override
    public void openCaller(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(String.format("tel:%s", number)));
        startActivity(intent);
    }

    public LatLng locationFromString(String s) {
        if (s == null) {
            return null;
        }
        String sa[] = s.split(";");
        return new LatLng(Double.parseDouble(sa[0]), Double.parseDouble(sa[1]));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        drawRoute(start, end);
    }

    private void drawRoute(LatLng start, LatLng end) {
        LatLng f1 = new LatLng(55.893270, 49.019864);
        LatLng f2 = new LatLng(55.872593, 49.022668);
        LatLng f3 = new LatLng(55.885258, 49.023553);
        LatLng f4 = new LatLng(55.898021, 49.030424);

        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(start, end) //f1,f3,f2,f4)
                .key("AIzaSyCr5Uq134ZDPO-02wjPsUow2rwQrfKP99s")
                .build();
        routing.execute();
    }

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

        //TODO: extract int resources

        final int pad = 7;
        keyView.setPadding(pad, pad, pad, pad);
        valueView.setPadding(pad, pad, pad, pad);

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
        this.actionState = state;
        setAssignedPanelState(state);
        if (state == 0) {
            actionButton.setText("accept");
            actionButton.setEnabled(true);
            actionButton.setVisibility(View.VISIBLE);
            actionButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorGreen));
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    controller.acceptOrder(order_id);
                }
            });
        } else if (state == 1) {
            actionButton.setText("decline");
            actionButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorPrimaryDark));
            actionButton.setEnabled(true);
            actionButton.setVisibility(View.VISIBLE);
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    controller.cancelOrder(order_id);
                }
            });
        } else if (state == 2) {
            actionButton.setText("contact");
            actionButton.setVisibility(View.VISIBLE);
            actionButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorAccent));
            actionButton.setEnabled(true);
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCallSelector(customer_name, customer_phone);
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
    }

    public void setAssignedPanelState(int state) {
        if (state == 0) {
            assignedPanelButton.setText("available");
            assignedPanelButton.setEnabled(false);
            assignedPanelInfo.setText(Html.fromHtml("Press <b>ACCEPT</b> to assign order"));
            assignedPanelInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNotification("Press <b>ACCEPT</b> button at the bottom of screen to request this order to be assigned to you.");
                }
            });
        } else if (state == 1) {
            assignedPanelButton.setText("pick");
            assignedPanelButton.setEnabled(true);
            assignedPanelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    receiveOrder();
                }
            });
            assignedPanelInfo.setText(Html.fromHtml("Click to locate warehouse"));
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
            assignedPanelButton.setText("deliver");
            assignedPanelButton.setEnabled(true);
            assignedPanelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deliverOrder();
                }
            });
            assignedPanelInfo.setEnabled(true);
            assignedPanelInfo.setText(Html.fromHtml("Press to locate destination"));
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
            assignedPanelButton.setText("unavailable");
            assignedPanelButton.setEnabled(true);
            assignedPanelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // do nothing
                }
            });
            assignedPanelInfo.setEnabled(true);
            assignedPanelInfo.setText(Html.fromHtml("Press <b>CLOSE</b> and select another order"));
            assignedPanelInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNotification("This order is either finished or unavailable to you :(");
                }
            });
        }
    }

    @Override
    public void setCustomer(String name, String phone) {
        this.customer_name = name;
        this.customer_phone = phone;
    }

    @Override
    public void showNotification(String message) {
        try {
            ((ContainerActivity) getActivity()).showNotification(message);
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void receiveOrder() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Confirm parcel is picked for delivery: ");
        final EditText input = new EditText(getContext());
        input.setGravity(Gravity.CENTER);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        alert.setView(input);
        alert.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                controller.pickOrder(order_id, input.getText().toString());
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
                showNotification(String.format("Invalid barcode format, try typing manually", barcode.rawValue));
            }
            controller.pickOrder(order_id, barcode.rawValue);

        }

    }

    public void deliverOrder() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Enter code that customer has received: ");
        final EditText input = new EditText(getContext());
        input.setGravity(Gravity.CENTER);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        alert.setView(input);
        alert.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                controller.deliverOrder(order_id, input.getText().toString());
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Put actions for CANCEL button here, or leave in blank
            }
        });
        alert.setNeutralButton("RESEND CODE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showNotification("RESENDING CODE");
            }
        });
        alert.show();
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        try {
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
            options.title("Warehouse");
            map.addMarker(options);

            // End marker
            options = new MarkerOptions();
            options.position(end);
            options.title("Destination");
            options.icon(getMarkerIcon("#840f82"));
            map.addMarker(options);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Route r : route) {
                for (LatLng l : r.getPoints())
                    builder.include(l);
            }
            LatLngBounds bounds = builder.build();

            int padding = 100; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            map.moveCamera(cu);
            map.animateCamera(cu);
        } catch (Exception e) {
            // Ignore exeptions since they arise on async call returning to empty fragment
        }
    }

    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    @Override
    public void onRoutingCancelled() {

    }

    void showMapsPlaceholder(boolean visibility) {
        if (visibility) {
            mapsPlaceholder.setVisibility(View.VISIBLE);
        } else {
            mapsPlaceholder.setVisibility(View.GONE);
        }
    }

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

    public boolean checkCodeFormat(String val) {
        return val.matches(codeChecker);
    }
}
