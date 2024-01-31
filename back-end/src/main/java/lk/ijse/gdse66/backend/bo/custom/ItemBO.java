package lk.ijse.gdse66.backend.bo.custom;

import lk.ijse.gdse66.backend.bo.SuperBO;
import lk.ijse.gdse66.backend.dto.ItemDTO;

import java.sql.SQLException;
import java.util.List;

public interface ItemBO extends SuperBO {
    List<ItemDTO> getAllItems() throws SQLException;

    ItemDTO getItemByCode(String code) throws SQLException;

    List<String> getItemIDList() throws SQLException;

    boolean saveItem( ItemDTO itemDTO) throws SQLException;

    boolean updateItem(ItemDTO itemDTO) throws SQLException;

    boolean deleteItem(String code) throws SQLException;


}
