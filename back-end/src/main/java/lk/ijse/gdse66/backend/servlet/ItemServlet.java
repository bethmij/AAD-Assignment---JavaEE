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
                    builder.add("cusID", code);
                    builder.add("cusName", description);
                    builder.add("cusAddress", qtyOnHand);
                    builder.add("cusSalary", uPrice);
                    arrayBuilder.add(builder.build());

                }
                sendMsg(resp, arrayBuilder.build(), "Got the Item", 200);
            } catch (SQLException e) {
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, e.getLocalizedMessage(), 400);
            }
        }else if(option.equals("SEARCH")){
            String code = req.getParameter("code");

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            try (Connection connection = source.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM customer WHERE code=?");
                preparedStatement.setString(1,code);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String id = resultSet.getString(1);
                    String name = resultSet.getString(2);
                    String address = resultSet.getString(3);
                    double salary = resultSet.getDouble(4);

                    JsonObjectBuilder builder = Json.createObjectBuilder();
                    builder.add("cusID", id);
                    builder.add("cusName", name);
                    builder.add("cusAddress", address);
                    builder.add("cusSalary", salary);
                    arrayBuilder.add(builder.build());

                }
                sendMsg(resp, arrayBuilder.build(), "Got the Customer", 200);
            } catch (SQLException e) {
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, e.getLocalizedMessage(), 400);
            }
        }else if(option.equals("ID")){
            try (Connection connection = source.getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM customer");
                ResultSet resultSet = preparedStatement.executeQuery();
                JsonArrayBuilder cusIDList = Json.createArrayBuilder();

                while (resultSet.next()){
                    cusIDList.add(resultSet.getString(1));
                }
                sendMsg(resp, cusIDList.build(), "Got the list",200);
            } catch (SQLException e) {
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, e.getLocalizedMessage(),400);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String id = req.getParameter("cusID");
        String name = req.getParameter("cusName");
        String address = req.getParameter("cusAddress");
        String salary = req.getParameter("cusSalary");



        try (Connection connection = source.getConnection()) {
            PreparedStatement pst = connection.prepareStatement(
                    "INSERT INTO company.customer (id, name, address, salary) VALUES (?,?,?,?)");
            pst.setString(1,id);
            pst.setString(2,name);
            pst.setString(3,address);
            pst.setString(4,salary);
            boolean isAdded = pst.executeUpdate() > 0;

            if(isAdded){
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, "Customer Added Successfully", 200);
            }else {
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, "Customer Addition Failed", 400);
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
        String cusID = jsonObject.getString("cusID");
        String cusName = jsonObject.getString("cusName");
        String cusAddress = jsonObject.getString("cusAddress");
        String cusSalary = jsonObject.getString("cusSalary");

        try (Connection connection = source.getConnection()){
            PreparedStatement pst = connection.prepareStatement(
                    "UPDATE customer SET name=?, address=?, salary=? WHERE id=?");
            pst.setString(1,cusName);
            pst.setString(2,cusAddress);
            pst.setString(3,cusSalary);
            pst.setString(4,cusID);

            boolean is_updated = pst.executeUpdate() > 0;
            if(is_updated)
                sendMsg(resp,JsonValue.EMPTY_JSON_ARRAY,"Customer Updated Successfully", 200);
            else
                sendMsg(resp,JsonValue.EMPTY_JSON_ARRAY,"Customer Update Failed", 400);
        } catch (SQLException e) {
            sendMsg(resp,JsonValue.EMPTY_JSON_ARRAY, e.getLocalizedMessage(), 400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String cusID = req.getParameter("cusID");
        try (Connection connection = source.getConnection()){
            PreparedStatement pst = connection.prepareStatement(
                    "DELETE FROM customer WHERE id=?"
            );
            pst.setString(1,cusID);
            boolean isDeleted = pst.executeUpdate() > 0;
            if(isDeleted){
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, "Customer Deleted Successfully", 200);
            }else {
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, "Customer Delete Failed", 400);
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
