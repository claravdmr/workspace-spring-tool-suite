package com.class4.resttemplate;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Qualifier("RestTemplate")
@Service
public class DataServicesImpl implements DataServicesInterface {

	private static final Logger log = LoggerFactory.getLogger(DataServicesImpl.class);

//	private static final String HOST = "https://httpbin.org"; ---- we no longer need this since the @Value below.
	private static final String URL_GET = "/get";
	private static final String URL_POST = "/post";
	private static final String URL_PUT = "/put";
	private static final String URL_DEL = "/delete";

	@Value("${host.httpbin}") // this then needs to go in application.properties
	private String host;

	private final RestTemplate restTemplate;

	public DataServicesImpl() {
		restTemplate = new RestTemplate();
	}

	@Override
	public String postDato(Dato dato) {
		log.info("Init postDatos");

		ResponseEntity<String> salida = restTemplate.exchange(host + URL_POST, HttpMethod.POST,
				new HttpEntity<Dato>(dato), String.class);

		ResponseEntity<String> salida2 = restTemplate.postForEntity(host + URL_POST, new HttpEntity<Dato>(dato),
				String.class);

		String salida3 = restTemplate.postForObject(host + URL_POST, new HttpEntity<Dato>(dato), String.class);

		URI salida4 = restTemplate.postForLocation(host + URL_POST, new HttpEntity<Dato>(dato), String.class);

		if (salida.getStatusCode().is2xxSuccessful()) {
			log.debug("Response OK");
			log.debug("Body: {}", salida.getBody());
			return salida.getBody();
		}
		return null;

	}

	@Override
	public String getDato() {
		log.info("Init getDatos");

		ResponseEntity<String> salida = restTemplate.exchange(host + URL_GET, HttpMethod.GET, null, String.class);

		ResponseEntity<String> salida2 = restTemplate.getForEntity(host + URL_GET, String.class);

		String salida3 = restTemplate.getForObject(host + URL_GET, String.class);

		if (salida.getStatusCode().is2xxSuccessful()) {
			log.info("Response OK");
			log.info("Body: {}", salida.getBody());
			return salida.getBody();
		}
		return null;
	}

	@Override
	public String putDato(Dato dato) {
		log.info("Init putDatos");

		ResponseEntity<String> salida = restTemplate.exchange(host + URL_PUT, HttpMethod.PUT,
				new HttpEntity<Dato>(dato), String.class);

		restTemplate.put(host + URL_PUT, new HttpEntity<Dato>(dato));

		if (salida.getStatusCode().is2xxSuccessful()) {
			log.debug("Response OK");
			log.debug("Body: {}", salida.getBody());
			return salida.getBody();
		}
		return null;
	}

	@Override
	public String deleteDato() {
		log.info("Init deleteDatos");

		ResponseEntity<String> salida = restTemplate.exchange(host + URL_DEL, HttpMethod.DELETE, null, String.class);

		restTemplate.delete(host + URL_DEL);

		if (salida.getStatusCode().is2xxSuccessful()) {
			log.debug("Response OK");
			log.debug("Body: {}", salida.getBody());
			return salida.getBody();
		}
		return null;
	}

}
