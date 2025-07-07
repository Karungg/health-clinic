package healthclinic.health_clinic.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import healthclinic.health_clinic.dto.Address;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class AddressAttributeConverter implements AttributeConverter<Address, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Address address) {
        try {
            return objectMapper.writeValueAsString(address);
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert address into json");
            return null;
        }
    }

    @Override
    public Address convertToEntityAttribute(String value) {
        try {
            return objectMapper.readValue(value, Address.class);
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert json into address");
            return null;
        }
    }

}
