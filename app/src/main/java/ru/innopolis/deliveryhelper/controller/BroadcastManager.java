package ru.innopolis.deliveryhelper.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BroadcastManager extends BroadcastReceiver {

    final String LOG_TAG = "BroadcastManager";

    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive " + intent.getAction());
        context.startService(new Intent(context, LocationService.class));
    }

}
