package arq_hex_parking_access.application.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import arq_hex_parking_access.application.ports.input.ParkingServiceInputPort;
import arq_hex_parking_access.application.ports.output.ContentManagerOutputPort;
import arq_hex_parking_access.application.ports.output.ParkingProducerOutputPort;
import arq_hex_parking_access.application.ports.output.ParkingRepositoryOutputPort;
import arq_hex_parking_access.application.ports.output.PoliceOutputPort;
import arq_hex_parking_access.domain.command.CreateEntryCommand;
import arq_hex_parking_access.domain.command.PayTicketCommand;
import arq_hex_parking_access.domain.command.ValidateExitCommand;
import arq_hex_parking_access.domain.model.Entry;
import arq_hex_parking_access.domain.query.GetCarsQuery;
import arq_hex_parking_access.domain.query.GetTicketCostQuery;

@Service
public class ParkingService implements ParkingServiceInputPort{
	
	@Autowired
	ContentManagerOutputPort contentManager;
	
	@Autowired
	ParkingRepositoryOutputPort parkingRepository;
	
	@Autowired
	PoliceOutputPort police;
	
	@Autowired
	ParkingProducerOutputPort parkingProducer;

	@Override
	public String createEntry(CreateEntryCommand command) {
		
		//here there is no logic of saving, if the output port/content manager changes tomorrow, this will not need to change in this class.
		
		String imageId = contentManager.saveImage(command.getImage());
		
		Entry entry = Entry.builder()
		.entryDateTime(LocalDateTime.now())
		.exitDateTime(null)
		.paidDateTime(null)
		.id(null)
		.contentManagerImageId(imageId)
		.registration(command.getRegistation())
		.paid(false)
		.build();
		
		
		return parkingRepository.createEntry(entry); // this will return the entryId for future reference
	}

	@Override
	public List<String> getCars(GetCarsQuery query) {
		
		return parkingRepository.getCars(query);
		
	}

	@Override
	public void payTicket(PayTicketCommand command) throws Exception {
			
		Optional<Entry> entry = parkingRepository.getEntry(command.getEntryId());
		
		if (entry.isEmpty()) {
			throw new Exception("Cannot pay ticket as no ticket found.");
		}
		
		entry.get().setPaidDateTime(LocalDateTime.now());
		entry.get().setPaid(true);
		
		
	}

	@Override
	public Float getTicketCost(GetTicketCostQuery query) throws Exception {
		Optional<Entry> entry = parkingRepository.getEntry(query.getEntryId());
		
		if (entry.isEmpty()) {
			throw new Exception("Cannot calculate ticket cost as no ticket found.");
		}
		
		float feePerMin = 1.56f; // this should be saved elsewhere be it the database, properties, etc.
		
		long entryMilis = Timestamp.valueOf(entry.get().getEntryDateTime()).getTime();
		long nowMilis = Timestamp.valueOf(LocalDateTime.now()).getTime();
		
		long milisInParking = nowMilis - entryMilis;
		
		float finalFee = (milisInParking / 1000 / 60) * feePerMin;
		
		
		return finalFee;
	}

	@Override
	public boolean validateExit(ValidateExitCommand command) throws Exception {
		Optional<Entry> entry = parkingRepository.getEntryByRegistration(command.getRegistration());
		
		if (entry.isEmpty()) {
			throw new Exception("Exit not permitted.");
		}
		
		boolean areSame = contentManager.compareImages(entry.get().getContentManagerImageId(), command.getExitImage());
		
		if(!areSame) {
			police.call();
		} else if (entry.get().isPaid()) {
			entry.get().setExitDateTime(LocalDateTime.now());
			parkingRepository.updateEntry(entry.get());
			parkingProducer.send(entry.get());
			return true;
		
		}
		
		return false;
	}

}
