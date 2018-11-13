package ru.innopolis.deliveryhelper.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import ru.innopolis.deliveryhelper.ContainerMVC;
import ru.innopolis.deliveryhelper.R;
import ru.innopolis.deliveryhelper.controller.ContainerController;


public class ContainerActivity extends AppCompatActivity implements ContainerMVC.View {

    // controller for given view
    private ContainerMVC.Controller controller;

    // UI elements in view
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    // fragment management entity
    private FragmentManager fragmentManager;
    Class previousFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list_activity);

        // assign controlling entities
        controller = new ContainerController(this);
        fragmentManager = getSupportFragmentManager();

        // find indicated elements in UI
        ButterKnife.bind(this);

        // set up menu bar on top of screen
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white);
        }

        // initialize fragment history menu
        setPreviousFragment(OrderListFragment.class);
        setFragment(OrderListFragment.class);

        // set up actions for the menu of left panel on screen
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

    /**
     * Set the class of previous pragment before switching to new one
     * @param fragment class of previous fragment
     */
    private void setPreviousFragment(Class fragment) {
        previousFragment = fragment;
    }

    /**
     * Fragment replacement mechanism in main view
     * @param fragmentClass class of framgment to be set up
     */
    private void setFragment(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    /**
     * Fragment replacement mechanism in main view, also includes the extra fat to be passed to fragment
     * @param fragmentClass class to be set up
     * @param extraKey key of extra data to be set up
     * @param extraValue value of extra data to be set up
     */
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

    /**
     * Make transition to the previous fragment
     */
    public void returnToPreviousFragment(){
        setFragment(previousFragment);
    }

    /**
     * Make transition to fragment with list of available orders
     */
    @Override
    public void openOrderList() {
        setPreviousFragment(AssignedOrderListFragment.class);
        setFragment(OrderListFragment.class);
    }

    /**
     * Make transition to fragment with details view of particular order
     * @param orderId id of given order
     */
    @Override
    public void openOrderView(String orderId) {
        setFragmentWithExtra(OrderViewFragment.class, "ORDER_ID_KEY", orderId);
    }

    /**
     * Make transition to fragment with list of assigned orders
     */
    @Override
    public void openAssignedOrderList() {
        setPreviousFragment(AssignedOrderListFragment.class);
        setFragment(AssignedOrderListFragment.class);
    }

    /**
     * Make transition to fragment with application settings
     */
    @Override
    public void openSettings() {
        setFragment(NotImplementedActivityFragment.class);
    }

    /**
     * Make transition to login activity
     */
    public void returnToLoginActivity(){
        Intent intent = new Intent(ContainerActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    /**
     * Assign actions for items in side menu
     * @param item element in drawer
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Show notification on top of screen for user
     * @param text Message in html format that user should see
     */
    @Override
    public void showNotification(String text) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final TextView message = new TextView(this);
        message.setText(Html.fromHtml(text));
        message.setPadding(50,50,50,0);
        message.setTypeface(null, Typeface.NORMAL);
        message.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        message.setTextColor(Color.BLACK);
        alert.setView(message);
        alert.setPositiveButton("DISMISS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // nothing to do
            }
        });
        alert.show();
    }

    /**
     * Get controller of given view
     * @return controller entity
     */
    public ContainerMVC.Controller getController() {
        return controller;
    }


}
