package lk.ijse.gdse66.backend.servlet;

import jakarta.json.*;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet (urlPatterns = "/item")
public class ItemServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource source;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String option = req.getParameter("option");

        if (option.equals("GET")) {
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            try (Connection connection = source.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM item");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String code = resultSet.getString(1);
                    String description = resultSet.getString(2);
                    int qtyOnHand = resultSet.getInt(3);
                    double uPrice = resultSet.getDouble(4);

                    JsonObjectBuilder builder = Json.createObjectBuilder();
                    builder.add("code", code);
                    builder.add("description", description);
                    builder.add("qtyOnHand", qtyOnHand);
                    builder.add("uPrice", uPrice);
                    arrayBuilder.add(builder.build());

                }
                sendMsg(resp, arrayBuilder.build(), "Got the Item", 200);
            } catch (SQLException e) {
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, e.getLocalizedMessage(), 400);
            }
        }else if(option.equals("SEARCH")){
            String itemCode = req.getParameter("code");

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            try (Connection connection = source.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM item WHERE code=?");
                preparedStatement.setString(1,itemCode);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String code = resultSet.getString(1);
                    String description = resultSet.getString(2);
                    int qtyOnHand = resultSet.getInt(3);
                    double uPrice = resultSet.getDouble(4);

                    JsonObjectBuilder builder = Json.createObjectBuilder();
                    builder.add("code", code);
                    builder.add("description", description);
                    builder.add("qtyOnHand", qtyOnHand);
                    builder.add("uPrice", uPrice);
                    arrayBuilder.add(builder.build());

                }
                sendMsg(resp, arrayBuilder.build(), "Got the Item", 200);
            } catch (SQLException e) {
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, e.getLocalizedMessage(), 400);
            }
        }else if(option.equals("ID")){
            try (Connection connection = source.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT code FROM item");
                ResultSet resultSet = preparedStatement.executeQuery();
                JsonArrayBuilder itemCodeList = Json.createArrayBuilder();

                while (resultSet.next()){
                    itemCodeList.add(resultSet.getString(1));
                }
                sendMsg(resp, itemCodeList.build(), "Got the list",200);
            } catch (SQLException e) {
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, e.getLocalizedMessage(),400);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String code = req.getParameter("code");
        String description = req.getParameter("description");
        String qtyOnHand = req.getParameter("qtyOnHand");
        String uPrice = req.getParameter("uPrice");



        try (Connection connection = source.getConnection()) {
            PreparedStatement pst = connection.prepareStatement(
                    "INSERT INTO company.item (code, description, qtyOnHand, unitPrice) VALUES (?,?,?,?)");
            pst.setString(1,code);
            pst.setString(2,description);
            pst.setString(3,qtyOnHand);
            pst.setString(4,uPrice);
            boolean isAdded = pst.executeUpdate() > 0;

            if(isAdded){
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, "Item Added Successfully", 200);
            }else {
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, "item Addition Failed", 400);
            }
        } catch (SQLException e) {
            sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, e.getLocalizedMessage(), 400);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String code = jsonObject.getString("code");
        String description = jsonObject.getString("description");
        String qtyOnHand = jsonObject.getString("qtyOnHand");
        String uPrice = jsonObject.getString("uPrice");

        try (Connection connection = source.getConnection()){
            PreparedStatement pst = connection.prepareStatement(
                    "UPDATE item SET description=?, qtyOnHand=?, unitPrice=? WHERE code=?");
            pst.setString(1,code);
            pst.setString(2,description);
            pst.setString(3,qtyOnHand);
            pst.setString(4,uPrice);

            boolean is_updated = pst.executeUpdate() > 0;
            if(is_updated)
                sendMsg(resp,JsonValue.EMPTY_JSON_ARRAY,"Item Updated Successfully", 200);
            else
                sendMsg(resp,JsonValue.EMPTY_JSON_ARRAY,"Item Update Failed", 400);
        } catch (SQLException e) {
            sendMsg(resp,JsonValue.EMPTY_JSON_ARRAY, e.getLocalizedMessage(), 400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String code = req.getParameter("code");
        try (Connection connection = source.getConnection()){
            PreparedStatement pst = connection.prepareStatement(
                    "DELETE FROM item WHERE code=?"
            );
            pst.setString(1,code);
            boolean isDeleted = pst.executeUpdate() > 0;
            if(isDeleted){
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, "Item Deleted Successfully", 200);
            }else {
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, "Item Delete Failed", 400);
            }
        } catch (SQLException e) {
            sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, e.getLocalizedMessage(), 400);
        }
    }


    public  void sendMsg(HttpServletResponse resp, JsonArray data, String message, int status) throws IOException {
        resp.setStatus(200);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("data",data);
        builder.add("message",message);
        builder.add("status",status);
        resp.getWriter().println(builder.build());
    }
}
