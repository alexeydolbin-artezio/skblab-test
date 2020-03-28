package com.lab.skb

import javax.jms.Message
import javax.jms.MessageListener
import javax.jms.TextMessage

class DictionaryJMSListenerService implements MessageListener {

    def countryService

    DictionaryJMSListenerService(countryService) {
        this.countryService = countryService
    }

    @Override
    void onMessage(Message message) {
        def textMessage = (TextMessage) message
        log.debug "Received countries dictionary message=$textMessage"
        def countriesDictionary = textMessage.getText().split('\n').drop(1).collect {
            def dictionaryLine = it.split(';')
            if (dictionaryLine.length != 2) {
                return null
            }
            return new Country(code: dictionaryLine[0], name: dictionaryLine[1])
        }.findAll { Objects.nonNull(it) }
        countryService.bulkUpdate(countriesDictionary)
    }
}
