package lk.ijse.gdse66.backend.bo.custom.impl;

import lk.ijse.gdse66.backend.bo.custom.CustomerBO;
import lk.ijse.gdse66.backend.dao.DAOFactory;
import lk.ijse.gdse66.backend.dao.custom.CustomerDAO;
import lk.ijse.gdse66.backend.dto.CustomerDTO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerBOImpl implements CustomerBO {
    CustomerDAO customerDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMERDAO);

    @Override
    public List<CustomerDTO> getAllCustomer() {
        List<CustomerEntity> customerEntityList = customerDAO.getAll();
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        CustomerDTO customerDTO;

        for (CustomerEntity customer : customerEntityList) {
            customerDTO = new CustomerDTO(customer.getCusID(), customer.getCusName(), customer.getCusAddress(), customer.getCusSalary());
            customerDTOList.add(customerDTO);
        }
        return customerDTOList;
    }

    @Override
    public CustomerDTO getCustomerByID(String id) {
        CustomerEntity customer = customerDAO.search(id);
        return new CustomerDTO(customer.getCusID(), customer.getCusName(), customer.getCusAddress(), customer.getCusSalary());
    }

    @Override
    public List<String> getCustomerIDList()  {
        return customerDAO.getIDList();
    }

    @Override
    public boolean saveCustomer( CustomerDTO customerDTO) {
        CustomerEntity customerEntity = new CustomerEntity(customerDTO.getCusID(), customerDTO.getCusName(),
                customerDTO.getCusAddress(), customerDTO.getCusSalary());
        return customerDAO.save(customerEntity);
    }

    @Override
    public boolean updateCustomer( CustomerDTO customerDTO) {
        CustomerEntity customerEntity = new CustomerEntity(customerDTO.getCusID(), customerDTO.getCusName(),
                customerDTO.getCusAddress(), customerDTO.getCusSalary());

        return customerDAO.update( customerEntity);
    }

    @Override
    public boolean deleteCustomer(String id) {
        return  customerDAO.delete( id);
    }


}
