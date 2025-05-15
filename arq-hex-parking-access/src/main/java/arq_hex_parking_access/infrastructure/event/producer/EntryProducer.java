package arq_hex_parking_access.infrastructure.event.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EntryProducer { //although this is the class name it just sends an object (line 11) so could be called more generally and used for other classes/objects.
	
	@Autowired
	KafkaTemplate<String,Object> kafkaTemplate; // we can autowire this as it is a bean in the kafkaconfiguration
	
	public void send(Object message) {
		kafkaTemplate.send("kafkaTopic",message); //this is the most basic way of sending message, see other projects for more detail.
	}

}
