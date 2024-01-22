package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;
import lk.ijse.gdse66.backend.entity.ItemEntity;

import java.sql.Connection;
import java.util.List;

public interface ItemDAO extends CrudDAO<ItemEntity> {
    @Override
    boolean save(ItemEntity dto, Connection connection);

    @Override
    boolean update(ItemEntity dto, Connection connection);

    @Override
    boolean delete(ItemEntity dto, Connection connection);

    @Override
    ItemEntity search(String id, Connection connection);

    @Override
    List<ItemEntity> getAll(Connection connection);
}
