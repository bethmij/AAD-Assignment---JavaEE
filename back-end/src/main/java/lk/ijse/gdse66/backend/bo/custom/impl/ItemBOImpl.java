package lk.ijse.gdse66.backend.bo.custom.impl;

import lk.ijse.gdse66.backend.bo.custom.ItemBO;
import lk.ijse.gdse66.backend.dao.DAOFactory;
import lk.ijse.gdse66.backend.dao.custom.ItemDAO;
import lk.ijse.gdse66.backend.dto.ItemDTO;
import lk.ijse.gdse66.backend.entity.ItemEntity;

import java.util.ArrayList;
import java.util.List;

public class ItemBOImpl implements ItemBO {
    ItemDAO itemDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEMDAO);


    @Override
    public List<ItemDTO> getAllItems(){
        List<ItemEntity> itemEntityList = itemDAO.getAll();
        List<ItemDTO> itemDTOList = new ArrayList<>();
        ItemDTO itemDTO;

        for (ItemEntity item : itemEntityList) {
            itemDTO = new ItemDTO(item.getItemCode(), item.getDescription(), item.getUnitPrice(), item.getQtyOnHand());
            itemDTOList.add(itemDTO);
        }
        return itemDTOList;
    }

    @Override
    public ItemDTO getItemByCode(String code){
        ItemEntity item = itemDAO.search(code);
        return new ItemDTO(item.getItemCode(), item.getDescription(), item.getUnitPrice(), item.getQtyOnHand());
    }

    @Override
    public List<String> getItemIDList(){
        return itemDAO.getIDList();
    }

    @Override
    public boolean saveItem(ItemDTO itemDTO){
        ItemEntity itemEntity = new ItemEntity(itemDTO.getItemCode(), itemDTO.getDescription(), itemDTO.getQtyOnHand(),itemDTO.getUnitPrice());

        return itemDAO.save(itemEntity);
    }

    @Override
    public boolean updateItem(ItemDTO itemDTO){
        ItemEntity itemEntity = new ItemEntity(itemDTO.getItemCode(), itemDTO.getDescription(), itemDTO.getQtyOnHand(),itemDTO.getUnitPrice());

        return itemDAO.update(itemEntity);
    }

    @Override
    public boolean deleteItem(String code){
        return  itemDAO.delete(code);
    }


}
