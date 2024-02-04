package lk.ijse.gdse66.backend.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse66.backend.bo.BOFactory;
import lk.ijse.gdse66.backend.bo.custom.CustomerBO;
import lk.ijse.gdse66.backend.bo.custom.DashboardBO;
import lk.ijse.gdse66.backend.dto.CustomerDTO;

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
    List<String> cusIDList;



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        String option = req.getParameter("option");

        switch (option) {
            case "GET": {
                getAllCustomer(resp);
                break;
            }
            case "SEARCH": {
                getCustomerById(req, resp);
                break;
            }
            case "ID":
                cusIDList = getCusIDList();
                Jsonb jsonb = JsonbBuilder.create();
                try {
                    jsonb.toJson(cusIDList, resp.getWriter());
                } catch (IOException e) {
                    sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
                }
                break;

            case "COUNT":
                getCustomerCount(resp);
        }
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        CustomerDTO customer = JsonbBuilder.create().fromJson(req.getReader(),CustomerDTO.class);
        String id = customer.getCusID();
        String name = customer.getCusName();
        String address = customer.getCusAddress();
        double salary = customer.getCusSalary();

        if (validation(id, resp, name, address, salary)) return;

        if(cusIDList.contains(id)){
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "ID already added!");
            return;
        }

        boolean isSaved = customerBO.saveCustomer(customer);

        if(isSaved){
            sendServerMsg(resp, HttpServletResponse.SC_OK, "Customer Saved Successfully!");
        }else {
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Customer Save Failed!");
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)  {

        CustomerDTO customer;
        try {
            customer = JsonbBuilder.create().fromJson(req.getReader(), CustomerDTO.class);
            String id = customer.getCusID();
            String name = customer.getCusName();
            String address = customer.getCusAddress();
            double salary = customer.getCusSalary();

            if (validation(id, resp, name, address, salary)) return;

            if(!cusIDList.contains(id)){
                sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "ID not saved!");
                return;
            }

            boolean isUpdated = customerBO.updateCustomer( customer);

            if(isUpdated)
                sendServerMsg(resp,HttpServletResponse.SC_OK,"Customer Updated Successfully!");
            else
                sendServerMsg(resp,HttpServletResponse.SC_BAD_REQUEST,"Customer Update Failed!");
        } catch (IOException ex) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String cusID = req.getParameter("cusID");

        if(!cusIDList.contains(cusID)){
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "ID not saved!");
            return;
        }

        boolean isDeleted = customerBO.deleteCustomer( cusID);

        if(isDeleted){
            sendServerMsg(resp, HttpServletResponse.SC_OK, "Customer Deleted Successfully!");
        }else {
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Customer Delete Failed!");
        }
    }

    private void getAllCustomer(HttpServletResponse resp)  {

        List<CustomerDTO> customerList = customerBO.getAllCustomer();

        Jsonb jsonb = JsonbBuilder.create();
        try {
            jsonb.toJson(customerList, resp.getWriter());
        } catch (IOException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }

    }

    private void getCustomerById(HttpServletRequest req, HttpServletResponse resp)  {
        String cusID = req.getParameter("cusID");
        CustomerDTO customer;

        customer = customerBO.getCustomerByID( cusID);

        Jsonb jsonb = JsonbBuilder.create();
        try {
            jsonb.toJson(customer, resp.getWriter());
        } catch (IOException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }

    }

    private List<String> getCusIDList() {
        List<String> customerIDList;

        customerIDList = customerBO.getCustomerIDList();
        return customerIDList;

    }

    private void getCustomerCount(HttpServletResponse resp) {

            int count = dashboardBO.getCustomerCount();

            Jsonb jsonb = JsonbBuilder.create();
        try {
            jsonb.toJson(count, resp.getWriter());
        } catch (IOException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }

    }

    private boolean validation(String id, HttpServletResponse resp, String name, String address, double salary) {
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

    private void sendServerMsg(HttpServletResponse resp, int status, String msg){
        resp.setStatus(status);
        try {
            resp.getWriter().println(msg);
        } catch (IOException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }
}
