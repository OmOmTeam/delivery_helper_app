package ru.innopolis.deliveryhelper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;


import butterknife.BindView;
import butterknife.ButterKnife;
import ru.innopolis.deliveryhelper.ContainerMVC;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.controller.ContainerController;


public class ContainerActivity extends AppCompatActivity implements ContainerMVC.View {

    private ContainerMVC.Controller controller;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    Class previousFragment;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list_activity);

        controller = new ContainerController(this);
        fragmentManager = getSupportFragmentManager();

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white);
        }

        previousFragment = OrderListFragment.class;
        setFragment(OrderListFragment.class);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.nav_logout:
                        controller.logOut();
                        break;
                    case R.id.nav_all:
                        openOrderList();
                        break;
                    case R.id.nav_assigned:
                        openAssignedOrderList();
                        break;
                    case R.id.nav_settings:
                        openSettings();
                        break;
                    default:
                        openOrderList();
                }
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void setPreviousFragment() {
        try {
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
            if(currentFragment instanceof OrderListFragment){
                previousFragment = OrderListFragment.class;
            }else if(currentFragment instanceof AssignedOrderListFragment){
                previousFragment = AssignedOrderListFragment.class;
            }else if(currentFragment instanceof OrderViewFragment){
                previousFragment = OrderViewFragment.class;
            }else if(currentFragment instanceof NotImplementedActivityFragment){
                previousFragment = NotImplementedActivityFragment.class;
            }
        }catch(NullPointerException e){
            previousFragment = OrderListFragment.class;
        }
    }

    private void setFragment(Class fragmentClass) {
        Fragment fragment = null;
        setPreviousFragment();
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private void setFragmentWithExtra(Class fragmentClass, String extraKey, String extraValue) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putString(extraKey, extraValue);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public void returnToPreviousFragment(){
        setFragment(previousFragment);
    }

    @Override
    public void openOrderList() {
        setFragment(OrderListFragment.class);
    }

    @Override
    public void openOrderView(String orderId) {
        setFragmentWithExtra(OrderViewFragment.class, "ORDER_ID_KEY", orderId);
    }

    @Override
    public void openAssignedOrderList() {
        setFragment(AssignedOrderListFragment.class);
    }

    @Override
    public void openCurrentOrder() {
        setFragment(NotImplementedActivityFragment.class);
    }

    @Override
    public void openSettings() {
        setFragment(NotImplementedActivityFragment.class);
    }

    public void returnToLoginActivity(){
        Intent intent = new Intent(ContainerActivity.this, LoginActivity.class);
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
        View parentLayout = findViewById(android.R.id.content);
        Snackbar mySnackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG);
        mySnackbar.show();
    }

    public ContainerMVC.Controller getController() {
        return controller;
    }


}
