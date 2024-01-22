package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;
import lk.ijse.gdse66.backend.entity.OrderDetailsEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OrderDetailDAO extends CrudDAO<List<OrderDetailsEntity>> {

    @Override
    boolean save(Connection connection, List<OrderDetailsEntity> dto) throws SQLException;

    @Override
    boolean update(Connection connection, List<OrderDetailsEntity> dto) throws SQLException;

    @Override
    boolean delete(Connection connection, String id);

    @Override
    List<OrderDetailsEntity> search(Connection connection, String id);

    @Override
    List<List<OrderDetailsEntity>> getAll(Connection connection);

    @Override
    List<String> getIDList(Connection connection) throws SQLException;
}
