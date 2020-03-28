package com.lab.skb.marshalling

import com.lab.skb.Country

class CountryMarshaller {

    def nameOnlyMarshallerCls = { jsonCfg ->
        jsonCfg.registerObjectMarshaller(Country) { Country country ->
            return [
                    name: country.name
            ]
        }
    }

    def nameCodeMarshallerCls = { jsonCfg ->
        jsonCfg.registerObjectMarshaller(Country) { Country country ->
            return [
                    code: country.code,
                    name: country.name,
            ]
        }
    }
}
