package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;
import lk.ijse.gdse66.backend.entity.OrderDetailsEntity;

import java.sql.Connection;
import java.util.List;

public interface OrderDetailDAO extends CrudDAO<OrderDetailsEntity> {

    @Override
    boolean save(OrderDetailsEntity dto, Connection connection);

    @Override
    boolean update(OrderDetailsEntity dto, Connection connection);

    @Override
    boolean delete(OrderDetailsEntity dto, Connection connection);

    @Override
    OrderDetailsEntity search(String id, Connection connection);

    @Override
    List<OrderDetailsEntity> getAll(Connection connection);
}
