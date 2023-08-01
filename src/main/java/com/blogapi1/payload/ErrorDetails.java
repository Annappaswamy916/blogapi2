package com.blogapi1.payload;

import java.util.Date;

public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String description;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ErrorDetails(Date timestamp, String message, String description){
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }

}
