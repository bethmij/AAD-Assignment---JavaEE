package lk.ijse.gdse66.backend.bo.custom;

import lk.ijse.gdse66.backend.bo.SuperBO;
import lk.ijse.gdse66.backend.dto.CustomerDTO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CustomerBO extends SuperBO {

    List<CustomerDTO> getAllCustomer(Connection connection) throws SQLException;

    CustomerDTO getCustomerByID();

    List<String> getCustomerIDList();

    void saveCustomer();

    void updateCustomer();

    void deleteCustomer();
}
