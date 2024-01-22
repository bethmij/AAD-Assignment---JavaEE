package lk.ijse.gdse66.backend.dao.custom.impl;

import lk.ijse.gdse66.backend.dao.custom.ItemDAO;
import lk.ijse.gdse66.backend.entity.ItemEntity;

import java.sql.Connection;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {


    @Override
    public boolean save(ItemEntity dto, Connection connection) {
        return false;
    }

    @Override
    public boolean update(ItemEntity dto, Connection connection) {
        return false;
    }

    @Override
    public boolean delete(ItemEntity dto, Connection connection) {
        return false;
    }

    @Override
    public ItemEntity search(String id, Connection connection) {
        return null;
    }

    @Override
    public List<ItemEntity> getAll(Connection connection) {
        return null;
    }
}
