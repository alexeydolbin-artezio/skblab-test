import com.lab.skb.DefaultMessageListenerContainerWrapper
import com.lab.skb.DictionaryJMSListenerService
import com.sun.messaging.ConnectionFactory
import org.springframework.jms.connection.SingleConnectionFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor

// Place your Spring DSL code here
beans = {
    jmsConnectionFactory(ConnectionFactory) {
    }
    jmsSingleConnectionFactory(SingleConnectionFactory, ref("jmsConnectionFactory")) {
    }
    jmsTemplate(JmsTemplate, ref("jmsSingleConnectionFactory")) {
    }
    messageListener(DictionaryJMSListenerService, ref("countryService")) {
    }
    dictionaryUpdateQueue(com.sun.messaging.Queue, '${country.service.messaging.queue.dictionary-import:country.service.dictionary.import}') {
    }
    dictionaryUpdateJmsContainer(DefaultMessageListenerContainerWrapper, ref("jmsSingleConnectionFactory"), ref("dictionaryUpdateQueue"), ref("messageListener")) {
    }

    methodValidationPostProcessor(MethodValidationPostProcessor) {
    }
}
