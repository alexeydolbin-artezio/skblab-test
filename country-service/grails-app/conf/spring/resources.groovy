import com.sun.messaging.ConnectionFactory
import com.sun.messaging.jmq.jmsclient.ContainerType
import com.sun.messaging.jmq.jmsclient.JMSContextImpl
import com.sun.messaging.jmq.jmsclient.JMSProducerImpl
import org.springframework.jms.connection.SingleConnectionFactory
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor

// Place your Spring DSL code here
beans = {
    jmsConnectionFactory(ConnectionFactory) {
    }
    jmsSingleConnectionFactory(SingleConnectionFactory, ref("jmsConnectionFactory")){
    }
    jmsContext(JMSContextImpl, ref("jmsSingleConnectionFactory"), ContainerType.JavaSE) {
    }
    jmsProducer(JMSProducerImpl, ref("jmsContext")) {
    }

    methodValidationPostProcessor(MethodValidationPostProcessor) {
    }
}
