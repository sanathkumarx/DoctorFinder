package com.amazon.asksdk.address.exceptions;

import com.amazon.asksdk.address.DeviceAddressSpeechlet;

public class UnauthorizedException extends DeviceAddressClientException {

    public UnauthorizedException(String message, Exception e) {
        super(message, e);
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}