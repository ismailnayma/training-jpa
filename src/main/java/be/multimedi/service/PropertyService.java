package be.multimedi.service;

import be.multimedi.model.Property;

import java.util.List;
import java.util.Optional;

public interface PropertyService {

    void registerProperty(Property property);

    List<Property> getAllProperties();

    List<Property> searchByAddress(String searchTerms);

    Optional<Property> getById(Long id);

    void deleteProperty(Property property);

    void close(); // to close underlying resources if needed
}
