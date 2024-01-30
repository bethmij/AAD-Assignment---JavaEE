package lk.ijse.gdse66.backend.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse66.backend.bo.BOFactory;
import lk.ijse.gdse66.backend.bo.custom.DashboardBO;
import lk.ijse.gdse66.backend.bo.custom.PlaceOrderBO;
import lk.ijse.gdse66.backend.dto.OrderDTO;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = "/order")
public class PlaceOrderServlet extends HttpServlet {
    DataSource source;
    PlaceOrderBO placeOrderBO = BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PLACEORDERBO);
    DashboardBO dashboardBO = BOFactory.getBoFactory().getBO(BOFactory.BOTypes.DASHBOARDBO);

    @Override
    public void init(){
        try {
            InitialContext initCtx = new InitialContext();
            source = (DataSource)initCtx.lookup("java:comp/env/jdbc/pool");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String option = req.getParameter("option");

        switch (option){
            case "SEARCH" :
                getOrderDetailsByID(req, resp, source);
                break;

            case "ID" :
                getOrderIDs(resp, source);
                break;

            case "COUNT":
                getOrderCount(resp, source);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        OrderDTO orderDTO = JsonbBuilder.create().fromJson(req.getReader(), OrderDTO.class);


        try (Connection connection = source.getConnection()){
            boolean isOrderSaved = placeOrderBO.saveOrder(connection, orderDTO);

            if (isOrderSaved) {
                sendServerMsg(resp, HttpServletResponse.SC_OK, "Order Updated Successfully!");
            }else {
                sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Order Update Failed!");
            }

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        OrderDTO orderDTO = JsonbBuilder.create().fromJson(req.getReader(), OrderDTO.class);

        try (Connection connection = source.getConnection()){
            boolean isOrderSaved = placeOrderBO.updateOrder(connection, orderDTO);

            if (isOrderSaved) {
                sendServerMsg(resp, HttpServletResponse.SC_OK, "Order Updated Successfully!");
            }else {
                sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Order Update Failed!");
            }

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String cusID = req.getParameter("ID");

        try (Connection connection = source.getConnection()){
            boolean isDeleted = placeOrderBO.deleteOrder(connection, cusID);

            if(isDeleted){
                sendServerMsg(resp, HttpServletResponse.SC_OK, "Order Deleted Successfully!");
            }else {
                sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Order Delete Failed!");
            }
        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getOrderDetailsByID(HttpServletRequest req, HttpServletResponse resp, DataSource source) throws IOException {
        String id = req.getParameter("id");

        try (Connection connection = source.getConnection()){
            OrderDTO order = placeOrderBO.getOrderByID(connection, id);

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(order, resp.getWriter());

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private void getOrderIDs(HttpServletResponse resp, DataSource source) throws IOException {
        try (Connection connection = source.getConnection()){
            List<String> orderIDList = placeOrderBO.getOrderIDList(connection);

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(orderIDList, resp.getWriter());

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private void getOrderCount(HttpServletResponse resp, DataSource source) throws IOException {
        try (Connection connection = source.getConnection()){
            int count = dashboardBO.getOrderCount(connection);

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(count, resp.getWriter());

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendServerMsg(HttpServletResponse resp, int status, String msg) throws IOException {
        resp.setStatus(status);
        resp.getWriter().println(msg);
    }
}
