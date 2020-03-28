import grails.converters.JSON

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BootStrap {

    def init = { servletContext ->
        JSON.registerObjectMarshaller(LocalDateTime) {
            return it?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }
    }
    def destroy = {
    }
}
