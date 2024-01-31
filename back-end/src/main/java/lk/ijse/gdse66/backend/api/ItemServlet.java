package lk.ijse.gdse66.backend.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse66.backend.bo.BOFactory;
import lk.ijse.gdse66.backend.bo.custom.DashboardBO;
import lk.ijse.gdse66.backend.bo.custom.ItemBO;
import lk.ijse.gdse66.backend.dto.ItemDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {
    ItemBO itemBO = BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEMBO);
    DashboardBO dashboardBO = BOFactory.getBoFactory().getBO(BOFactory.BOTypes.DASHBOARDBO);

    List<String> itemCodeList;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String option = req.getParameter("option");

        switch (option) {
            case "GET": {
                getAllItem(resp);
                break;
            }
            case "SEARCH": {
                getItemById(req, resp);
                break;
            }
            case "ID":
                itemCodeList = getItemIDList(resp);
                Jsonb jsonb = JsonbBuilder.create();
                jsonb.toJson(itemCodeList, resp.getWriter());
                break;

            case "COUNT":
                getItemCount(resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ItemDTO item = JsonbBuilder.create().fromJson(req.getReader(),ItemDTO.class);
        String code = item.getItemCode();
        String description = item.getDescription();
        int qtyOnHand = item.getQtyOnHand();
        double unitPrice = item.getUnitPrice();

        if (validation(code, resp, description, qtyOnHand, unitPrice)) return;

        if(itemCodeList.contains(code)){
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item code already saved!");
            return;
        }

        try{

            boolean isSaved = itemBO.saveItem(item);

            if(isSaved){
                sendServerMsg(resp, HttpServletResponse.SC_OK, "Item Saved Successfully!");
            }else {
                sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item Save Failed!");
            }
        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ItemDTO item = JsonbBuilder.create().fromJson(req.getReader(),ItemDTO.class);
        String code = item.getItemCode();
        String description = item.getDescription();
        int qtyOnHand = item.getQtyOnHand();
        double unitPrice = item.getUnitPrice();

        if (validation(code, resp, description, qtyOnHand, unitPrice)) return;

        if(!itemCodeList.contains(code)){
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item code not added!");
            return;
        }

        try{

            boolean isUpdated = itemBO.updateItem(item);

            if(isUpdated)
                sendServerMsg(resp,HttpServletResponse.SC_OK,"Item Updated Successfully!");
            else
                sendServerMsg(resp,HttpServletResponse.SC_BAD_REQUEST,"Item Update Failed!");
        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String itemCode = req.getParameter("itemCode");

        if(!itemCodeList.contains(itemCode)){
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item code not saved!");
            return;
        }

        try{
            boolean isDeleted = itemBO.deleteItem( itemCode);

            if(isDeleted){
                sendServerMsg(resp, HttpServletResponse.SC_OK, "Item Deleted Successfully!");
            }else {
                sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item Delete Failed!");
            }
        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private void getAllItem(HttpServletResponse resp) throws IOException {

        try {
            List<ItemDTO> itemList = itemBO.getAllItems();

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(itemList, resp.getWriter());

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }



    private void getItemById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String itemCode = req.getParameter("itemCode");

        try {
            ItemDTO item = itemBO.getItemByCode( itemCode);

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(item, resp.getWriter());

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private List<String> getItemIDList(HttpServletResponse resp) throws IOException {

        try {
            return itemBO.getItemIDList();

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
        return itemCodeList;
    }

    private void getItemCount(HttpServletResponse resp) throws IOException {
        try{
            int count = dashboardBO.getItemCount();

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(count, resp.getWriter());

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validation(String code, HttpServletResponse resp, String description, int qty, double price) throws IOException {
        if(code.equals("") || !code.matches("^(I00-)[0-9]{3}$")){
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item code is a required field : Pattern I00-000");
            return true;
        } else if (description.length() < 5 || description.length() > 50) {
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item Name is a required field : Min 5, Max 20, Spaces Allowed");
            return true;
        } else if (qty==0 || !String.valueOf(qty).matches("^\\d+$")) {
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item quantity is a required field : only numbers");
            return true;
        } else if (price == 0.0 || !String.valueOf(price).matches("^[0-9]*(\\.[0-9]{0,2})?$")) {
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item Price is a required field : Pattern 100.00 or 100");
            return true;
        }
        return false;
    }

    private void sendServerMsg(HttpServletResponse resp, int status, String msg) throws IOException {
        resp.setStatus(status);
        resp.getWriter().println(msg);
    }
}
