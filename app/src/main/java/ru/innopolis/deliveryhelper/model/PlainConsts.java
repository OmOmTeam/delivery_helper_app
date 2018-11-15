package ru.innopolis.deliveryhelper.model;

public class PlainConsts {
    public static final String BASE_URL = "http://10.240.23.148:5321/";
    public static final String internal_server_error_message = "<b>Internal server error</b>, please contact app provider.";
    public static final String server_connection_error_message = "Server Connection Error";
    public static final String barcode_checker_regexp = "\\d{5}";
    public static final String logout_successful_message = "Logout successful";
    public static final String order_picked_successfully_message = "Order picked successfully";
    public static final String sms_sent_message = "New SMS was sent to recipient";
    public static final String order_marked_delivered_message = "Order is now marked as delivered";
    public static final String order_marked_available_message = "Order is now available for other delivery operators";
    public static final String optimal_order_template = "<big>Optimal Order for delivery:<br><b>%s</b><br><h5>Time period:<br> %s - %s</h5></big>";
    public static final String picked_order_naming = "picked";
    public static final String order_status_code_available = "0";
    public static final String order_status_code_assigned = "1";
    public static final String order_status_code_picked = "2";
    public static final String order_status_code_other = "3";
    public static final String order_type_code_letter = "0";
    public static final String order_type_code_sparcel = "1";
    public static final String order_type_code_bparcel = "2";
    public static final String order_type_code_palette = "3";
}
