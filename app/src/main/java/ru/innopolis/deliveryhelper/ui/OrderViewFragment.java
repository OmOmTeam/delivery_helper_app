package ru.innopolis.deliveryhelper.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.google.android.gms.maps.MapFragment;
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
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import jp.wasabeef.blurry.Blurry;
import ru.innopolis.deliveryhelper.ContainerMVC;
import ru.innopolis.deliveryhelper.OrderViewMVC;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.controller.OrderViewController;
import ru.innopolis.deliveryhelper.gmsInterface;

public class OrderViewFragment extends Fragment implements OrderViewMVC.View, RoutingListener, OnMapReadyCallback {

    private static final String TAG = "OrderViewFragment";
    private OrderViewMVC.Controller controller;

    TableLayout orderDetails;
    Button cancelButton;
    Button pickButton;
    ImageView imageHolder;
    ProgressBar progressBar;
    View assignedPanel;
    private boolean variator;

    private int actionState;
    Button actionButton;

    private int assignedPanelState;
    private Button assignedPanelButton;
    private Button assignedPanelInfo;

    GoogleMap map;
    ArrayList<Marker> markers;
    LatLng start, end;

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
        pickButton = view.findViewById(R.id.order_confirmation);
        assignedPanelButton = view.findViewById(R.id.order_confirmation);
        assignedPanelInfo = view.findViewById(R.id.order_message);
        progressBar = view.findViewById(R.id.details_progress);
        actionState = 2;

        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveOrder();
            }
        });

        Bundle bundle = this.getArguments();
        String order = "0";
        if (bundle != null) {
            order = bundle.getString("ORDER_ID_KEY", "0");
            Log.d("ORDER_DET", order);
        } else {
            Log.e(TAG, "Bundle is null");
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ContainerActivity) getActivity()).openOrderList();
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionState == 0) {
                    showNotification("Assigning delivery");
                } else if (actionState == 1) {
                    showNotification("Declining delivery");
                }

            }
        });
        controller.loadDetailList(order);

        return view;
    }

    public void loadMap(String start, String end) {
        if (start==null || end == null){
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

    public LatLng locationFromString(String s){
        if (s==null){
            return null;
        }
        String sa[] = s.split(";");
        return new LatLng(Double.parseDouble(sa[0]),Double.parseDouble(sa[1]));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        showNotification("Here");
        this.map = googleMap;
        drawRoute(start, end);
    }

    private void drawRoute(LatLng start, LatLng end) {
        showNotification("Routing Started");
        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(start, end)
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
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setActionState(int state) {
        this.actionState = state;
        if (state == 0) {
            actionButton.setText("accept");
            actionButton.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            actionButton.setEnabled(true);
        } else if (state == 1) {
            actionButton.setText("decline");
            actionButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            actionButton.setEnabled(true);
        } else {
            actionButton.setText("unavailable");
            actionButton.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            actionButton.setEnabled(false);
        }
    }

    public void setAssignedPanelState(int state) {
        this.assignedPanelState = state;
        if (state == 0) {
            assignedPanelButton.setText("available");
            assignedPanelButton.setEnabled(false);
            assignedPanelInfo.setText(Html.fromHtml("Press <b>ACCEPT</b> to assign order"));
        } else if (state == 1) {
            assignedPanelButton.setText("pick");
            assignedPanelInfo.setText(Html.fromHtml("Visit warehouse to pick item"));
        } else if (state == 2) {
            assignedPanelButton.setText("deliver");
            assignedPanelInfo.setText(Html.fromHtml("Press to send approval code to customer"));
        }
    }

    @Override
    public void showNotification(String message) {
        ((ContainerActivity) getActivity()).showNotification(message);
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
                //Put actions for OK button here
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Put actions for CANCEL button here, or leave in blank
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
        options.title("Destination");
        map.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(end);
        options.title("Warehouse");
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

    }

    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    @Override
    public void onRoutingCancelled() {

    }


}
