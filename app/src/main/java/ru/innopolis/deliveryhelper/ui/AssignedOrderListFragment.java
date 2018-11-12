package ru.innopolis.deliveryhelper.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import org.apache.commons.lang3.ObjectUtils;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ru.innopolis.deliveryhelper.OrderListMVC;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.controller.AssignedOrderListController;
import ru.innopolis.deliveryhelper.controller.OrderListController;
import ru.innopolis.deliveryhelper.model.dataframes.response.ItemHeaderResponseModel;

public class AssignedOrderListFragment extends Fragment implements OrderListMVC.View {

    private static final String TAG = "AssignedOrderListFrag";
    private AssignedOrderEntryAdapter oAdapter;
    private ListView listView;
    private OrderListMVC.Controller controller;
    private ProgressBar progressBar;
    private View emptyListInfo;
    private View suggestionPanel;
    private TextView suggestionContent;
    private Button suggestionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assigned_order_list, container, false);

        getActivity().setTitle("Assigned Orders");
        controller = new AssignedOrderListController(this);
        listView = view.findViewById(R.id.all_orders_listview);
        emptyListInfo = view.findViewById(R.id.empty_list_info);
        suggestionPanel = view.findViewById(R.id.suggestion_panel);
        suggestionContent = view.findViewById(R.id.suggestion_message);
        suggestionButton = view.findViewById(R.id.suggestion_button);

        setSuggestionPanelVisibie(false);
        try {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                    ((ContainerActivity) getActivity()).openOrderView(oAdapter.getOrderList().get(i).getOrderId());
                }
            });
        } catch (NullPointerException e) {
            Log.e("NULL CAUGHT", e.getMessage());
        }

        setSuggestionPanelContent("<big>Optimal Order for delivery:<br><b>AliExpress parcel</b><br><h5>Time period: 14:00 - 17:00</h5></big>");
        setSuggestionReferrer("4");

        return view;
    }

    public void makeOptimalOrderSuggestion() {
        getOptimalOrderIndex();

    }

    private int getOptimalOrderIndex() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        int optimalOrderIndex = -1;
        List<ItemHeaderResponseModel> list = oAdapter.getOrderList();
        for (int i = 0; i < list.size(); ++i) {
            try {
                Date date = sdf.parse(list.get(i).getDeliveryTimeTo());

            } catch (ParseException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        //TODO: FINISH IMPLEMENTATION
        return optimalOrderIndex;
    }



    public void showEmptyListInfo(boolean visibility) {
        if (visibility) {
            emptyListInfo.setVisibility(View.VISIBLE);
        } else {
            emptyListInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        controller.loadOrderList();
    }

    public void updateList(List<ItemHeaderResponseModel> orderList) {
        try {
            if (orderList.isEmpty()) {
                showEmptyListInfo(true);
                listView.setAdapter(null);
            } else {
                showEmptyListInfo(false);
                oAdapter = new AssignedOrderEntryAdapter(getContext(), orderList);
                listView.setAdapter(oAdapter);
            }
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
        if (oAdapter != null && oAdapter.getOrderList() != null) {
            oAdapter.getOrderList().clear();
        }
    }

    @Override
    public void showNotification(String message) {
        ((ContainerActivity) getActivity()).showNotification(message);
    }

    public void setSuggestionPanelVisibie(boolean state) {
        if (state) {
            suggestionPanel.setVisibility(View.VISIBLE);
        } else {
            suggestionPanel.setVisibility(View.GONE);
        }
    }

    public void setSuggestionPanelContent(String content) {
        suggestionContent.setText(Html.fromHtml(content));
    }

    public void setSuggestionReferrer(String orderId) {
        if (orderId != null && !orderId.isEmpty()) {
            suggestionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ContainerActivity) getActivity()).openOrderView(orderId);
                }
            });
        } else {
            suggestionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
