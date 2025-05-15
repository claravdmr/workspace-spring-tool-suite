package com.mongo.apirest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongo.apirest.dto.BasketDto;
import com.mongo.database.entity.BasketEntity;
import com.mongo.database.entity.BasketState;
import com.mongo.database.repository.BasketRepository;
import com.mongo.utils.Utilities;

@RestController
@RequestMapping("/baskets")
public class BasketController {
	
	@Autowired
	BasketRepository basketRepository;
	
	@GetMapping
	public ResponseEntity<List<BasketDto>> getBaskets(@RequestParam("state") BasketState state){
		
		//List<BasketEntity> list = basketRepository.findAll();  would give all
		List<BasketEntity> list = basketRepository.findByState(state);
		
		
		if(list.isEmpty()) {
			return ResponseEntity.noContent().build();

		}
		
		return ResponseEntity.ok(list.stream().map(basketEntity -> {
			BasketDto dto = new BasketDto();
			dto.setId(basketEntity.getId());
			dto.setState(basketEntity.getState().name());
			dto.setTotalCost(basketEntity.getTotalCost());
			return dto;
		}).toList());
		
	}
	
	@PostMapping
	public ResponseEntity<Void> postBasket(){
		
		BasketEntity basket = new BasketEntity();
		basket.setState(BasketState.OPEN);
		basket.setTotalCost(0.0);
		BasketEntity save = basketRepository.save(basket);
		
		return ResponseEntity.created(Utilities.createUri(save.getId())).build();
		
	}
	
//	@PostMapping
//	public ResponseEntity<Void> postBasket(@RequestBody BasketDto basketDto){
//		
//		BasketEntity forDB = new BasketEntity();
//		
//		forDB.setId(null);
//		
//		if (basketDto.getState().equalsIgnoreCase("open")) {
//			forDB.setState(BasketState.OPEN);
//		} else if (basketDto.getState().equalsIgnoreCase("closed")){
//			forDB.setState(BasketState.CLOSED);
//		} else {
//			forDB.setState(null);
//		}
//		
//		
//		forDB.setTotalCost(basketDto.getTotalCost());
//		
//		BasketEntity toDB = basketRepository.save(forDB);
//		
//		return ResponseEntity.created(Utilities.createUri(toDB.getId())).build();
//		
//	}
	
	
	//this doesn't delete per se but closes basket.
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBasket(@PathVariable("id") String id){
		
		Optional<BasketEntity> basket = basketRepository.findById(id);
		if (basket.isPresent()) {
			basket.get().setState(BasketState.CLOSED);
			basketRepository.save(basket.get());
		}
		
		return ResponseEntity.noContent().build();
	}
	
	

}
