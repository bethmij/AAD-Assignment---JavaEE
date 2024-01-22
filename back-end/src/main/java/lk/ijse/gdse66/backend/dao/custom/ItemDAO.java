package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;
import lk.ijse.gdse66.backend.entity.ItemEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ItemDAO extends CrudDAO<ItemEntity> {
    @Override
    boolean save(Connection connection, ItemEntity dto) throws SQLException;

    @Override
    boolean update(Connection connection, ItemEntity dto) throws SQLException;

    @Override
    boolean delete(Connection connection, String id) throws SQLException;

    @Override
    ItemEntity search(Connection connection, String id) throws SQLException;

    @Override
    List<ItemEntity> getAll(Connection connection) throws SQLException;

    @Override
    List<String> getIDList(Connection connection) throws SQLException;
}
