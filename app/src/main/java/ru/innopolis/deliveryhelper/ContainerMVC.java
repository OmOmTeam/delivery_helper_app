package ru.innopolis.deliveryhelper;

public interface ContainerMVC {
    interface View extends Notifiable {
        void returnToLoginActivity();
        void openOrderList();
        void openOrderView(String orderId);
        void openAssignedOrderList();
        void openSettings();
    }
    interface Controller {
        void logOut();
    }
}
