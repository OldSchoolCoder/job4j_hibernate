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
import ru.job4j.model.Candidate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.Assert.*;

public class TestHB {
    private HBStore store;
    private SessionFactory sessionFactory;
    private StandardServiceRegistry registry;
    private Candidate candidate;

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
        this.candidate = new Candidate("Ben", 5, 999);
    }

    @After
    public void tearDown() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test
    public void addTest() {
        store.add(candidate);
        Candidate savedCandidate = this.wrapper(session -> session.get(Candidate.class, this.candidate.getId()));
        Assert.assertEquals(candidate, savedCandidate);
    }

    @Test
    public void findAllTest() {
        List<Candidate> candidateList = store.getAllCandidates();
        Assert.assertEquals(3, candidateList.size());
    }

    @Test
    public void findByIdTest() {
        Optional<Candidate> candidateOptional = store.findById(1);
        Assert.assertTrue(candidateOptional.isPresent());
    }

    @Test
    public void findByNameTest() {
        List<Candidate> candidates = store.findByName("Ben");
        Assert.assertTrue(candidates.get(0).getName().equals("Ben"));
    }

    @Test
    public void updateTest() {
        store.update(7, "Mike", 8, 888);
        Candidate result = store.findById(7).orElse(new Candidate());
        Assert.assertTrue(result.getName().equals("Mike"));
    }

    @Test
    public void deleteTest() {
        store.delete(1);
        Optional<Candidate> candidateOptional = store.findById(1);
        Assert.assertTrue(candidateOptional.isEmpty());
    }

}