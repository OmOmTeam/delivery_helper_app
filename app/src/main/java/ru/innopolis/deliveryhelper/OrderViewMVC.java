package ru.innopolis.deliveryhelper;

import com.google.android.gms.maps.model.LatLng;

public interface OrderViewMVC {
    interface View extends Notifiable {
        void addDetailEntity(String key, String value);
        void showProgressBar(boolean visibility);
        void setActionState(int state);
        void setAssignedPanelState(int state);
        void setCustomer(String name, String phone);
        void loadMap(String start, String end);
        void resetLoad();
    }
    interface Controller {
        void loadDetailList(String orderId);
        void acceptOrder(String orderId);
        void pickOrder(String orderId, String key);
        void validateRecipient(String orderId);
        void deliverOrder(String orderId, String key);
        void cancelOrder(String orderId);
    }
}
