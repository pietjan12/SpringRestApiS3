package com.kiddygambles.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ErrorDetails {
    @JsonProperty
    private Date timestamp;
    @JsonProperty
    private String message;
    @JsonProperty
    private String details;

    public ErrorDetails(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }
}