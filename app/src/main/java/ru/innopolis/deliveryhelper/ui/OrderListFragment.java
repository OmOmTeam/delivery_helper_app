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

import java.util.List;

import ru.innopolis.deliveryhelper.OrderListMVC;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.controller.OrderListController;
import ru.innopolis.deliveryhelper.model.dataframes.response.ItemHeaderResponseModel;


public class OrderListFragment extends Fragment implements OrderListMVC.View {

    private static final String TAG = "OrderListFragment";
    private OrderEntryAdapter oAdapter;
    private ListView listView;
    private OrderListMVC.Controller controller;
    private ProgressBar progressBar;
    private View emptyListInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderlist, container, false);

        // set a title on top of page
        try{
            getActivity().setTitle("Available Orders");
        }catch(NullPointerException e){
            Log.e(TAG, e.getMessage());
        }

        // assign control entities for this view
        listView = view.findViewById(R.id.all_orders_listview);
        controller = new OrderListController(this);
        emptyListInfo = view.findViewById(R.id.empty_list_info);

        // assign actions for items of the list view
        try {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, android.view.View view, int i, long l) {
                    ((ContainerActivity) getActivity()).openOrderView(oAdapter.getOrderList().get(i).getOrderId());
                }
            });
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }

        return view;
    }

    // perform reload when given view is reopened
    @Override
    public void onResume() {
        super.onResume();
        controller.loadOrderList();
    }

    //
    public void updateList(List<ItemHeaderResponseModel> orderList) {
        try {
            if (orderList.isEmpty()){
                showEmptyListInfo(true);
                listView.setAdapter(null);
            }else{
                showEmptyListInfo(false);
                oAdapter = new OrderEntryAdapter(getContext(), orderList);
                listView.setAdapter(oAdapter);
            }
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void showEmptyListInfo(boolean visibility){
        if(visibility){
            emptyListInfo.setVisibility(View.VISIBLE);
        }else{
            emptyListInfo.setVisibility(View.GONE);
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
}
