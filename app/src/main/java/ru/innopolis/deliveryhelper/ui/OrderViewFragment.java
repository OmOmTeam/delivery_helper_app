package ru.innopolis.deliveryhelper.ui;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import ru.innopolis.deliveryhelper.ContainerMVC;
import ru.innopolis.deliveryhelper.OrderViewMVC;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.controller.OrderViewController;

public class OrderViewFragment extends Fragment implements OrderViewMVC.View{

    private OrderViewMVC.Controller controller;

    TableLayout orderDetails;
    Button cancelButton;
    Button actionButton;
    ImageView imageHolder;
    ProgressBar progressBar;
    private boolean variator;
    private int actionState;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderview, container, false);

        controller = new OrderViewController(this);
        variator = true;
        orderDetails = view.findViewById(R.id.order_view_details);
        cancelButton = view.findViewById(R.id.order_view_cancel);
        actionButton = view.findViewById(R.id.order_view_action);
        imageHolder = view.findViewById(R.id.image_holder);
        progressBar = view.findViewById(R.id.details_progress);
        actionState = 2;

        Bundle bundle = this.getArguments();
        String order = "0";
        if (bundle != null) {
            order = bundle.getString("ORDER_ID_KEY", "0");
            Log.d("ORDER_DET", order);
        }else{
            showNotification("Bundle IS NULL");
        }

        Picasso.with(getContext()).load("https://images.mentalfloss.com/sites/default/files/styles/mf_image_16x9/public/traffic_primary.jpg?itok=6hVRNcu_&resize=1100x1100").into(imageHolder);

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
}
