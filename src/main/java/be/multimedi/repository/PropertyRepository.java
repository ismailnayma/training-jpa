package be.multimedi.repository;

import be.multimedi.model.Property;

import java.util.List;

public interface PropertyRepository {
    void save(Property property);
    List<Property> findAll();
    List<Property> findByAddress(String searchTerms);
}
