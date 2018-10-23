package com.amazon.asksdk.address;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

public class DeviceAddressSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds;
    static {
        supportedApplicationIds = new HashSet<String>();
        supportedApplicationIds.add("amzn1.ask.skill.2b495574-de8f-4ed6-916f-1f0a31dfbdce");
        supportedApplicationIds.add("amzn1.ask.skill.ee287e32-7c99-4ba7-bb03-0053d68b8d50"); 
    }
    public DeviceAddressSpeechletRequestStreamHandler() {
        super(new DeviceAddressSpeechlet(), supportedApplicationIds);
    }
}
