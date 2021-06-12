package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.model.Model;
import ru.job4j.model.Author;

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
    public Model add(Model model) {
        this.wrapper(session -> session.save(model));
        return model;
    }

    @Override
    public Boolean del(Model model) {
        return this.wrapper(session -> {
            Model savedModel = session.get(model.getClass(), model.getId());
            session.remove(savedModel);
            Model deletedModel = session.get(model.getClass(), model.getId());
            return deletedModel == null;
        });
    }
}
