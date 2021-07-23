package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.model.Candidate;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class HBStore implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private <T> T wrapper(final Function<Session, T> command) {
        Session session = sf.openSession();
        session.beginTransaction();
        try {
            T result = command.apply(session);
            session.getTransaction().commit();
            return result;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Override
    public Candidate add(Candidate candidate) {
        this.wrapper(session -> session.save(candidate));
        return candidate;
    }

    public List<Candidate> getAllCandidates() {
        return this.wrapper(session -> session.createQuery("from Candidate").list());
    }

    public Optional<Candidate> findById(Integer id) {
        Candidate Candidate = (Candidate) this.wrapper(session -> {
            final Query query = session.createQuery("from Candidate where id=:fId");
            query.setParameter("fId", id);
            return query.uniqueResult();
        });
        return Optional.ofNullable(Candidate);
    }

    public Optional<Candidate> findByName(String name) {
        Candidate Candidate = (Candidate) this.wrapper(session -> {
            final Query query = session.createQuery("from Candidate where name=:fName");
            query.setParameter("fName", name);
            return query.uniqueResult();
        });
        return Optional.ofNullable(Candidate);
    }

    public void update(Integer id, String name, Integer experience, Integer salary) {
        this.wrapper(session -> session.createQuery("update Candidate c " +
                "set c.name= :newName, c.experience= : newExperience, " +
                "c.salary= :newSalary where c.id= :fId")
                .setParameter("newName", name)
                .setParameter("newExperience", experience)
                .setParameter("newSalary", salary)
                .setParameter("fId", id)
                .executeUpdate());
    }

    public void delete(Integer id) {
        this.wrapper(session -> session.createQuery("delete from Candidate where id= :fId")
                .setParameter("fId", id)
                .executeUpdate());
    }
}
