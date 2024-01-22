package lk.ijse.gdse66.backend.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse66.backend.bo.BOFactory;
import lk.ijse.gdse66.backend.bo.custom.CustomerBO;
import lk.ijse.gdse66.backend.dto.CustomerDTO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;
import lk.ijse.gdse66.backend.util.CrudUtil;
import org.apache.commons.dbcp.BasicDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    CustomerBO customerBO = BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMERBO);

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
        BasicDataSource source = (BasicDataSource) req.getServletContext().getAttribute("bds");

        CustomerEntity customer = JsonbBuilder.create().fromJson(req.getReader(),CustomerEntity.class);
        String id = customer.getCusID();
        String name = customer.getCusName();
        String address = customer.getCusAddress();
        double salary = customer.getCusSalary();

        try (Connection connection = source.getConnection()) {
            CustomerDTO customerDTO = new CustomerDTO(id, name, address, salary);
            boolean isSaved = customerBO.saveCustomer(connection, customerDTO);
            String sql = "INSERT INTO company.customer (id, name, address, salary) VALUES (?,?,?,?)";
            boolean isSaved = CrudUtil.execute(sql, connection, id, name, address, salary);

            if(isSaved){
                sendServerMsg(resp, HttpServletResponse.SC_OK, "Customer Saved Successfully!");
            }else {
                sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Customer Save Failed!");
            }
        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BasicDataSource source = (BasicDataSource) req.getServletContext().getAttribute("bds");

        CustomerEntity customer = JsonbBuilder.create().fromJson(req.getReader(),CustomerEntity.class);
        String id = customer.getCusID();
        String name = customer.getCusName();
        String address = customer.getCusAddress();
        double salary = customer.getCusSalary();

        try (Connection connection = source.getConnection()){
            String sql = "UPDATE customer SET name=?, address=?, salary=? WHERE id=?";
            boolean isUpdated = CrudUtil.execute(sql, connection, name, address, salary, id);

            if(isUpdated)
                sendServerMsg(resp,HttpServletResponse.SC_OK,"Customer Updated Successfully!");
            else
                sendServerMsg(resp,HttpServletResponse.SC_BAD_REQUEST,"Customer Update Failed!");
        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private void getAllCustomer(HttpServletResponse resp, BasicDataSource source) throws IOException {

        try (Connection connection = source.getConnection()){
            List<CustomerDTO> customerList = customerBO.getAllCustomer(connection);

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(customerList, resp.getWriter());

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
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
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
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
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private void sendServerMsg(HttpServletResponse resp, int status, String msg) throws IOException {
        resp.setStatus(status);
        resp.getWriter().println(msg);
    }
}
