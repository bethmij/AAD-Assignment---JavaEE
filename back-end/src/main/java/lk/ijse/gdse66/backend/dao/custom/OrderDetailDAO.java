package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;
import lk.ijse.gdse66.backend.entity.OrderDetailsEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OrderDetailDAO extends CrudDAO<OrderDetailsEntity> {

    @Override
    boolean save(Connection connection, OrderDetailsEntity dto);

    @Override
    boolean update(Connection connection, OrderDetailsEntity dto);

    @Override
    boolean delete(Connection connection, String id);

    @Override
    OrderDetailsEntity search(Connection connection, String id);

    @Override
    List<OrderDetailsEntity> getAll(Connection connection);

    @Override
    List<String> getIDList(Connection connection) throws SQLException;
}
