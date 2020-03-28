package com.lab.skb

import java.time.LocalDateTime

class ErrorInfo {

    String type
    String message
    LocalDateTime timestamp

    @Override
    public String toString() {
        return "ErrorInfo{" +
                "type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
