package be.multimedi.repository;

import be.multimedi.model.Property;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class PropertyRepositoryImpl implements PropertyRepository{
    private final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("immoPU");

    @Override
    public void save(Property property) {

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(property);
            em.getTransaction().commit();
        }
    }

    @Override
    public List<Property> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT p FROM Property p", Property.class).getResultList();
        }
    }

    @Override
    public List<Property> findByAddress(String searchTerms) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT p FROM Property p WHERE p.address LIKE :searchTerms", Property.class)
                    .setParameter("searchTerms", "%" + searchTerms + "%")
                    .getResultList();
        }
    }

    public void close() {
        emf.close();
    }

}
