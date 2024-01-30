package lk.ijse.gdse66.backend.bo.custom;

import lk.ijse.gdse66.backend.bo.SuperBO;
import lk.ijse.gdse66.backend.dto.CustomerDTO;
import lk.ijse.gdse66.backend.dto.OrderDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface PlaceOrderBO extends SuperBO {

    OrderDTO getOrderByID(Connection connection, String id) throws SQLException;

    List<String> getOrderIDList(Connection connection) throws SQLException;

    boolean saveOrder(Connection connection, OrderDTO orderDTO) throws SQLException;

    boolean updateOrder(Connection connection, OrderDTO orderDTO) throws SQLException;

    boolean deleteOrder(Connection connection, String id) throws SQLException;

}
