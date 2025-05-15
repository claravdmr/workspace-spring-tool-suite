package com.example.demo.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.dto.Chat;
import com.example.demo.dto.Message;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/chats")
@CrossOrigin(origins = "${SECURITY_CORS_ALLOWED_ORIGINS:}")
public class ControllerRestLevel1and2 {

	private static final Logger log = LoggerFactory.getLogger(ControllerRestLevel1and2.class);

	private Long nextId;
	private final List<Chat> chats;
	private Long nextMessageId = 0L;

	public ControllerRestLevel1and2() {
		chats = new ArrayList<>();
		nextId = 1L;

	}

	@GetMapping // photo taken of alternative which was easier
	public ResponseEntity<List<Chat>> getChats(@RequestParam(value = "active", required = false) Boolean active) {
		log.debug("getChats");

		List<Chat> filteredChats = chats.stream().filter(chat -> chat.getActive().booleanValue()).toList();

		if (!filteredChats.isEmpty()) {
			return ResponseEntity.ok(filteredChats);

		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@PostMapping
	public ResponseEntity<Void> postChat(@Valid @RequestBody Chat chat) {
		log.debug("postChat");

		chat.setId(nextId++);
		chat.setActive(Boolean.TRUE);
		chat.setDateCreated(LocalDateTime.now());
		chat.setMessages(new ArrayList<>());

		chats.add(chat);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(chat.getId()).toUri();

		return ResponseEntity.created(uri).build();

	}

	@DeleteMapping
	public ResponseEntity<Void> deleteChats() {
		log.debug("deleteChats");

		chats.stream().forEach(chat -> {
			if (chat.getActive().booleanValue()) {
				chat.setActive(Boolean.FALSE);
			}
		});

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id-chat}")
	public ResponseEntity<Chat> getChat(@Valid @PathVariable("id-chat") Long chatId) {
		log.debug("getChat");

		Optional<Chat> chatFiltrados = chats.stream()
				.filter(chat -> chat.getActive().booleanValue() && chat.getId() == chatId).findFirst();

		if (chatFiltrados.isPresent()) {
			return ResponseEntity.ok(chatFiltrados.get());
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@DeleteMapping("/{id-chat}")
	public ResponseEntity<Void> deleteChat(@Valid @PathVariable("id-chat") Long chatId) {
		log.debug("deleteChat");

		chats.stream().forEach(chat -> {
			if (chat.getActive().booleanValue() && chat.getId() == chatId) {
				chat.setActive(Boolean.FALSE);
			}
		});

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id-chat}/messages")
	public ResponseEntity<List<Message>> getMessages(@Valid @PathVariable("id-chat") Long chatId) {
		log.debug("getMensajes");

		List<Message> chatsFiltrados = chats.stream()
				.filter(chat -> chat.getActive().booleanValue() && chat.getId() == chatId)
				.flatMap(chat -> chat.getMessages().stream()).toList();

		if (!chatsFiltrados.isEmpty()) {
			return ResponseEntity.ok(chatsFiltrados);
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@PostMapping("/{id-chat}/messages")
	public ResponseEntity<Void> postMessage(@Valid @PathVariable("id-chat") Long chatId,
			@Valid @RequestBody Message message) {

		log.debug("postMessage");

		boolean changing = false;

		for (Chat chat : chats) {
			if (chat.getActive().booleanValue() && chat.getId() == chatId) {
				message.setId(nextMessageId++);
				message.setDate(null);
				chat.getMessages().add(message);
				changing = true;
			}
		}

		if (changing) {
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(message.getId())
					.toUri();

			return ResponseEntity.created(uri).build();
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id-chat}/mensajes")
	public ResponseEntity<Void> deleteMensajes(@Valid @PathVariable("id-chat") Long chatId) {
		log.debug("deleteMensajes");

		chats.forEach(chat -> {
			if (chat.getActive().booleanValue() && chat.getId() == chatId) {
				chat.getMessages().clear();
			}
		});

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id-chat}/mensajes/{id-men}")
	public ResponseEntity<Message> getMensaje(@Valid @PathVariable("id-chat") Long chatId,
			@Valid @PathVariable("id-men") Long mensajeId) {
		log.debug("getMensaje");

		Optional<Message> filteredMessage = chats.stream().filter(chat -> chat.getId() == chatId)
				.flatMap(chat -> chat.getMessages().stream()).filter(message -> message.getId() == mensajeId)
				.findFirst();

		if (filteredMessage.isPresent()) {
			return ResponseEntity.ok(filteredMessage.get());
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@DeleteMapping("/{id-chat}/mensajes/{id-men}")
	public ResponseEntity<Void> deleteMensaje(@Valid @PathVariable("id-chat") Long chatId,
			@Valid @PathVariable("id-men") Long mensajeId) {
		log.debug("deleteMensaje");

		for (int indChat = 0; indChat < chats.size(); indChat++) {
			if (chats.get(indChat).getActive().booleanValue() && chats.get(indChat).getId() == chatId) {

				for (int indMen = 0; indMen < chats.get(indChat).getMessages().size(); indMen++) {
					if (chats.get(indChat).getMessages().get(indMen).getId() == mensajeId) {
						chats.get(indChat).getMessages().remove(indMen);
						break;
					}
				}
				break;
			}
		}

		return ResponseEntity.noContent().build();
	}
}
