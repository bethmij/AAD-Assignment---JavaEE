package lk.ijse.gdse66.backend.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse66.backend.bo.BOFactory;
import lk.ijse.gdse66.backend.bo.custom.CustomerBO;
import lk.ijse.gdse66.backend.bo.custom.DashboardBO;
import lk.ijse.gdse66.backend.dto.CustomerDTO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    CustomerBO customerBO = BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMERBO);
    DashboardBO dashboardBO = BOFactory.getBoFactory().getBO(BOFactory.BOTypes.DASHBOARDBO);
    DataSource source;
    List<String> cusIDList;

    @Override
    public void init(){
        try {
            InitialContext initCtx = new InitialContext();
            source = (DataSource)initCtx.lookup("java:comp/env/jdbc/pool");
            cusIDList = getCusIDList(null, source);

        } catch (NamingException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String option = req.getParameter("option");

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
                cusIDList = getCusIDList(resp, source);
                Jsonb jsonb = JsonbBuilder.create();
                jsonb.toJson(cusIDList, resp.getWriter());
                break;

            case "COUNT":
                getCustomerCount(resp, source);
        }
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        CustomerEntity customer = JsonbBuilder.create().fromJson(req.getReader(),CustomerEntity.class);
        String id = customer.getCusID();
        String name = customer.getCusName();
        String address = customer.getCusAddress();
        double salary = customer.getCusSalary();

        if (validation(id, resp, name, address, salary)) return;

        if(cusIDList.contains(id)){
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "ID already added!");
            return;
        }

        try (Connection connection = source.getConnection()) {
            CustomerDTO customerDTO = new CustomerDTO(id, name, address, salary);
            boolean isSaved = customerBO.saveCustomer(connection, customerDTO);

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

        CustomerEntity customer = JsonbBuilder.create().fromJson(req.getReader(),CustomerEntity.class);
        String id = customer.getCusID();
        String name = customer.getCusName();
        String address = customer.getCusAddress();
        double salary = customer.getCusSalary();

        if (validation(id, resp, name, address, salary)) return;

        if(!cusIDList.contains(id)){
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "ID not saved!");
            return;
        }

        try (Connection connection = source.getConnection()){
            CustomerDTO customerDTO = new CustomerDTO(id, name, address, salary);
            boolean isUpdated = customerBO.updateCustomer(connection, customerDTO);

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

        if(!cusIDList.contains(cusID)){
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "ID not saved!");
            return;
        }

        try (Connection connection = source.getConnection()){
            boolean isDeleted = customerBO.deleteCustomer(connection, cusID);

            if(isDeleted){
                sendServerMsg(resp, HttpServletResponse.SC_OK, "Customer Deleted Successfully!");
            }else {
                sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Customer Delete Failed!");
            }
        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private void getAllCustomer(HttpServletResponse resp, DataSource source) throws IOException {

        try (Connection connection = source.getConnection()){
            List<CustomerDTO> customerList = customerBO.getAllCustomer(connection);

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(customerList, resp.getWriter());

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private void getCustomerById(HttpServletRequest req, HttpServletResponse resp, DataSource source) throws IOException {
        String cusID = req.getParameter("cusID");
        CustomerDTO customer;

        try (Connection connection = source.getConnection()){

            customer = customerBO.getCustomerByID(connection, cusID);

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(customer, resp.getWriter());

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private List<String> getCusIDList(HttpServletResponse resp, DataSource source) throws IOException {
        List<String> customerIDList;

        try (Connection connection = source.getConnection()){
            customerIDList = customerBO.getCustomerIDList(connection);
            return customerIDList;

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
        return cusIDList;
    }

    private void getCustomerCount(HttpServletResponse resp, DataSource source) throws IOException {
        try (Connection connection = source.getConnection()){
            int count = dashboardBO.getCustomerCount(connection);

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(count, resp.getWriter());

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validation(String id, HttpServletResponse resp, String name, String address, double salary) throws IOException {
        if(id.equals("") || !id.matches("^(C00-)[0-9]{3}$")){
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Cus ID is a required field : Pattern C00-000");
            return true;
        } else if (name.equals("") || !name.matches("^[A-Za-z\\s]*$")) {
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Cus Name is a required field : Min 5, Max 50, Spaces Allowed");
            return true;
        } else if (address.length() < 7) {
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Cus Address is a required field : Minimum 7");
            return true;
        } else if (salary == 0.0 || !String.valueOf(salary).matches("^\\d{1,10}(?:\\.\\d{1,2})?$")) {
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Cus Salary is a required field : Pattern 100.00 or 100");
            return true;
        }
        return false;
    }

    private void sendServerMsg(HttpServletResponse resp, int status, String msg) throws IOException {
        resp.setStatus(status);
        resp.getWriter().println(msg);
    }
}
