package be.multimedi.service;

import be.multimedi.model.Property;
import be.multimedi.repository.PropertyRepository;
import be.multimedi.repository.PropertyRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository repository;

    public PropertyServiceImpl() {
        this.repository = new PropertyRepositoryImpl();
    }

    // Optionally a constructor if you want to inject a mock repository for tests
    public PropertyServiceImpl(PropertyRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public void registerProperty(Property property) {
        repository.save(property);
    }

    @Override
    public List<Property> getAllProperties() {
        return repository.findAll();
    }

    @Override
    public List<Property> searchByAddress(String searchTerms) {
        return repository.findByAddress(searchTerms);
    }

    @Override
    public Optional<Property> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteProperty(Property property) {
        repository.delete(property);
    }

    @Override
    public void close() {
        // Only needed because underlying impl has this method
        if (repository instanceof PropertyRepositoryImpl impl) {
            impl.close();
        }
    }
}
