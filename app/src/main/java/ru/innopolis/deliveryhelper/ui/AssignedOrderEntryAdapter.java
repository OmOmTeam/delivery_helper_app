package ru.innopolis.deliveryhelper.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.model.dataframes.response.ItemHeaderResponseModel;

import static ru.innopolis.deliveryhelper.model.PlainConsts.order_status_code_picked;
import static ru.innopolis.deliveryhelper.model.PlainConsts.order_type_code_bparcel;
import static ru.innopolis.deliveryhelper.model.PlainConsts.order_type_code_letter;
import static ru.innopolis.deliveryhelper.model.PlainConsts.order_type_code_palette;
import static ru.innopolis.deliveryhelper.model.PlainConsts.order_type_code_sparcel;
import static ru.innopolis.deliveryhelper.model.PlainConsts.picked_order_naming;

public class AssignedOrderEntryAdapter extends ArrayAdapter<ItemHeaderResponseModel> {
    private Context oContext;
    private List<ItemHeaderResponseModel> orderList = new ArrayList<>();

    /**
     * Adapter for the applet list
     */
    public AssignedOrderEntryAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes List<ItemHeaderResponseModel> list) {
        super(context, 0, list);
        oContext = context;
        orderList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(oContext).inflate(R.layout.assigned_order_entry_listitem, parent, false);

        // find elements in UI
        final ItemHeaderResponseModel currentEntry = orderList.get(position);
        ImageView image = listItem.findViewById(R.id.order_entry_icon);
        TextView title = listItem.findViewById(R.id.order_entry_title);
        TextView dimensions = listItem.findViewById(R.id.order_entry_sub_1);
        TextView weight = listItem.findViewById(R.id.order_entry_sub_2);
        Button acceptEntryButton = listItem.findViewById(R.id.order_accept_button);

        // modify element display badge if order status is 'picked'
        if (currentEntry.getStateCode().equals(order_status_code_picked)) {
            acceptEntryButton.setText(picked_order_naming);
            acceptEntryButton.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorGreen));
        }

        // set icon based on order type
        String orderType = currentEntry.getOrderType();
        switch (orderType) {
            case order_type_code_letter:
                image.setImageResource(R.drawable.icon_letter);
                break;
            case order_type_code_sparcel:
                image.setImageResource(R.drawable.small_box);
                break;
            case order_type_code_bparcel:
                image.setImageResource(R.drawable.large_box);
                break;
            case order_type_code_palette:
                image.setImageResource(R.drawable.pallette);
                break;
            default:
                image.setImageResource(R.drawable.icon_letter);
        }

        // set text in information preview on entry
        title.setText(currentEntry.getTitle());
        dimensions.setText(currentEntry.getDimensions());
        weight.setText(currentEntry.getWeight());

        return listItem;
    }

    public List<ItemHeaderResponseModel> getOrderList() {
        return orderList;
    }
}
