package ru.innopolis.deliveryhelper.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.innopolis.deliveryhelper.ContainerMVC;
import ru.innopolis.deliveryhelper.OrderEntryAdapterCallback;
import ru.innopolis.deliveryhelper.OrderListMVC;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.controller.OrderListController;


public class OrderListFragment extends Fragment implements OrderListMVC.View, OrderEntryAdapterCallback {

    private ArrayList<OrderEntry> orderList;
    private OrderEntryAdapter oAdapter;
    private ListView listView;
    private OrderListMVC.Controller controller;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderlist, container, false);

        orderList = new ArrayList<>();
        listView = view.findViewById(R.id.all_orders_listview);
        controller = new OrderListController(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, android.view.View view, int i, long l) {
                ((ContainerActivity) getActivity()).openOrderView(orderList.get(i).getOrderId());
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        controller.loadOrderList();
    }

    public void updateList() {
        oAdapter = new OrderEntryAdapter(getContext(), orderList);
        oAdapter.setCallback(this);
        listView.setAdapter(oAdapter);
        //setEmptyMessageNotificationVisibility(aAdapter.isEmpty());
    }

    @Override
    public void hideProgressBar() {
        progressBar = getView().findViewById(R.id.order_list_progress);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgressBar() {
        progressBar = getView().findViewById(R.id.order_list_progress);
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void addEntity(String orderId, String title, String address, String weight, String dimensions, String distanceFromWarehouse, String type) {
        orderList.add(new OrderEntry(orderId, title, address, weight, dimensions, distanceFromWarehouse, R.drawable.ic_letter));
    }

    /**
     * Delete all items from current list in activity
     */
    public void clearList() {
        orderList.clear();
        updateList();
    }

    @Override
    public void showNotification(String message) {
        ((ContainerActivity) getActivity()).showNotification(message);
    }

    @Override
    public void assignOrder(String orderId) {
        showNotification("Trying to assign: " + orderId);
    }
}
