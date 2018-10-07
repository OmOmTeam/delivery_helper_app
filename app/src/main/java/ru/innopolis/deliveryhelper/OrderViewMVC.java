package ru.innopolis.deliveryhelper;

public interface OrderViewMVC {
    interface View extends Notifiable {
        void addDetailEntity(String key, String value);
        void hideProgressBar();
    }
    interface Controller {
        void loadDetailList(String orderId);
    }
}
