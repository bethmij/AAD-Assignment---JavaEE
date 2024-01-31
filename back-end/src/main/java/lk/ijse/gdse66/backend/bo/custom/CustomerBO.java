package lk.ijse.gdse66.backend.bo.custom;

import lk.ijse.gdse66.backend.bo.SuperBO;
import lk.ijse.gdse66.backend.dto.CustomerDTO;

import java.sql.SQLException;
import java.util.List;

public interface CustomerBO extends SuperBO {

    List<CustomerDTO> getAllCustomer() ;

    CustomerDTO getCustomerByID( String id) ;

    List<String> getCustomerIDList() ;

    boolean saveCustomer(CustomerDTO customerDTO) ;

    boolean updateCustomer(CustomerDTO customerDTO) ;

    boolean deleteCustomer(String id) ;


}
