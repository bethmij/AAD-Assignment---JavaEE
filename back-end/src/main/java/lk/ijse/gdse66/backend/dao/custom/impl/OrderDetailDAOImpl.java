package lk.ijse.gdse66.backend.dao.custom.impl;

import lk.ijse.gdse66.backend.dao.custom.OrderDetailDAO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;
import lk.ijse.gdse66.backend.entity.OrderDetailsEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailDAOImpl implements OrderDetailDAO {


    @Override
    public boolean save(Connection connection, OrderDetailsEntity dto) {
        return false;
    }

    @Override
    public boolean update(Connection connection, OrderDetailsEntity dto) {
        return false;
    }

    @Override
    public boolean delete(Connection connection, String id) {
        return false;
    }

    @Override
    public OrderDetailsEntity search(Connection connection, String id) {
        return null;
    }

    @Override
    public List<OrderDetailsEntity> getAll(Connection connection) {
        return null;
    }

    @Override
    public List<String> getIDList(Connection connection) throws SQLException {
        return null;
    }
}
