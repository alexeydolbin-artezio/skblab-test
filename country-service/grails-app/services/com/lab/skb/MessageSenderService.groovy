package com.lab.skb

import org.springframework.beans.factory.annotation.Value
import org.springframework.validation.annotation.Validated

import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.jms.Destination
import javax.validation.constraints.NotNull

@Validated
class MessageSenderService {

    @Value('${country.service.messaging.queue.dictionary-import:country.service.dictionary.import}')
    private String dictionaryImportQueueName

    def jmsContext

    def jmsProducer

    Destination dictionaryImportQueue

    /**
     * Отправляет нотификацию со справочником стран в очередь 'country.service.dictionary.import'.
     *
     * @param dictionary - представление csv справочника стран с сохранением перевода строка.
     */
    void sendCountriesDictionary(@NotNull(message = "Country dictionary cannot be null") String dictionary) {
        def message = jmsContext.createTextMessage(dictionary)
        jmsProducer.send(dictionaryImportQueue, message)
    }

    @PostConstruct
    void createQueue() {
        dictionaryImportQueue = jmsContext.createQueue(dictionaryImportQueueName)
    }

    @PreDestroy
    void closeContext() {
        if (jmsContext) {
            jmsContext.close()
        }
    }
}
