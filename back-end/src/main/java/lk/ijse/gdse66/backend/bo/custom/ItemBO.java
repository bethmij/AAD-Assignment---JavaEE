package lk.ijse.gdse66.backend.bo.custom;

import lk.ijse.gdse66.backend.bo.SuperBO;
import lk.ijse.gdse66.backend.dto.ItemDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ItemBO extends SuperBO {
    List<ItemDTO> getAllItems(Connection connection) throws SQLException;

    ItemDTO getItemByCode(Connection connection, String code) throws SQLException;

    List<String> getItemIDList(Connection connection) throws SQLException;

    boolean saveItem(Connection connection, ItemDTO itemDTO) throws SQLException;

    boolean updateItem(Connection connection, ItemDTO itemDTO) throws SQLException;

    boolean deleteItem(Connection connection, String code) throws SQLException;
}
