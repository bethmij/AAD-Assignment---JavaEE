package lk.ijse.gdse66.backend.dao.custom.impl;

import lk.ijse.gdse66.backend.dao.custom.OrderDAO;
import lk.ijse.gdse66.backend.entity.OrderEntity;

import java.sql.Connection;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {


    @Override
    public boolean save(OrderEntity dto, Connection connection) {
        return false;
    }

    @Override
    public boolean update(OrderEntity dto, Connection connection) {
        return false;
    }

    @Override
    public boolean delete(OrderEntity dto, Connection connection) {
        return false;
    }

    @Override
    public OrderEntity search(String id, Connection connection) {
        return null;
    }


    @Override
    public List<OrderEntity> getAll(Connection connection) {
        return null;
    }
}
