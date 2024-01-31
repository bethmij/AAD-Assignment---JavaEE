//package lk.ijse.gdse66.backend.api;
//
//import jakarta.json.bind.Jsonb;
//import jakarta.json.bind.JsonbBuilder;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lk.ijse.gdse66.backend.bo.BOFactory;
//import lk.ijse.gdse66.backend.bo.custom.DashboardBO;
//import lk.ijse.gdse66.backend.bo.custom.ItemBO;
//import lk.ijse.gdse66.backend.dto.ItemDTO;
//import lk.ijse.gdse66.backend.entity.ItemEntity;
//
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.sql.DataSource;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//
//@WebServlet(urlPatterns = "/item")
//public class ItemServlet extends HttpServlet {
//    ItemBO itemBO = BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEMBO);
//    DashboardBO dashboardBO = BOFactory.getBoFactory().getBO(BOFactory.BOTypes.DASHBOARDBO);
//    DataSource source;
//    List<String> itemCodeList;
//
//    @Override
//    public void init(){
//        try {
//            InitialContext initCtx = new InitialContext();
//            source = (DataSource)initCtx.lookup("java:comp/env/jdbc/pool");
//            itemCodeList = getItemIDList(null, source);
//        } catch (NamingException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        resp.setContentType("application/json");
//        String option = req.getParameter("option");
//
//        switch (option) {
//            case "GET": {
//                getAllItem(resp, source);
//                break;
//            }
//            case "SEARCH": {
//                getItemById(req, resp, source);
//                break;
//            }
//            case "ID":
//                itemCodeList = getItemIDList(resp, source);
//                Jsonb jsonb = JsonbBuilder.create();
//                jsonb.toJson(itemCodeList, resp.getWriter());
//                break;
//
//            case "COUNT":
//                getItemCount(resp, source);
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//
//        ItemEntity item = JsonbBuilder.create().fromJson(req.getReader(),ItemEntity.class);
//        String code = item.getItemCode();
//        String description = item.getDescription();
//        int qtyOnHand = item.getQtyOnHand();
//        double unitPrice = item.getUnitPrice();
//
//        if (validation(code, resp, description, qtyOnHand, unitPrice)) return;
//
//        if(itemCodeList.contains(code)){
//            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item code already saved!");
//            return;
//        }
//
//        try (Connection connection = source.getConnection()) {
//            ItemDTO itemDTO = new ItemDTO(code, description, unitPrice, qtyOnHand);
//            boolean isSaved = itemBO.saveItem(connection, itemDTO);
//
//            if(isSaved){
//                sendServerMsg(resp, HttpServletResponse.SC_OK, "Item Saved Successfully!");
//            }else {
//                sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item Save Failed!");
//            }
//        } catch (SQLException e) {
//            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
//        }
//
//    }
//
//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//
//        ItemEntity item = JsonbBuilder.create().fromJson(req.getReader(),ItemEntity.class);
//        String code = item.getItemCode();
//        String description = item.getDescription();
//        int qtyOnHand = item.getQtyOnHand();
//        double unitPrice = item.getUnitPrice();
//
//        if (validation(code, resp, description, qtyOnHand, unitPrice)) return;
//
//        if(!itemCodeList.contains(code)){
//            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item code not added!");
//            return;
//        }
//
//        try (Connection connection = source.getConnection()){
//            ItemDTO itemDTO = new ItemDTO(code, description, unitPrice, qtyOnHand);
//            boolean isUpdated = itemBO.updateItem(connection, itemDTO);
//
//            if(isUpdated)
//                sendServerMsg(resp,HttpServletResponse.SC_OK,"Item Updated Successfully!");
//            else
//                sendServerMsg(resp,HttpServletResponse.SC_BAD_REQUEST,"Item Update Failed!");
//        } catch (SQLException e) {
//            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
//        }
//    }
//
//    @Override
//    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        String itemCode = req.getParameter("itemCode");
//
//        if(!itemCodeList.contains(itemCode)){
//            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item code not saved!");
//            return;
//        }
//
//        try (Connection connection = source.getConnection()){
//            boolean isDeleted = itemBO.deleteItem(connection, itemCode);
//
//            if(isDeleted){
//                sendServerMsg(resp, HttpServletResponse.SC_OK, "Item Deleted Successfully!");
//            }else {
//                sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item Delete Failed!");
//            }
//        } catch (SQLException e) {
//            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
//        }
//    }
//
//    private void getAllItem(HttpServletResponse resp, DataSource source) throws IOException {
//
//        try (Connection connection = source.getConnection()){
//            List<ItemDTO> itemList = itemBO.getAllItems(connection);
//
//            Jsonb jsonb = JsonbBuilder.create();
//            jsonb.toJson(itemList, resp.getWriter());
//
//        } catch (SQLException e) {
//            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
//        }
//    }
//
//
//
//    private void getItemById(HttpServletRequest req, HttpServletResponse resp, DataSource source) throws IOException {
//        String itemCode = req.getParameter("itemCode");
//
//        try (Connection connection = source.getConnection()){
//            ItemDTO item = itemBO.getItemByCode(connection, itemCode);
//
//            Jsonb jsonb = JsonbBuilder.create();
//            jsonb.toJson(item, resp.getWriter());
//
//        } catch (SQLException e) {
//            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
//        }
//    }
//
//    private List<String> getItemIDList(HttpServletResponse resp, DataSource source) throws IOException {
//
//        try (Connection connection = source.getConnection()){
//            return itemBO.getItemIDList(connection);
//
//        } catch (SQLException e) {
//            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
//        }
//        return itemCodeList;
//    }
//
//    private void getItemCount(HttpServletResponse resp, DataSource source) throws IOException {
//        try (Connection connection = source.getConnection()){
//            int count = dashboardBO.getItemCount(connection);
//
//            Jsonb jsonb = JsonbBuilder.create();
//            jsonb.toJson(count, resp.getWriter());
//
//        } catch (SQLException e) {
//            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private boolean validation(String code, HttpServletResponse resp, String description, int qty, double price) throws IOException {
//        if(code.equals("") || !code.matches("^(I00-)[0-9]{3}$")){
//            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item code is a required field : Pattern I00-000");
//            return true;
//        } else if (description.length() < 5 || description.length() > 50) {
//            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item Name is a required field : Min 5, Max 20, Spaces Allowed");
//            return true;
//        } else if (qty==0 || !String.valueOf(qty).matches("^\\d+$")) {
//            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item quantity is a required field : only numbers");
//            return true;
//        } else if (price == 0.0 || !String.valueOf(price).matches("^[0-9]*(\\.[0-9]{0,2})?$")) {
//            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item Price is a required field : Pattern 100.00 or 100");
//            return true;
//        }
//        return false;
//    }
//
//    private void sendServerMsg(HttpServletResponse resp, int status, String msg) throws IOException {
//        resp.setStatus(status);
//        resp.getWriter().println(msg);
//    }
//}
