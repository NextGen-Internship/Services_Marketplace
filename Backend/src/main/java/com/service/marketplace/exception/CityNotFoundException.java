package com.service.marketplace.exception;

public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException (Integer cityId) {
        super("City not found with id: " + cityId);
    }

    public CityNotFoundException () {
        super("City not found!");
    }
}
