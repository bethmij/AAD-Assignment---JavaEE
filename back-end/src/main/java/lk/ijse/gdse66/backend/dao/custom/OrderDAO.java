package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;
import lk.ijse.gdse66.backend.entity.OrderEntity;

import java.sql.Connection;
import java.util.List;

public interface OrderDAO extends CrudDAO<OrderEntity> {
    @Override
    boolean save(OrderEntity dto, Connection connection);

    @Override
    boolean update(OrderEntity dto, Connection connection);

    @Override
    boolean delete(OrderEntity dto, Connection connection);

    @Override
    OrderEntity search(String id, Connection connection);

    @Override
    List<OrderEntity> getAll(Connection connection);
}
