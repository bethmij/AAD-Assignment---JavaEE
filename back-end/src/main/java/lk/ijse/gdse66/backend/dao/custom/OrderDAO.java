package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;
import lk.ijse.gdse66.backend.entity.OrderEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OrderDAO extends CrudDAO<OrderEntity> {
    @Override
    boolean save( OrderEntity dto) throws SQLException;

    @Override
    boolean update(OrderEntity dto) throws SQLException;

    @Override
    boolean delete(String id) throws SQLException;

    @Override
    OrderEntity search( String id);

    @Override
    List<OrderEntity> getAll();

    @Override
    List<String> getIDList() throws SQLException;
}
