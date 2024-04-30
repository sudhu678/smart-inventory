package com.one.smartinventory.model;

public enum Image {
    Chocolate("Kirkland_Chocolate_14.jpg"),

    ManukaHoney("ManukaHoney_63.jpg"),

    SalmonFillets("Salmon_Fillets_29.jpg"),

    TidePods("TidePods_48.jpg"),

    MagnaTiles("MagnaTiles_101.jpg"),

    Customer("Customer.jpg");

    public final String label;

    Image(String label) {
        this.label = label;
    }
}
