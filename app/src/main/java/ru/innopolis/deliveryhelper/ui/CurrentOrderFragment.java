package ru.innopolis.deliveryhelper.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.innopolis.deliveryhelper.ContainerMVC;
import ru.innopolis.deliveryhelper.R;

public class CurrentOrderFragment extends Fragment {
    private ContainerMVC.Controller controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderlist, container, false);

        controller = ((ContainerActivity)getActivity()).getController();
        return view;
    }
}
