package ru.innopolis.deliveryhelper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.innopolis.deliveryhelper.Notifiable;
import ru.innopolis.deliveryhelper.OrderListMVC;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.controller.LoginController;
import ru.innopolis.deliveryhelper.controller.OrderListController;


public class OrderListActivity extends AppCompatActivity implements OrderListMVC.View {

    private OrderListMVC.Controller controller;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;


    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list_activity);

        controller = new OrderListController(this);
        fragmentManager = getSupportFragmentManager();

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white);
        }

        Class initialFragementClass = OrderListFragment.class;
        Fragment initialFragment = null;
        try {
            initialFragment = (Fragment) initialFragementClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        setTitle("Available Orders");
        fragmentManager.beginTransaction().replace(R.id.fragment_container, initialFragment).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                boolean isExitInvoked = false;
                Fragment fragment = null;
                Class fragmentClass = null;
                switch(menuItem.getItemId()) {
                    case R.id.nav_logout:
                        isExitInvoked = true;
                        break;
                    case R.id.nav_all:
                        fragmentClass = OrderListFragment.class;
                        break;
                    case R.id.nav_assigned:
                        fragmentClass = NotImplementedActivityFragment.class;
                        break;
                    case R.id.nav_settings:
                        fragmentClass = NotImplementedActivityFragment.class;
                        break;
                    default:
                        fragmentClass = NotImplementedActivityFragment.class;
                }

                if(!isExitInvoked) {
                    try {
                        fragment = (Fragment) fragmentClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Insert the fragment by replacing any existing fragment
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();

                    menuItem.setChecked(true);
                    setTitle(menuItem.getTitle());
                    mDrawerLayout.closeDrawers();
                }else{
                    controller.logOut();
                }
                return true;
            }
        });

    }

    public void returnToLoginActivity(){
        Intent intent = new Intent(OrderListActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showNotification(String message) {
    }

}
