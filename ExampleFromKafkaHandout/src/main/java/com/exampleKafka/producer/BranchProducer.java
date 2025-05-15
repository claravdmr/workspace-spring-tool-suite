package com.exampleKafka.producer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
public class BranchProducer {
    private static final Logger Log = LoggerFactory.getLogger(BranchProducer.class);

    //we do not have this in properties but the : means that if it is not found it will be 6000
    @Value("${spring.kafka.timeout:6000}")
    private long kafkaTimeout;

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    
    public void sendMessage(String topicName, Object msg) {
        Log.info("****************************************************************** MESSAGE SENT");
        try {
        	//kafta template is what we use to send the messages
            kafkaTemplate.send(topicName, msg).get(kafkaTimeout, TimeUnit.MILLISECONDS);
            handleSuccess(msg);
        } catch (ExecutionException e) {
            handleKafkaExecutionFailure(msg, e.getCause());
        } catch (TimeoutException e) {
            handleKafkaTimeoutException(msg, e);
        } catch (InterruptedException e) {
            handleKafkaInterruptedException(msg, e);
            Thread.currentThread().interrupt();
        }
    }

	public void sendMessageAsync(String topicName, Object msg) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, msg);
        future.handle((result, ex) -> {
            if (ex != null) {
                if (ex.getCause() instanceof TimeoutException e) {
                	handleKafkaTimeoutException(msg, e);
                } else {
                	handleKafkaExecutionFailure(msg, ex.getCause());
                }
            } else {
                handleSuccess(msg);
            }
            return result;
        });
    }

	public void handleSuccess(Object msg) {
        Log.info("****************************************************************** MESSAGE SENT CORRECTLY");
    }
	
	private void handleKafkaTimeoutException(Object msg, TimeoutException e) {
		 Log.info("****************************************************************** TIMEOUT EXCEPTION");
	}

    public void handleKafkaInterruptedException(Object msg, InterruptedException e) {
        Log.info("****************************************************************** INTERRUPTED EXCEPTION");
    }

    public void handleKafkaExecutionFailure(Object msg, Throwable throwable) {
        Log.info("****************************************************************** EXECUTION EXCEPTION");
    }

}
