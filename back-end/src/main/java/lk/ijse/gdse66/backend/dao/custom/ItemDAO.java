package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;
import lk.ijse.gdse66.backend.entity.ItemEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ItemDAO extends CrudDAO<ItemEntity> {
    @Override
    boolean save(ItemEntity dto);

    @Override
    boolean update(ItemEntity dto);

    @Override
    boolean delete(String id);

    @Override
    ItemEntity search(String id) ;

    @Override
    List<ItemEntity> getAll();

    @Override
    List<String> getIDList();
}
