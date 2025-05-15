package com.exampleKafka.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.exampleKafka.dto.BranchEventDto;
import com.exampleKafka.dto.EventType;
import com.exampleKafka.producer.BranchProducer;

@Service
public class BranchService {
    private static final Logger Log = LoggerFactory.getLogger(BranchService.class);

    @Value("${custom.topic.branch}")
    private String topicBranch;

    @Autowired
    BranchProducer branchProducer;

    public List<String> info = new ArrayList<>();

    public void newCar(String newCarInfo) {
        
    	Log.info("************************************************************ NEW CAR ARRIVED");
       
    	// save in database (in this case the above list)
        info.add(newCarInfo);
    }

    public void saleCar(String saleCarInfo) {
       
    	Log.info("************************************************************ CAR SOLD");
        
    	// send kafka message
        BranchEventDto event = new BranchEventDto();
        event.setId("New ID");
        event.setInfo(saleCarInfo);
        event.setType(EventType.SALE);
        branchProducer.sendMessage(topicBranch, saleCarInfo);
    }
}
