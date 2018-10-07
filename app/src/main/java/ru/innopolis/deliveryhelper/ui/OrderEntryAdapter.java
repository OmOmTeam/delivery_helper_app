package ru.innopolis.deliveryhelper.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.innopolis.deliveryhelper.OrderEntryAdapterCallback;
import ru.innopolis.deliveryhelper.R;

public class OrderEntryAdapter extends ArrayAdapter<OrderEntry> {
    private Context oContext;
    private List<OrderEntry> orderList = new ArrayList<>();

    OrderEntryAdapterCallback callback;
    /**
     * Adapter for the applet list
     */
    public OrderEntryAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<OrderEntry> list) {
        super(context, 0, list);
        oContext = context;
        orderList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(oContext).inflate(R.layout.order_entry_listitem, parent, false);

        final OrderEntry currentEntry = orderList.get(position);
        ImageView image = listItem.findViewById(R.id.order_entry_icon);
        TextView title = listItem.findViewById(R.id.order_entry_title);
        TextView address = listItem.findViewById(R.id.order_entry_address);
        TextView weight = listItem.findViewById(R.id.order_entry_weight);
        Button btn = listItem.findViewById(R.id.order_accept_button);

        image.setImageResource(currentEntry.getIcon());
        title.setText(currentEntry.getTitle());
        address.setText(currentEntry.getAddress());
        weight.setText(currentEntry.getWeight());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.assignOrder(currentEntry.getOrderId());
            }
        });

        return listItem;
    }

    public void setCallback(OrderEntryAdapterCallback callback){
        this.callback = callback;
    }

}
