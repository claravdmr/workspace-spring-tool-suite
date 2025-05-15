package arq_hex_parking_access.infrastructure.police.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import arq_hex_parking_access.application.ports.output.PoliceOutputPort;

@Component
public class PoliceService implements PoliceOutputPort{
	
	private final RestTemplate restTemplate = new RestTemplate();

	@Override
	public void call() {
		
		restTemplate.exchange(
			"urlPoliceForExample",  //to where you are sending it
			HttpMethod.POST,  // how you are sending it
			HttpEntity.EMPTY, //what you are sending
			String.class); //what will be returned, in this case just whatever comes back even though we do not want anything specific in return.
		//
			
			
		
	}

}
