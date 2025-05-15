package arq_hex_parking_access.infrastructure;

import java.net.URI;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class Utilities {
	
	// this creates part of the url which will be unique based on the id so for example projects/1.
	public static URI createUri(String id) {
		return ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(id)
				.toUri();
	}

}
