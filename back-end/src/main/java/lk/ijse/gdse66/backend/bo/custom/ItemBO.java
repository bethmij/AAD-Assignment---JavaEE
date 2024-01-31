package lk.ijse.gdse66.backend.bo.custom;

import lk.ijse.gdse66.backend.bo.SuperBO;
import lk.ijse.gdse66.backend.dto.ItemDTO;

import java.util.List;

public interface ItemBO extends SuperBO {
    List<ItemDTO> getAllItems();

    ItemDTO getItemByCode(String code);

    List<String> getItemIDList();

    boolean saveItem( ItemDTO itemDTO);

    boolean updateItem(ItemDTO itemDTO);

    boolean deleteItem(String code);


}
