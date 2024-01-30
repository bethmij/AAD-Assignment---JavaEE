package lk.ijse.gdse66.backend.bo.custom;

import lk.ijse.gdse66.backend.bo.SuperBO;
import lk.ijse.gdse66.backend.dto.CustomerDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CustomerBO extends SuperBO {

    List<CustomerDTO> getAllCustomer(Connection connection) throws SQLException;

    CustomerDTO getCustomerByID(Connection connection, String id) throws SQLException;

    List<String> getCustomerIDList(Connection connection) throws SQLException;

    boolean saveCustomer(Connection connection, CustomerDTO customerDTO) throws SQLException;

    boolean updateCustomer(Connection connection, CustomerDTO customerDTO) throws SQLException;

    boolean deleteCustomer(Connection connection, String id) throws SQLException;


}
