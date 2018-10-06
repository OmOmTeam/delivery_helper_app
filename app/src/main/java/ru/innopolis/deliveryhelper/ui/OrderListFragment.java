package ru.innopolis.deliveryhelper.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.innopolis.deliveryhelper.R;


public class OrderListFragment extends Fragment {

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

        for(int i = 0;i<100;i++){
            addEntity(String.format("Regular Letter %s",Integer.toString(i)), "Universitetskaya 0, k.0, kv.000","1.2 kg", "12x15x20cm", "2.7km");
        }

        return view;
    }

    private void updateList() {
        oAdapter = new OrderEntryAdapter(getContext(), orderList);
        //oAdapter.setCallback(this);
        listView.setAdapter(oAdapter);
        //setEmptyMessageNotificationVisibility(aAdapter.isEmpty());
    }

    void addEntity(String title, String address, String weight, String dimensions, String distanceFromWarehouse) {
        orderList.add(new OrderEntry(title, address, weight, dimensions, distanceFromWarehouse, R.drawable.ic_letter));
        updateList();
    }

    /**
     * Delete all items from current list in activity
     */
    public void clearList() {
        orderList.clear();
        updateList();
    }
}
