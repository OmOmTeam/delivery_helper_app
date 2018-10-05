package ru.innopolis.deliveryhelper;

public interface LoginMVC {

    interface View extends Notifiable {
        void setCredentials(String login, String password);
        void goToOrderListActivity();
    }

    interface Controller {
        void tryLogin(String login, String password);
    }

}
