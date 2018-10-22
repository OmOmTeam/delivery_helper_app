package ru.innopolis.deliveryhelper;

import java.util.List;

import ru.innopolis.deliveryhelper.Notifiable;
import ru.innopolis.deliveryhelper.model.dataframes.response.ItemHeaderResponseModel;

public interface OrderListMVC {
    interface View extends Notifiable {
//        void addEntity(String orderId, String title, String weight, String dimensions, String type);
        void updateList(List<ItemHeaderResponseModel> orderList);
        void clearList();
        void hideProgressBar();
        void showProgressBar();
    }
    interface Controller {
        void loadOrderList();
    }
}
