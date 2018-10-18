package ru.innopolis.deliveryhelper.ui;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import ru.innopolis.deliveryhelper.ContainerMVC;
import ru.innopolis.deliveryhelper.OrderViewMVC;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.controller.OrderViewController;

public class OrderViewFragment extends Fragment implements OrderViewMVC.View, RoutingListener {

    private OrderViewMVC.Controller controller;

    TableLayout orderDetails;
    Button cancelButton;
    Button actionButton;
    ImageView imageHolder;
    ProgressBar progressBar;
    private boolean variator;
    private int actionState;
    SupportMapFragment mapFragment;
    GoogleMap map;
    ArrayList<Marker> markers;
    LatLng start, waypoint, end;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;
//        try {
            view = inflater.inflate(R.layout.fragment_orderview, container, false);
//        } catch (InflateException e) {
//            /* map is already there, just return view as it is */
//        }

        controller = new OrderViewController(this);
        variator = true;
        orderDetails = view.findViewById(R.id.order_view_details);
        cancelButton = view.findViewById(R.id.order_view_cancel);
        actionButton = view.findViewById(R.id.order_view_action);
//        imageHolder = view.findViewById(R.id.image_holder);
        progressBar = view.findViewById(R.id.details_progress);
        actionState = 2;
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_window);

        start = new LatLng(55.753320, 48.741012);
        end = new LatLng(55.817458, 49.130425);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
//                showNotification("Map is ready");
                map = googleMap;
                drawRoute(start, end);
            }
        });

        Bundle bundle = this.getArguments();
        String order = "0";
        if (bundle != null) {
            order = bundle.getString("ORDER_ID_KEY", "0");
            Log.d("ORDER_DET", order);
        }else{
            showNotification("Bundle IS NULL");
        }

//        Picasso.with(getContext()).load("https://images.mentalfloss.com/sites/default/files/styles/mf_image_16x9/public/traffic_primary.jpg?itok=6hVRNcu_&resize=1100x1100").into(imageHolder);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ContainerActivity)getActivity()).openOrderList();
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actionState==0){
                    showNotification("Assigning delivery");
                }else if(actionState==1){
                    showNotification("Declining delivery");
                }

            }
        });
        controller.loadDetailList(order);

        return view;
    }

    public void drawRoute(LatLng start, LatLng end) {

        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(start, end)
                .key("AIzaSyCr5Uq134ZDPO-02wjPsUow2rwQrfKP99s")
                .build();
        routing.execute();
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        SupportMapFragment f = (SupportMapFragment) getFragmentManager()
//                .findFragmentById(R.id.map_window);
//        if (f != null)
//            getFragmentManager().beginTransaction().remove(f).commit();
//    }

    public void addDetailEntity(String key, String value) {
        TableRow row = new TableRow(getContext());
        Resources resource = getContext().getResources();
        if(resource!=null){

            if(variator) {
                row.setBackgroundColor(resource.getColor(R.color.colorShaded));
            }
        }
        variator = !variator;

        row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,0f));

        TextView keyView = new TextView(getContext());
        TextView valueView = new TextView(getContext());

        keyView.setText(key);
        valueView.setText(value);

        final int pad = 7;
        keyView.setPadding(pad,pad,pad,pad);
        valueView.setPadding(pad,pad,pad,pad);

        //TODO: extract int resources
        keyView.setTextSize(17);
        valueView.setTextSize(17);

        keyView.setTypeface(null, Typeface.BOLD);
        valueView.setTypeface(null, Typeface.ITALIC);

        keyView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f));
        valueView.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.MATCH_PARENT, 3f));

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
        if(state==0){
            actionButton.setText("accept");
            actionButton.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            actionButton.setEnabled(true);
        }else if(state==1){
            actionButton.setText("decline");
            actionButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            actionButton.setEnabled(true);
        }else{
            actionButton.setText("unavailable");
            actionButton.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            actionButton.setEnabled(false);
        }
    }

    @Override
    public void showNotification(String message) {
        ((ContainerActivity)getActivity()).showNotification(message);
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

//        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
//
//        map.moveCamera(center);
//        map.animateCamera(zoom);




        ArrayList<Polyline> polylines;
//        if(polylines.size()>0) {
//            for (Polyline poly : polylines) {
//                poly.remove();
//            }
//        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
//            int colorIndex = i % COLORS.length;

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
        Marker a = map.addMarker(options);
//        markers.add(a);
        // End marker
        options = new MarkerOptions();
        options.position(end);
        options.title("Warehouse");
        options.icon(getMarkerIcon("#840f82"));
        Marker b = map.addMarker(options);
//        markers.add(b);

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
