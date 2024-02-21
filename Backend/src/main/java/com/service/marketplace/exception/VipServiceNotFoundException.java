package com.service.marketplace.exception;

public class VipServiceNotFoundException extends RuntimeException {
    public VipServiceNotFoundException(Integer vipServiceId) {
        super("VipService not found with id: " + vipServiceId);
    }

    public VipServiceNotFoundException() {
        super("VipService not found!");
    }
}
