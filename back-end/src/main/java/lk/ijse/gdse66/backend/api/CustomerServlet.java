package lk.ijse.gdse66.backend.api;

import jakarta.json.*;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse66.backend.entity.CustomerEntity;
import lk.ijse.gdse66.backend.util.CrudUtil;
import org.apache.commons.dbcp.BasicDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource source;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String option = req.getParameter("option");

        BasicDataSource source = (BasicDataSource) req.getServletContext().getAttribute("bds");



        switch (option) {
            case "GET": {
                List<CustomerEntity> customerList = new ArrayList<>();
                CustomerEntity customer;

                    String sql = "SELECT * FROM customer";
                    ResultSet resultSet = CrudUtil.execute(sql, source,resp);

                    try {
                        while (resultSet.next()) {
                        String id = resultSet.getString(1);
                        String name = resultSet.getString(2);
                        String address = resultSet.getString(3);
                        double salary = resultSet.getDouble(4);

                        customer = new CustomerEntity(id, name, address, salary);
                        customerList.add(customer);
                    }
                        Jsonb jsonb = JsonbBuilder.create();
                        jsonb.toJson(customerList, resp.getWriter());

                    } catch (SQLException e) {
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        resp.getWriter().println("Server Error! Please Try Again");
                    }
                    break;
            }
            case "SEARCH": {
                String cusID = req.getParameter("cusID");

                List<CustomerEntity> customerList = new ArrayList<>();
                CustomerEntity customer;

                String sql = "SELECT * FROM customer";
                ResultSet resultSet = CrudUtil.execute(sql, source,resp);

                try {
                    while (resultSet.next()) {
                        String id = resultSet.getString(1);
                        String name = resultSet.getString(2);
                        String address = resultSet.getString(3);
                        double salary = resultSet.getDouble(4);

                        customer = new CustomerEntity(id, name, address, salary);
                        customerList.add(customer);
                    }
                    Jsonb jsonb = JsonbBuilder.create();
                    jsonb.toJson(customerList, resp.getWriter());

                } catch (SQLException e) {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().println("Server Error! Please Try Again");
                }
                break;
            }
            case "ID":
                try (Connection connection = source.getConnection()) {
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM customer");
                    ResultSet resultSet = preparedStatement.executeQuery();
                    JsonArrayBuilder cusIDList = Json.createArrayBuilder();

                    while (resultSet.next()) {
                        cusIDList.add(resultSet.getString(1));
                    }
                    sendMsg(resp, cusIDList.build(), "Got the list", 200);
                } catch (SQLException e) {
                    sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, e.getLocalizedMessage(), 400);
                }
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
