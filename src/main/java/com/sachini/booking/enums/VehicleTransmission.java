package com.sachini.booking.enums;

public enum VehicleTransmission {

    AUTO, MANUAL;

    public static VehicleTransmission fromText(String text) {
        switch (text.toUpperCase()) {
            case "AUTO":
                return AUTO;
            case "MANUAL":
                return MANUAL;

            default:
                throw new IllegalArgumentException("Unknown" + text);
        }
    }
}
