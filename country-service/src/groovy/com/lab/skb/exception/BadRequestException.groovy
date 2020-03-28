package com.lab.skb.exception

class BadRequestException extends RuntimeException {

    BadRequestException() {
    }

    BadRequestException(String message) {
        super(message)
    }
}
