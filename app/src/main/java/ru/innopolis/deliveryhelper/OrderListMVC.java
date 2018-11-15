package ru.innopolis.deliveryhelper;

import java.util.List;

import ru.innopolis.deliveryhelper.Notifiable;
import ru.innopolis.deliveryhelper.model.dataframes.response.ItemHeaderResponseModel;

public interface OrderListMVC {
    interface View extends Notifiable {
        void updateList(List<ItemHeaderResponseModel> orderList);
        void clearList();
        void hideProgressBar();
        void showProgressBar();
        void hideRefreshing();
    }
    interface Controller {
        void loadOrderList();
    }
}
