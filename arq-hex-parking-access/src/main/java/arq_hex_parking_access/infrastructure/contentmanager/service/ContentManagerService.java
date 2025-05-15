package arq_hex_parking_access.infrastructure.contentmanager.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import arq_hex_parking_access.application.ports.output.ContentManagerOutputPort;
import arq_hex_parking_access.infrastructure.contentmanager.dto.ContentManagerInputDto;
import arq_hex_parking_access.infrastructure.contentmanager.dto.ContentManagerOutputDto;
import arq_hex_parking_access.infrastructure.contentmanager.dto.ContentManagerResultDto;

@Component
public class ContentManagerService implements ContentManagerOutputPort{
	
	private final RestTemplate restTemplate = new RestTemplate();

	@Override
	public String saveImage(byte[] image) {

		
		ContentManagerInputDto contentManagerInputDto;
		
		//the below is like a postman send
		
		ResponseEntity<ContentManagerOutputDto> result = restTemplate.exchange(
			"urlOfDocument",  //to where you are sending it
			HttpMethod.POST,  // how you are sending it
			new HttpEntity<ContentManagerInputDto>(ContentManagerInputDto.builder().file(image).build()), //what you are sending
			ContentManagerOutputDto.class); //what will be returned 
		//
		
		ContentManagerOutputDto body = result.getBody();
		
		if (body!=null) {
			return body.getId();
		}
		
		return null;
	}

	@Override
	public boolean compareImages(String contentManagerImageId, byte[] exitImage) {
	

		ResponseEntity<ContentManagerResultDto> result = restTemplate.exchange(
				"urlOfDocument/" + contentManagerImageId,  //to where you are sending it
				HttpMethod.POST,  // how you are sending it
				new HttpEntity<ContentManagerInputDto>(ContentManagerInputDto.builder().file(exitImage).build()), //what you are sending
				ContentManagerResultDto.class); //what will be returned 
		//
		
		
		ContentManagerResultDto body = result.getBody();
		
		if (body!=null) {
			return body.isResult();
		}
		
		return false;	
	}

}
