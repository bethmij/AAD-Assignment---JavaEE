package lk.ijse.gdse66.backend.bo.custom;

import lk.ijse.gdse66.backend.bo.SuperBO;
import lk.ijse.gdse66.backend.dto.CustomerDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CustomerBO extends SuperBO {

    List<CustomerDTO> getAllCustomer() throws SQLException;

    CustomerDTO getCustomerByID( String id) throws SQLException;

    List<String> getCustomerIDList() throws SQLException;

    boolean saveCustomer(CustomerDTO customerDTO) throws SQLException;

    boolean updateCustomer(CustomerDTO customerDTO) throws SQLException;

    boolean deleteCustomer(String id) throws SQLException;


}
