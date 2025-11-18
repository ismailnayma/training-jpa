package be.multimedi.repository;

import be.multimedi.model.Property;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository {

    void save(Property property);

    List<Property> findAll();

    List<Property> findByAddress(String searchTerms);

    Optional<Property> findById(Long id);

    void delete(Property property);
}
