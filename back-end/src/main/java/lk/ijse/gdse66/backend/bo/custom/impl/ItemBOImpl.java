//package lk.ijse.gdse66.backend.bo.custom.impl;
//
//import lk.ijse.gdse66.backend.bo.custom.ItemBO;
//import lk.ijse.gdse66.backend.dao.DAOFactory;
//import lk.ijse.gdse66.backend.dao.custom.ItemDAO;
//import lk.ijse.gdse66.backend.dto.ItemDTO;
//import lk.ijse.gdse66.backend.entity.ItemEntity;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ItemBOImpl implements ItemBO {
//    ItemDAO itemDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEMDAO);
//
//
//    @Override
//    public List<ItemDTO> getAllItems(Connection connection) throws SQLException {
//        List<ItemEntity> itemEntityList = itemDAO.getAll(connection);
//        List<ItemDTO> itemDTOList = new ArrayList<>();
//        ItemDTO itemDTO;
//
//        for (ItemEntity item : itemEntityList) {
//            itemDTO = new ItemDTO(item.getItemCode(), item.getDescription(), item.getUnitPrice(), item.getQtyOnHand());
//            itemDTOList.add(itemDTO);
//        }
//        return itemDTOList;
//    }
//
//    @Override
//    public ItemDTO getItemByCode(Connection connection, String code) throws SQLException {
//        ItemEntity item = itemDAO.search(connection, code);
//        return new ItemDTO(item.getItemCode(), item.getDescription(), item.getUnitPrice(), item.getQtyOnHand());
//    }
//
//    @Override
//    public List<String> getItemIDList(Connection connection) throws SQLException {
//        return itemDAO.getIDList(connection);
//    }
//
//    @Override
//    public boolean saveItem(Connection connection, ItemDTO itemDTO) throws SQLException {
//        ItemEntity itemEntity = new ItemEntity(itemDTO.getItemCode(), itemDTO.getDescription(), itemDTO.getQtyOnHand(),itemDTO.getUnitPrice());
//
//        return itemDAO.save(connection, itemEntity);
//    }
//
//    @Override
//    public boolean updateItem(Connection connection, ItemDTO itemDTO) throws SQLException {
//        ItemEntity itemEntity = new ItemEntity(itemDTO.getItemCode(), itemDTO.getDescription(), itemDTO.getQtyOnHand(),itemDTO.getUnitPrice());
//
//        return itemDAO.update(connection, itemEntity);
//    }
//
//    @Override
//    public boolean deleteItem(Connection connection, String code) throws SQLException {
//        return  itemDAO.delete(connection, code);
//    }
//
//
//}
