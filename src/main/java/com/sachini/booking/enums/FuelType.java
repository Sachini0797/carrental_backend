package com.sachini.booking.enums;

public enum FuelType {

    PETROL, DIESEL, HYBRID;

    public static FuelType fromText(String text) {
        switch (text.toUpperCase()) {
            case "DIESEL":
                return DIESEL;
            case "PETROL":
                return PETROL;
            case "HYBRID":
                return HYBRID;
            default:
                throw new IllegalArgumentException("Undefined Fuel Type" + text);
        }
    }
}
