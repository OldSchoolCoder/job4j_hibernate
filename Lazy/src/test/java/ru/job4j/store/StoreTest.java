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
import ru.job4j.model.Brand;
import ru.job4j.model.ModelAuto;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class StoreTest {

    private HBStore store;
    private SessionFactory sessionFactory;
    private StandardServiceRegistry registry;
    private ModelAuto modelAuto;
    private Brand brand;
    private List<ModelAuto> models = new ArrayList<>();


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
        this.brand = new Brand("Ford");
        this.modelAuto = new ModelAuto("Mustang");
        this.models.add(modelAuto);
        this.models.add(new ModelAuto("Focus"));
        this.models.add(new ModelAuto("Explorer"));
        this.models.add(new ModelAuto("Transit"));
        this.models.add(new ModelAuto("Fiesta"));
        store.add(brand);
        for (ModelAuto model : models) {
            model.add(brand);
            store.add(model);
        }
        for (ModelAuto model : store.getAllModels()) {
            System.out.println("Model name = " + model);
        }
    }

    @After
    public void tearDown() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test
    public void testAdd() {
        store.add(modelAuto);
        ModelAuto savedAuto = this.wrapper(session -> session.get(ModelAuto.class, this.modelAuto.getId()));
        Assert.assertEquals(modelAuto, savedAuto);
    }
}