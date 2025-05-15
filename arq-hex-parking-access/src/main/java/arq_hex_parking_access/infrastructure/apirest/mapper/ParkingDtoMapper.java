package arq_hex_parking_access.infrastructure.apirest.mapper;

import org.mapstruct.Mapper;

import arq_hex_parking_access.domain.command.CreateEntryCommand;
import arq_hex_parking_access.domain.command.ValidateExitCommand;
import arq_hex_parking_access.infrastructure.apirest.dto.PostEntryDto;
import arq_hex_parking_access.infrastructure.apirest.dto.ValidateExitDto;

@Mapper(componentModel = "spring")
public interface ParkingDtoMapper {
	
	CreateEntryCommand map (PostEntryDto dto);
	
	ValidateExitCommand map(ValidateExitDto dto);

}
