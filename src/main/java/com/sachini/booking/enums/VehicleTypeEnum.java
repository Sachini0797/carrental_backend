package com.sachini.booking.enums;

public enum VehicleTypeEnum {

    SMALL_TOWN_CAR, SMALL_FAMILY_HATCHBACK, LARGE_FAMILY_SALOON, LARGE_FAMILY_ESTATE, MEDIUM_VAN, TYPE_OTHER, NOTHING;

    public static VehicleTypeEnum fromText(String text) {
        switch (text.toUpperCase()) {
            case "SMALL_TOWN_CAR":
                return SMALL_TOWN_CAR;
            case "SMALL_FAMILY_HATCHBACK":
                return SMALL_FAMILY_HATCHBACK;
            case "LARGE_FAMILY_SALOON":
                return LARGE_FAMILY_SALOON;
            case "LARGE_FAMILY_ESTATE":
                return LARGE_FAMILY_ESTATE;
            case "MEDIUM_VAN":
                return MEDIUM_VAN;
            case "OTHER":
                return TYPE_OTHER;
            case "NOTHING":
                return NOTHING;

            default:
                throw new IllegalArgumentException("Undefined Vehicle Type" + text);
        }
    }
}
