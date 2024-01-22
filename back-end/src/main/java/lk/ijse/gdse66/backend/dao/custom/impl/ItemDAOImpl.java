package lk.ijse.gdse66.backend.dao.custom.impl;

import lk.ijse.gdse66.backend.dao.custom.ItemDAO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;
import lk.ijse.gdse66.backend.entity.ItemEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {


    @Override
    public boolean save(Connection connection, ItemEntity dto) {
        return false;
    }

    @Override
    public boolean update(Connection connection, ItemEntity dto) {
        return false;
    }

    @Override
    public boolean delete(Connection connection, String id) {
        return false;
    }

    @Override
    public ItemEntity search(Connection connection, String id) {
        return null;
    }

    @Override
    public List<ItemEntity> getAll(Connection connection) {
        return null;
    }

    @Override
    public List<String> getIDList(Connection connection) throws SQLException {
        return null;
    }
}
