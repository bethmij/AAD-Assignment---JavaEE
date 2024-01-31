package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;
import lk.ijse.gdse66.backend.entity.OrderEntity;

import java.util.List;

public interface OrderDAO extends CrudDAO<OrderEntity> {
    @Override
    boolean save( OrderEntity dto);

    @Override
    boolean update(OrderEntity dto);

    @Override
    boolean delete(String id);

    @Override
    OrderEntity search( String id);

    @Override
    List<OrderEntity> getAll();

    @Override
    List<String> getIDList();
}
