package ru.job4j.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.model.Order;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class OrdersStoreTest {

    private BasicDataSource pool;
    private OrdersStore store;
    private Order order;

    public void SqlExecute(String fileName) throws SQLException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName)))
        ) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @Before
    public void setUp() throws SQLException {
        this.pool = new BasicDataSource();
        this.store = new OrdersStore(pool);
        this.order = Order.of("name1", "description1");
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        SqlExecute("./db/update_001.sql");
        store.save(order);
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        List<Order> all = (List<Order>) store.findAll();
        Assert.assertEquals(all.size(), 1);
        Assert.assertEquals(all.get(0).getDescription(), "description1");
        Assert.assertEquals(all.get(0).getId(), 1);
    }

    @Test
    public void findByNameTest() {
        Order order = store.findByName("name1");
        Assert.assertEquals(order.getName(), "name1");
    }

    @Test
    public void findByIdTest() {
        Order order = store.findById(1);
        Assert.assertEquals(order.getId(), 1);
    }

    public void updateTest() throws SQLException {
        order.setName("afterUpdate");
        store.update(order);
        Order updatedOrder = store.findByName("afterUpdate");
        Assert.assertEquals(updatedOrder.getName(), "afterUpdate");
    }

    @After
    public void tearDown() throws Exception {
        SqlExecute("./db/delete.sql");
    }
}