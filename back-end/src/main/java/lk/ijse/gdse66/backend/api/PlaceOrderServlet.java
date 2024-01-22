package lk.ijse.gdse66.backend.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse66.backend.bo.BOFactory;
import lk.ijse.gdse66.backend.bo.custom.PlaceOrderBO;
import lk.ijse.gdse66.backend.dto.OrderDTO;
import org.apache.commons.dbcp.BasicDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = "/order")
public class PlaceOrderServlet extends HttpServlet {
    PlaceOrderBO placeOrderBO = BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PLACEORDERBO);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        BasicDataSource source = (BasicDataSource) req.getServletContext().getAttribute("bds");

        String option = req.getParameter("option");

        switch (option){
            case "SEARCH" :
                getOrderDetailsByID(req, resp, source);
                break;

            case "ID" :
                getOrderIDs(resp, source);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BasicDataSource source = (BasicDataSource) req.getServletContext().getAttribute("bds");

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
        BasicDataSource source = (BasicDataSource) req.getServletContext().getAttribute("bds");

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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {

    }

    private void getOrderDetailsByID(HttpServletRequest req, HttpServletResponse resp, BasicDataSource source) throws IOException {
        String id = req.getParameter("id");

        try (Connection connection = source.getConnection()){
            OrderDTO order = placeOrderBO.getOrderByID(connection, id);

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(order, resp.getWriter());

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private void getOrderIDs(HttpServletResponse resp, BasicDataSource source) throws IOException {
        try (Connection connection = source.getConnection()){
            List<String> orderIDList = placeOrderBO.getOrderIDList(connection);

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(orderIDList, resp.getWriter());

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private void sendServerMsg(HttpServletResponse resp, int status, String msg) throws IOException {
        resp.setStatus(status);
        resp.getWriter().println(msg);
    }
}
