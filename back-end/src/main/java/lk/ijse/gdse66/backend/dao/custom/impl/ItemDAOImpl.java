//package lk.ijse.gdse66.backend.dao.custom.impl;
//
//import lk.ijse.gdse66.backend.dao.custom.ItemDAO;
//import lk.ijse.gdse66.backend.entity.ItemEntity;
//import lk.ijse.gdse66.backend.util.CrudUtil;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ItemDAOImpl implements ItemDAO {
//
//
//    @Override
//    public boolean save(Connection connection, ItemEntity item) throws SQLException {
//        String sql = "INSERT INTO item (code, description, qtyOnHand, unitPrice) VALUES (?,?,?,?)";
//        return CrudUtil.execute(sql, connection, item.getItemCode(), item.getDescription(),
//                                item.getQtyOnHand(), item.getUnitPrice());
//
//    }
//
//    @Override
//    public boolean update(Connection connection, ItemEntity item) throws SQLException {
//        String sql = "UPDATE item SET description=?, qtyOnHand=?, unitPrice=? WHERE code=?";
//        return CrudUtil.execute(sql, connection, item.getDescription(),
//                item.getQtyOnHand(), item.getUnitPrice(), item.getItemCode());
//
//    }
//
//    @Override
//    public boolean delete(Connection connection, String id) throws SQLException {
//        String sql = "DELETE FROM item WHERE code=?";
//        return CrudUtil.execute(sql, connection, id);
//    }
//
//    @Override
//    public ItemEntity search(Connection connection, String id) throws SQLException {
//        ItemEntity item = null;
//        String sql = "SELECT * FROM item WHERE code=?";
//        ResultSet resultSet = CrudUtil.execute(sql, connection, id);
//
//        if (resultSet.next()) {
//            String code = resultSet.getString(1);
//            String description = resultSet.getString(2);
//            int qtyOnHand = resultSet.getInt(3);
//            double unitPrice = resultSet.getDouble(4);
//
//            item = new ItemEntity(code, description, qtyOnHand, unitPrice);
//        }
//        return item;
//    }
//
//    @Override
//    public int count(Connection connection) throws SQLException {
//        String sql = "SELECT count(code) FROM item";
//        ResultSet resultSet = CrudUtil.execute(sql, connection);
//        if (resultSet.next()){
//            return resultSet.getInt(1);
//        }
//        return 0;
//    }
//
//    @Override
//    public List<ItemEntity> getAll(Connection connection) throws SQLException {
//        List<ItemEntity> itemList = new ArrayList<>();
//
//        String sql = "SELECT * FROM item";
//        ResultSet resultSet = CrudUtil.execute(sql, connection);
//
//        while (resultSet.next()) {
//            String code = resultSet.getString(1);
//            String description = resultSet.getString(2);
//            int qtyOnHand = resultSet.getInt(3);
//            double unitPrice = resultSet.getDouble(4);
//
//            ItemEntity item = new ItemEntity(code, description, qtyOnHand, unitPrice);
//            itemList.add(item);
//        }
//        return itemList;
//    }
//
//    @Override
//    public List<String> getIDList(Connection connection) throws SQLException {
//        List<String> itemCodeList = new ArrayList<>();
//        String sql = "SELECT code FROM item";
//        ResultSet resultSet = CrudUtil.execute(sql, connection);
//
//        while (resultSet.next()) {
//            String code = resultSet.getString(1);
//            itemCodeList.add(code);
//        }
//        return itemCodeList;
//    }
//}
