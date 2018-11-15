package ru.innopolis.deliveryhelper.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private SwipeRefreshLayout swipeRefresh;


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
        swipeRefresh = view.findViewById(R.id.swiperefresh);


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

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                controller.loadOrderList();
            }
        });

        return view;
    }

    /**
     * Hide spinner animation in after scrollview refresh complete
     */
    @Override
    public void hideRefreshing(){
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    /**
     * Perform actions when app is reopened
     */
    @Override
    public void onResume() {
        super.onResume();
        controller.loadOrderList();
    }

    /**
     * Insert list data into list view
     *
     * @param orderList list of actual order elements
     */
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


    /**
     * Show or hide message overlay telling that the list is empty
     *
     * @param visibility true to show, false otherwise
     */
    public void showEmptyListInfo(boolean visibility){
        if(visibility){
            emptyListInfo.setVisibility(View.VISIBLE);
        }else{
            emptyListInfo.setVisibility(View.GONE);
        }
    }

    /**
     * Hide spinning load indicator over the list view
     */
    @Override
    public void hideProgressBar() {
        try {
            progressBar = getView().findViewById(R.id.order_list_progress);
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Show spinning load indicator over the list view
     */
    @Override
    public void showProgressBar() {
        try {
            progressBar = getView().findViewById(R.id.order_list_progress);
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
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


    /**
     * Show a notification to user
     *
     * @param message - string that user should read
     */
    @Override
    public void showNotification(String message) {
        try {
            ((ContainerActivity) getActivity()).showNotification(message);
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
