package ru.innopolis.deliveryhelper.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import ru.innopolis.deliveryhelper.R;

public class AssignedOrderListFragment extends Fragment {

    private ArrayList<OrderEntry> orderList;
    private OrderEntryAdapter oAdapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderlist, container, false);

        orderList = new ArrayList<>();

        listView = view.findViewById(R.id.all_orders_listview);
//        ButterKnife.bind(view);

        return view;
    }

    private void updateList() {
        oAdapter = new OrderEntryAdapter(getContext(), orderList);
        //oAdapter.setCallback(this);
        listView.setAdapter(oAdapter);
        //setEmptyMessageNotificationVisibility(aAdapter.isEmpty());
    }

    public void addEntity(String orderId, String title, String address, String weight, String dimensions, String distanceFromWarehouse,  String type) {
        orderList.add(new OrderEntry(orderId, title, address, weight, dimensions, distanceFromWarehouse, R.drawable.ic_letter));
    }

    /**
     * Delete all items from current list in activity
     */
    public void clearList() {
        orderList.clear();
        updateList();
    }
}
