package lk.ijse.gdse66.backend.bo.custom;

import lk.ijse.gdse66.backend.dto.CustomerDTO;

import java.util.List;

public interface ItemBO {
    List<CustomerDTO> getAllItems();

    CustomerDTO getItemByCode();

    List<String> getItemIDList();

    void saveItem();

    void updateItem();

    void deleteItem();
}
