package ru.innopolis.deliveryhelper;

public interface OrderListMVC {
    interface View extends Notifiable {
        void returnToLoginActivity();
    }
    interface Controller {
        void logOut();
    }
}
