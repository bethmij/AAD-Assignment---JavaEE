package lk.ijse.gdse66.backend.bo.custom;

import lk.ijse.gdse66.backend.bo.SuperBO;
import lk.ijse.gdse66.backend.dto.CustomerDTO;

import java.util.List;

public interface CustomerBO extends SuperBO {

    List<CustomerDTO> getAllCustomer();

    CustomerDTO getCustomerByID();

    List<String> getCustomerIDList();

    void saveCustomer();

    void updateCustomer();

    void deleteCustomer();
}
