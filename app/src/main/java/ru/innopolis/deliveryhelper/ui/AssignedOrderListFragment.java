package ru.innopolis.deliveryhelper.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ru.innopolis.deliveryhelper.OrderListMVC;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.controller.AssignedOrderListController;
import ru.innopolis.deliveryhelper.model.dataframes.response.ItemHeaderResponseModel;

import static ru.innopolis.deliveryhelper.model.PlainConsts.optimal_order_template;

public class AssignedOrderListFragment extends Fragment implements OrderListMVC.View {

    private static final String TAG = "AssignedOrderListFrag";
    // Controller for that view
    private OrderListMVC.Controller controller;

    // Main list element in fragment
    private AssignedOrderEntryAdapter oAdapter;
    private ListView listView;

    // UI elements in fragment
    private ProgressBar progressBar;
    private View emptyListInfo;
    private View suggestionPanel;
    private TextView suggestionContent;
    private Button suggestionButton;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assigned_order_list, container, false);

        // set the title for page
        try {
            getActivity().setTitle("Assigned Orders");
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }

        // create controller
        controller = new AssignedOrderListController(this);

        // find ui elements in view
        listView = view.findViewById(R.id.all_orders_listview);
        emptyListInfo = view.findViewById(R.id.empty_list_info);
        suggestionPanel = view.findViewById(R.id.suggestion_panel);
        suggestionContent = view.findViewById(R.id.suggestion_message);
        suggestionButton = view.findViewById(R.id.suggestion_button);
        swipeRefresh = view.findViewById(R.id.swiperefresh);

        // ensure that suggestion is closed while page is loading
        setSuggestionPanelVisibie(false);

        // assign list interaction actions
        try {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
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
    public void hideRefreshing() {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    /**
     * Try to find most optimal order from the list of assigned orders to make a suggestion for delivery operator
     */
    public void makeOptimalOrderSuggestion() {
        int index = getOptimalOrderIndex();
        if (index >= 0) {
            List<ItemHeaderResponseModel> list = oAdapter.getOrderList();
            ItemHeaderResponseModel order = list.get(index);
            setSuggestionPanelContent(String.format(optimal_order_template,
                    order.getTitle(), order.getDeliveryTimeFrom(), order.getDeliveryTimeTo()));
            setSuggestionReferrer(order.getOrderId());
            setSuggestionPanelVisibie(true);
        } else {
            setSuggestionPanelVisibie(false);
        }
    }

    /**
     * Get order with nearest closing time
     *
     * @return index of the order
     */
    private int getOptimalOrderIndex() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        int optimalOrderIndex = -1;
        long minTimeTo = Long.MAX_VALUE;

        List<ItemHeaderResponseModel> list = oAdapter.getOrderList();
        for (int i = 0; i < list.size(); ++i) {
            try {
                // compare time to order and find the nearest
                Date date = sdf.parse(list.get(i).getDeliveryTimeTo());
                Date now = new Date();
                long timeTo = date.getTime() - now.getTime();
                if (timeTo >= 0 && timeTo < minTimeTo) {
                    minTimeTo = timeTo;
                    optimalOrderIndex = i;
                }
            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return optimalOrderIndex;
    }

    /**
     * Show or hide message overlay telling that the list is empty
     *
     * @param visibility true to show, false otherwise
     */
    public void showEmptyListInfo(boolean visibility) {
        if (visibility) {
            emptyListInfo.setVisibility(View.VISIBLE);
        } else {
            emptyListInfo.setVisibility(View.GONE);
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
            if (orderList.isEmpty()) {
                showEmptyListInfo(true);
                listView.setAdapter(null);
            } else {
                showEmptyListInfo(false);
                oAdapter = new AssignedOrderEntryAdapter(getContext(), orderList);
                listView.setAdapter(oAdapter);
                makeOptimalOrderSuggestion();
            }

        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
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
        if (oAdapter != null && oAdapter.getOrderList() != null) {
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
        ((ContainerActivity) getActivity()).showNotification(message);
    }

    /**
     * Show or hide suggestion panel for optimal order
     *
     * @param state true to show, false otherwise
     */
    public void setSuggestionPanelVisibie(boolean state) {
        if (state) {
            suggestionPanel.setVisibility(View.VISIBLE);
        } else {
            suggestionPanel.setVisibility(View.GONE);
        }
    }

    /**
     * Set content for the order that will be actually shown as optimal order
     *
     * @param content string with html formatting
     */
    public void setSuggestionPanelContent(String content) {
        suggestionContent.setText(Html.fromHtml(content));
    }

    /**
     * Set action to open order view with optimal order
     *
     * @param orderId the id of optimal order in suggestion panel
     */
    public void setSuggestionReferrer(String orderId) {
        if (orderId != null && !orderId.isEmpty()) {
            suggestionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ((ContainerActivity) getActivity()).openOrderView(orderId);
                    } catch (NullPointerException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });
        } else {
            suggestionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // set empty listener
                }
            });
        }
    }
}
