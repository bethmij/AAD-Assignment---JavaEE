package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;

import java.util.List;

public interface OrderDetailDAO extends CrudDAO<OrderDetailDAO> {

    @Override
    boolean save(OrderDetailDAO dto);

    @Override
    boolean update(OrderDetailDAO dto);

    @Override
    boolean delete(OrderDetailDAO dto);

    @Override
    OrderDetailDAO search();

    @Override
    List<OrderDetailDAO> getAll();
}
