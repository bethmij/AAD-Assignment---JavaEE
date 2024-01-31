package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.SuperDAO;
import lk.ijse.gdse66.backend.dto.OrderDTO;
import lk.ijse.gdse66.backend.entity.OrderEntity;

import java.sql.Connection;
import java.sql.SQLException;

public interface QueryDAO extends SuperDAO {

    OrderEntity getOrderDetail(String id) throws SQLException;
}
