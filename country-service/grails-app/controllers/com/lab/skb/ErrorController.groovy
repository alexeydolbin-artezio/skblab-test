package com.lab.skb

import com.lab.skb.exception.BadRequestException

import javax.persistence.EntityNotFoundException
import java.time.LocalDateTime

class ErrorController {

    def exceptionHandling() {
        if (!request.exception) {
            respond new ErrorInfo(type: null, message: null, timestamp: LocalDateTime.now()), status: 404
            log.warn "Resource doesn't exist=${request.getContextPath()}"
            return
        }

        def ex = request.exception.cause
        def errorInfo = new ErrorInfo(type: ex.class.name, message: ex.message, timestamp: LocalDateTime.now())
        if (ex instanceof EntityNotFoundException) {
            log.warn "Entity not found=$errorInfo"
            respond errorInfo, status: 404
        } else if (ex instanceof BadRequestException) {
            log.warn "Bad request=$errorInfo"
            respond errorInfo, status: 400
        } else {
            log.error "Internal server error=$errorInfo"
            respond errorInfo, status: 500
        }
    }
}
