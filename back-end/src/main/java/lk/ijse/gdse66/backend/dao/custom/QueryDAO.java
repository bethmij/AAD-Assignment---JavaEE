package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.SuperDAO;
import lk.ijse.gdse66.backend.dto.OrderDTO;

import java.sql.Connection;
import java.sql.SQLException;

public interface QueryDAO extends SuperDAO {

    OrderDTO getOrderDetail(String id) throws SQLException;
}
