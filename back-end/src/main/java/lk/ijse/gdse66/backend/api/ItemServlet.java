package lk.ijse.gdse66.backend.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse66.backend.entity.ItemEntity;
import lk.ijse.gdse66.backend.util.CrudUtil;
import org.apache.commons.dbcp.BasicDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String option = req.getParameter("option");

        BasicDataSource source = (BasicDataSource) req.getServletContext().getAttribute("bds");

        switch (option) {
            case "GET": {
                getAllItem(resp, source);
                break;
            }
            case "SEARCH": {
                getItemById(req, resp, source);
                break;
            }
            case "ID":
                getItemIDList(resp, source);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BasicDataSource source = (BasicDataSource) req.getServletContext().getAttribute("bds");

        ItemEntity item = JsonbBuilder.create().fromJson(req.getReader(),ItemEntity.class);
        String code = item.getItemCode();
        String description = item.getDescription();
        int qtyOnHand = item.getQtyOnHand();
        double unitPrice = item.getUnitPrice();

        try (Connection connection = source.getConnection()) {
            String sql = "INSERT INTO item (code, description, qtyOnHand, unitPrice) VALUES (?,?,?,?)";
            boolean isSaved = CrudUtil.execute(sql, connection, code, description, qtyOnHand, unitPrice);

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
        BasicDataSource source = (BasicDataSource) req.getServletContext().getAttribute("bds");

        ItemEntity item = JsonbBuilder.create().fromJson(req.getReader(),ItemEntity.class);
        String code = item.getItemCode();
        String description = item.getDescription();
        int qtyOnHand = item.getQtyOnHand();
        double unitPrice = item.getUnitPrice();

        try (Connection connection = source.getConnection()){
            String sql = "UPDATE item SET description=?, qtyOnHand=?, unitPrice=? WHERE code=?";
            boolean isUpdated = CrudUtil.execute(sql, connection, description, qtyOnHand, unitPrice, code);

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
        BasicDataSource source = (BasicDataSource) req.getServletContext().getAttribute("bds");

        try (Connection connection = source.getConnection()){
            String sql = "DELETE FROM item WHERE code=?";
            boolean isDeleted = CrudUtil.execute(sql, connection, itemCode);

            if(isDeleted){
                sendServerMsg(resp, HttpServletResponse.SC_OK, "Item Deleted Successfully!");
            }else {
                sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item Delete Failed!");
            }
        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private void getAllItem(HttpServletResponse resp, BasicDataSource source) throws IOException {
        List<ItemEntity> itemList = new ArrayList<>();
        ItemEntity item;

        try (Connection connection = source.getConnection()){
            String sql = "SELECT * FROM item";
            ResultSet resultSet = CrudUtil.execute(sql, connection);

            while (resultSet.next()) {
                String code = resultSet.getString(1);
                String description = resultSet.getString(2);
                int qtyOnHand = resultSet.getInt(3);
                double unitPrice = resultSet.getDouble(4);

                item = new ItemEntity(code, description, qtyOnHand, unitPrice);
                itemList.add(item);
            }
            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(itemList, resp.getWriter());

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }



    private void getItemById(HttpServletRequest req, HttpServletResponse resp, BasicDataSource source) throws IOException {
        String itemCode = req.getParameter("itemCode");
        ItemEntity item = null;

        try (Connection connection = source.getConnection()){
            String sql = "SELECT * FROM item WHERE code=?";
            ResultSet resultSet = CrudUtil.execute(sql, connection, itemCode);

            if (resultSet.next()) {
                String code = resultSet.getString(1);
                String description = resultSet.getString(2);
                int qtyOnHand = resultSet.getInt(3);
                double unitPrice = resultSet.getDouble(4);

                item = new ItemEntity(code, description, qtyOnHand, unitPrice);

            }
            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(item, resp.getWriter());

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private void getItemIDList(HttpServletResponse resp, BasicDataSource source) throws IOException {
        List<String> itemCodeList = new ArrayList<>();

        try (Connection connection = source.getConnection()){
            String sql = "SELECT code FROM item";
            ResultSet resultSet = CrudUtil.execute(sql, connection);

            while (resultSet.next()) {
                String code = resultSet.getString(1);
                itemCodeList.add(code);
            }
            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(itemCodeList, resp.getWriter());

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private void sendServerMsg(HttpServletResponse resp, int status, String msg) throws IOException {
        resp.setStatus(status);
        resp.getWriter().println(msg);
    }
}
