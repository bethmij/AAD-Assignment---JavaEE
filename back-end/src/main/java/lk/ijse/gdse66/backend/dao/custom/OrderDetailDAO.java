package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.SuperDAO;
import lk.ijse.gdse66.backend.entity.OrderDetailsEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OrderDetailDAO extends SuperDAO {

    boolean save(Connection connection, List<OrderDetailsEntity> dto) throws SQLException;

    boolean update(Connection connection, List<OrderDetailsEntity> dto) throws SQLException;


}
