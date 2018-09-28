package ru.innopolis.deliveryhelper;

public interface LoginMVC {
    interface View extends Notifiable {
        void setCredentials();
    }
    interface Controller {
        void check();
    }
}
