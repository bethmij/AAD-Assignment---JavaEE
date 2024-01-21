package lk.ijse.gdse66.backend.api;

import jakarta.json.*;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
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
                getAllCustomer(resp, source);
                break;
            }
            case "SEARCH": {
                getCustomerById(req, resp, source);
                break;
            }
            case "ID":
                getCusIDList(resp, source);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        BasicDataSource source = (BasicDataSource) req.getServletContext().getAttribute("bds");

        CustomerEntity customer = JsonbBuilder.create().fromJson(req.getReader(),CustomerEntity.class);
        String id = customer.getId();
        String name = customer.getName();
        String address = customer.getAddress();
        double salary = customer.getSalary();

        try (Connection connection = source.getConnection()) {
            String sql = "INSERT INTO company.customer (id, name, address, salary) VALUES (?,?,?,?)";
            boolean isSaved = CrudUtil.execute(sql, connection, id, name, address, salary);

            if(isSaved){
                sendServerMsg(resp, HttpServletResponse.SC_OK, "Customer Saved Successfully!");
            }else {
                sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error! Please try again");
            }
        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error! Please try again");
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        BasicDataSource source = (BasicDataSource) req.getServletContext().getAttribute("bds");

        CustomerEntity customer = JsonbBuilder.create().fromJson(req.getReader(),CustomerEntity.class);
        String id = customer.getId();
        String name = customer.getName();
        String address = customer.getAddress();
        double salary = customer.getSalary();

        try (Connection connection = source.getConnection()){
            String sql = "UPDATE customer SET name=?, address=?, salary=? WHERE id=?";
            boolean isUpdared
            PreparedStatement pst = connection.prepareStatement(
                    "");


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


    private void getAllCustomer(HttpServletResponse resp, BasicDataSource source) throws IOException {
        List<CustomerEntity> customerList = new ArrayList<>();
        CustomerEntity customer;

        try (Connection connection = source.getConnection()){
            String sql = "SELECT * FROM customer";
            ResultSet resultSet = CrudUtil.execute(sql, connection);

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
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error! Please try again");
        }
    }



    private void getCustomerById(HttpServletRequest req, HttpServletResponse resp, BasicDataSource source) throws IOException {
        String cusID = req.getParameter("cusID");
        CustomerEntity customer = null;

        try (Connection connection = source.getConnection()){
            String sql = "SELECT * FROM customer WHERE id=?";
            ResultSet resultSet = CrudUtil.execute(sql, connection, cusID);

            if (resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                double salary = resultSet.getDouble(4);

                customer = new CustomerEntity(id, name, address, salary);

            }
            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(customer, resp.getWriter());

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error! Please try again");
        }
    }

    private void getCusIDList(HttpServletResponse resp, BasicDataSource source) throws IOException {
        List<String> cusIdList = new ArrayList<>();

        try (Connection connection = source.getConnection()){
            String sql = "SELECT id FROM customer";
            ResultSet resultSet = CrudUtil.execute(sql, connection);

            while (resultSet.next()) {
                String id = resultSet.getString(1);
                cusIdList.add(id);
            }
            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(cusIdList, resp.getWriter());

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error! Please try again");
        }
    }

    private void sendServerMsg(HttpServletResponse resp, int status, String msg) throws IOException {
        resp.setStatus(status);
        resp.getWriter().println(msg);
    }
}
