package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;

import java.util.List;

public interface CustomerDAO extends CrudDAO<CustomerEntity> {
    @Override
    boolean save(CustomerEntity dto) ;

    @Override
    boolean update(CustomerEntity dto) ;

    @Override
    boolean delete(String id) ;

    @Override
    CustomerEntity search(String id) ;

    @Override
    List<CustomerEntity> getAll() ;

    @Override
    List<String> getIDList() ;
}
