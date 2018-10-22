package ru.innopolis.deliveryhelper.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.innopolis.deliveryhelper.ContainerMVC;
import ru.innopolis.deliveryhelper.OrderEntryAdapterCallback;
import ru.innopolis.deliveryhelper.OrderListMVC;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.controller.OrderListController;
import ru.innopolis.deliveryhelper.model.dataframes.response.ItemHeaderResponseModel;


public class OrderListFragment extends Fragment implements OrderListMVC.View, OrderEntryAdapterCallback {

    private OrderEntryAdapter oAdapter;
    private ListView listView;
    private OrderListMVC.Controller controller;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderlist, container, false);

        getActivity().setTitle("Available Orders");
        listView = view.findViewById(R.id.all_orders_listview);
        controller = new OrderListController(this);

        try {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, android.view.View view, int i, long l) {
                    ((ContainerActivity) getActivity()).openOrderView(oAdapter.getOrderList().get(i).getOrderId());
                }
            });
        } catch (NullPointerException e) {
            Log.e("NULL CAUGHT", e.getMessage());
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        controller.loadOrderList();
    }

    public void updateList(List<ItemHeaderResponseModel> orderList) {
        try {
            oAdapter = new OrderEntryAdapter(getContext(), orderList);
            oAdapter.setCallback(this);
            listView.setAdapter(oAdapter);
        } catch (NullPointerException e) {
            Log.e("NULL CAUGHT", e.getMessage());
        }
    }

    @Override
    public void hideProgressBar() {
        try {
            progressBar = getView().findViewById(R.id.order_list_progress);
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            Log.e("NULL CAUGHT", e.getMessage());
        }
    }

    @Override
    public void showProgressBar() {
        try {
            progressBar = getView().findViewById(R.id.order_list_progress);
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException e) {
            Log.e("NULL CAUGHT", e.getMessage());
        }
    }

    /**
     * Delete all items from current list in activity
     */
    public void clearList() {
        if (oAdapter!=null && oAdapter.getOrderList()!=null){
            oAdapter.getOrderList().clear();
        }
    }

    @Override
    public void showNotification(String message) {
        try {
            ((ContainerActivity) getActivity()).showNotification(message);
        } catch (NullPointerException e) {
            Log.e("NULL CAUGHT", e.getMessage());
        }
    }

    @Override
    public void assignOrder(String orderId) {
        showNotification("Trying to assign: " + orderId);
    }
}
