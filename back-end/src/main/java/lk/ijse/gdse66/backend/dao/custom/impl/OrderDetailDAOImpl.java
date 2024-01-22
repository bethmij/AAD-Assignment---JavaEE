package lk.ijse.gdse66.backend.dao.custom.impl;

import lk.ijse.gdse66.backend.dao.custom.OrderDetailDAO;
import lk.ijse.gdse66.backend.entity.OrderDetailsEntity;

import java.sql.Connection;
import java.util.List;

public class OrderDetailDAOImpl implements OrderDetailDAO {


    @Override
    public boolean save(OrderDetailsEntity dto, Connection connection) {
        return false;
    }

    @Override
    public boolean update(OrderDetailsEntity dto, Connection connection) {
        return false;
    }

    @Override
    public boolean delete(OrderDetailsEntity dto, Connection connection) {
        return false;
    }

    @Override
    public OrderDetailsEntity search(String id, Connection connection) {
        return null;
    }

    @Override
    public List<OrderDetailsEntity> getAll(Connection connection) {
        return null;
    }
}
