package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;

import java.util.List;

public interface OrderDAO extends CrudDAO<OrderDAO> {
    @Override
    boolean save(OrderDAO dto);

    @Override
    boolean update(OrderDAO dto);

    @Override
    boolean delete(OrderDAO dto);

    @Override
    OrderDAO search();

    @Override
    List<OrderDAO> getAll();
}
