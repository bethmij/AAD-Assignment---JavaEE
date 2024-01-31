package lk.ijse.gdse66.backend.bo.custom;

import lk.ijse.gdse66.backend.bo.SuperBO;
import lk.ijse.gdse66.backend.dto.OrderDTO;

import java.sql.SQLException;
import java.util.List;

public interface PlaceOrderBO extends SuperBO {

    OrderDTO getOrderByID(String id) throws SQLException;

    List<String> getOrderIDList() throws SQLException;

    boolean saveOrder(OrderDTO orderDTO) throws SQLException;

    boolean updateOrder(OrderDTO orderDTO) throws SQLException;

    boolean deleteOrder(String id) throws SQLException;


}
