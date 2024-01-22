package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;

import java.util.List;

public interface ItemDAO extends CrudDAO<ItemDAO> {
    @Override
    boolean save(ItemDAO dto);

    @Override
    boolean update(ItemDAO dto);

    @Override
    boolean delete(ItemDAO dto);

    @Override
    ItemDAO search();

    @Override
    List<ItemDAO> getAll();
}
