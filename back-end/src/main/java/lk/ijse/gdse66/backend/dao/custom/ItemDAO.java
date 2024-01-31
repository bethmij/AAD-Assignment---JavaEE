package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;
import lk.ijse.gdse66.backend.entity.ItemEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ItemDAO extends CrudDAO<ItemEntity> {
    @Override
    boolean save(ItemEntity dto) throws SQLException;

    @Override
    boolean update(ItemEntity dto) throws SQLException;

    @Override
    boolean delete(String id) throws SQLException;

    @Override
    ItemEntity search(String id) throws SQLException;

    @Override
    List<ItemEntity> getAll() throws SQLException;

    @Override
    List<String> getIDList() throws SQLException;
}
