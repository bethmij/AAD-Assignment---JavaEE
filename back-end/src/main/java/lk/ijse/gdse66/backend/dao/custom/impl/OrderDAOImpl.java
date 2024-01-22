package lk.ijse.gdse66.backend.dao.custom.impl;

import lk.ijse.gdse66.backend.dao.custom.OrderDAO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;
import lk.ijse.gdse66.backend.entity.OrderEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {


    @Override
    public boolean save(Connection connection, OrderEntity dto) {
        return false;
    }

    @Override
    public boolean update(Connection connection, OrderEntity dto) {
        return false;
    }

    @Override
    public boolean delete(Connection connection, String id) {
        return false;
    }

    @Override
    public OrderEntity search(Connection connection, String id) {
        return null;
    }

    @Override
    public List<OrderEntity> getAll(Connection connection) {
        return null;
    }

    @Override
    public List<String> getIDList(Connection connection) throws SQLException {
        return null;
    }
}
