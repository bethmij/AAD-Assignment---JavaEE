package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;

import java.util.List;

public interface CustomerDAO extends CrudDAO<CustomerDAO> {

    @Override
    boolean save(CustomerDAO dto);

    @Override
    boolean update(CustomerDAO dto);

    @Override
    boolean delete(CustomerDAO dto);

    @Override
    CustomerDAO search();

    @Override
    List<CustomerDAO> getAll();
}
