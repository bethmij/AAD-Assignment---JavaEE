package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;
import lk.ijse.gdse66.backend.entity.OrderEntity;

import java.sql.Connection;
import java.util.List;

public interface OrderDAO extends CrudDAO<OrderEntity> {
    @Override
    boolean save(Connection connection, OrderEntity dto);

    @Override
    boolean update(Connection connection, OrderEntity dto);

    @Override
    boolean delete(Connection connection, OrderEntity dto);

    @Override
    OrderEntity search(Connection connection, String id);

    @Override
    List<OrderEntity> getAll(Connection connection);
}
