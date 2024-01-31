package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.SuperDAO;
import lk.ijse.gdse66.backend.entity.OrderEntity;


public interface QueryDAO extends SuperDAO {

    OrderEntity getOrderDetail(String id) ;
}
