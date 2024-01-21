package lk.ijse.gdse66.backend.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse66.backend.dto.ItemDTO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;
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
        resp.setContentType("application/json");
        BasicDataSource source = (BasicDataSource) req.getServletContext().getAttribute("bds");

        ItemEntity item = JsonbBuilder.create().fromJson(req.getReader(),ItemEntity.class);
        String code = item.getCode();
        String description = item.getDescription();
        int qtyOnHand = item.getQtyOnHand();
        double unitPrice = item.getUnit_price();

        try (Connection connection = source.getConnection()) {
            String sql = "INSERT INTO item (code, description, qtyOnHand, unitPrice) VALUES (?,?,?,?)";
            boolean isSaved = CrudUtil.execute(sql, connection, code, description, qtyOnHand, unitPrice);

            if(isSaved){
                sendServerMsg(resp, HttpServletResponse.SC_OK, "Item Saved Successfully!");
            }else {
                sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Item Save Failed!");
            }
        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error! Please try again");
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BasicDataSource source = (BasicDataSource) req.getServletContext().getAttribute("bds");

        CustomerEntity customer = JsonbBuilder.create().fromJson(req.getReader(), ItemEntity.class);
        String id = customer.getId();
        String name = customer.getName();
        String address = customer.getAddress();
        double salary = customer.getSalary();

        try (Connection connection = source.getConnection()){
            String sql = "UPDATE customer SET name=?, address=?, salary=? WHERE id=?";
            boolean isUpdated = CrudUtil.execute(sql, connection, name, address, salary, id);

            if(isUpdated)
                sendServerMsg(resp,HttpServletResponse.SC_OK,"Customer Updated Successfully!");
            else
                sendServerMsg(resp,HttpServletResponse.SC_BAD_REQUEST,"Customer Update Failed!");
        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error! Please try again");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String cusID = req.getParameter("cusID");
        BasicDataSource source = (BasicDataSource) req.getServletContext().getAttribute("bds");

        try (Connection connection = source.getConnection()){
            String sql = "DELETE FROM customer WHERE id=?";
            boolean isDeleted = CrudUtil.execute(sql, connection, cusID);

            if(isDeleted){
                sendServerMsg(resp, HttpServletResponse.SC_OK, "Customer Deleted Successfully!");
            }else {
                sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Customer Delete Failed!");
            }
        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Internal Server Error! Please try again");
        }
    }

    private void getAllItem(HttpServletResponse resp, BasicDataSource source) throws IOException {
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
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error! Please try again");
        }
    }



    private void getItemById(HttpServletRequest req, HttpServletResponse resp, BasicDataSource source) throws IOException {
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
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error! Please try again");
        }
    }

    private void getItemIDList(HttpServletResponse resp, BasicDataSource source) throws IOException {
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
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error! Please try again");
        }
    }

    private void sendServerMsg(HttpServletResponse resp, int status, String msg) throws IOException {
        resp.setStatus(status);
        resp.getWriter().println(msg);
    }
}
