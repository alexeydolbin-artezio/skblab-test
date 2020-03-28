package com.lab.skb

import org.springframework.jms.listener.DefaultMessageListenerContainer

import javax.jms.ConnectionFactory
import javax.jms.Destination
import javax.jms.MessageListener

class DefaultMessageListenerContainerWrapper extends DefaultMessageListenerContainer {

    DefaultMessageListenerContainerWrapper(ConnectionFactory connectionFactory, Destination destination, MessageListener messageListener) {
        setConnectionFactory(connectionFactory)
        setDestination(destination)
        setupMessageListener(messageListener)
    }
}
