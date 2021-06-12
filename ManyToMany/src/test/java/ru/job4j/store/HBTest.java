package ru.job4j.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.model.Author;
import ru.job4j.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class HBTest {
    private HBStore store;
    private SessionFactory sessionFactory;
    private StandardServiceRegistry registry;
    private Author tolstoy;
    private Author pushkin;
    private Book peaceAndWar;
    private Book evgeniiOnegin;

    private <T> T wrapper(final Function<Session, T> command) {
        Session session = sessionFactory.openSession();
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

    @Before
    public void setUp() throws Exception {
        this.registry = new StandardServiceRegistryBuilder().configure().build();
        this.sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        this.store = new HBStore();
        this.tolstoy = new Author("Tolstoy");
        this.pushkin = new Author("Pushkin");
        this.evgeniiOnegin = new Book("Evgenii Onegin");
        this.peaceAndWar = new Book("Peace and War");
        store.add(evgeniiOnegin);
        store.add(peaceAndWar);
        pushkin.addBook(evgeniiOnegin);
        pushkin.addBook(peaceAndWar);
        tolstoy.addBook(peaceAndWar);
        tolstoy.addBook(evgeniiOnegin);
        store.add(pushkin);
        store.add(tolstoy);
    }

    @After
    public void tearDown() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test
    public void testAdd() {
        store.add(evgeniiOnegin);
        Book savedBook = this.wrapper(session -> session.get(Book.class, this.evgeniiOnegin.getId()));
        Assert.assertEquals(evgeniiOnegin, savedBook);
    }

    @Test
    public void testDel() {
        Assert.assertTrue(store.del(pushkin));
    }
}