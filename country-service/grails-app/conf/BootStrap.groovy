import com.lab.skb.Country
import com.lab.skb.marshalling.CountryMarshaller
import grails.converters.JSON

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BootStrap {

    def init = { servletContext ->
        JSON.registerObjectMarshaller(LocalDateTime) {
            return it?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }
        def countryMarshaller = new CountryMarshaller()
        JSON.createNamedConfig("v1", countryMarshaller.nameOnlyMarshallerCls)
        JSON.createNamedConfig("v2", countryMarshaller.nameCodeMarshallerCls)


        // TESTING
        environments {
            test {
                new Country(id: 1, code: "RU", name: "Россия").save()
            }
        }
    }
    def destroy = {
    }
}
