package lk.ijse.gdse66.backend.bo.custom;

import lk.ijse.gdse66.backend.bo.SuperBO;
import lk.ijse.gdse66.backend.dto.OrderDTO;

import java.util.List;

public interface PlaceOrderBO extends SuperBO {

    OrderDTO getOrderByID(String id);

    List<String> getOrderIDList();

    boolean saveOrder(OrderDTO orderDTO);

    boolean updateOrder(OrderDTO orderDTO);

    boolean deleteOrder(String id);


}
