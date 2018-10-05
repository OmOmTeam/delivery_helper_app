package ru.innopolis.deliveryhelper.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.innopolis.deliveryhelper.OrderListMVC;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.controller.LoginController;
import ru.innopolis.deliveryhelper.controller.OrderListController;


public class OrderListActivity extends AppCompatActivity implements OrderListMVC.View {

    private OrderListMVC.Controller controller;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list_activity);
        controller = new OrderListController(this);

        ButterKnife.bind(this);
    }

    @Override
    public void showNotification(String message) {
    }

}
