package ru.innopolis.deliveryhelper;

public interface Notifiable {
    /**
     * informs user about something unpredicted behaviour
     *
     * @param message - string that user should read
     */
    void showNotification(String message);
}
