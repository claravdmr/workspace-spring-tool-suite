package arq_hex_parking_access.application.ports.input;

import java.util.List;

import arq_hex_parking_access.domain.command.CreateEntryCommand;
import arq_hex_parking_access.domain.command.PayTicketCommand;
import arq_hex_parking_access.domain.command.ValidateExitCommand;
import arq_hex_parking_access.domain.query.GetCarsQuery;
import arq_hex_parking_access.domain.query.GetTicketCostQuery;

public interface ParkingServiceInputPort {
	
	// since there is so much data in the Entry which we will not send anyway, we could create a command object with just the data we want to send. Command is like dto in the sense that it it just an invented object type.
	public String createEntry(CreateEntryCommand command);
	
	// the below is made into a query, rather than a command as it is returning info.
	// public List<String> getCars(String registration, LocalDateTime entryDateTime);
	// the parameters are the filters so we only want to return the regs of cars which are still in the parking.
	public List<String> getCars(GetCarsQuery query);
	
	public void payTicket(PayTicketCommand command) throws Exception;
	
	public Float getTicketCost(GetTicketCostQuery query) throws Exception;
	
	public boolean validateExit(ValidateExitCommand command) throws Exception;
	
	
	
	
//	Entry createEntry(String registration, byte[] image);
//	
//	List<String> getCars();
	
	

}
