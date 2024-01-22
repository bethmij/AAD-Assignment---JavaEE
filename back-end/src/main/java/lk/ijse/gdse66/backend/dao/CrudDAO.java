package lk.ijse.gdse66.backend.dao;

import java.util.List;

public interface CrudDAO<T> {

    boolean save(T dto);

    boolean update(T dto);

    boolean delete(T dto);

    T search();

    List<T> getAll();


}
