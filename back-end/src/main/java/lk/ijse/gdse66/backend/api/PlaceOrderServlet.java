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

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/order")
public class PlaceOrderServlet extends HttpServlet {

    PlaceOrderBO placeOrderBO = BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PLACEORDERBO);
    DashboardBO dashboardBO = BOFactory.getBoFactory().getBO(BOFactory.BOTypes.DASHBOARDBO);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        String option = req.getParameter("option");

        switch (option){
            case "SEARCH" :
                getOrderDetailsByID(req, resp);
                break;

            case "ID" :
                getOrderIDs(resp);
                break;

            case "COUNT":
                getOrderCount(resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        OrderDTO orderDTO;
        try {
            orderDTO = JsonbBuilder.create().fromJson(req.getReader(), OrderDTO.class);
            boolean isOrderSaved = placeOrderBO.saveOrder( orderDTO);

            if (isOrderSaved) {
                sendServerMsg(resp, HttpServletResponse.SC_OK, "Order Saved Successfully!");
            }else {
                sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Order Save Failed!");
            }
        } catch (IOException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }



    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {

        OrderDTO orderDTO;
        try {
            orderDTO = JsonbBuilder.create().fromJson(req.getReader(), OrderDTO.class);
            boolean isOrderSaved = placeOrderBO.updateOrder(orderDTO);

            if (isOrderSaved) {
                sendServerMsg(resp, HttpServletResponse.SC_OK, "Order Updated Successfully!");
            }else {
                sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Order Update Failed!");
            }
        } catch (IOException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }



    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String cusID = req.getParameter("ID");

        boolean isDeleted = placeOrderBO.deleteOrder(cusID);

        if(isDeleted){
            sendServerMsg(resp, HttpServletResponse.SC_OK, "Order Deleted Successfully!");
        }else {
            sendServerMsg(resp, HttpServletResponse.SC_BAD_REQUEST, "Order Delete Failed!");
        }
    }

    private void getOrderDetailsByID(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");

        OrderDTO order = placeOrderBO.getOrderByID( id);

        Jsonb jsonb = JsonbBuilder.create();
        try {
            jsonb.toJson(order, resp.getWriter());
        } catch (IOException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }

    }

    private void getOrderIDs(HttpServletResponse resp) {
        List<String> orderIDList = placeOrderBO.getOrderIDList();

        Jsonb jsonb = JsonbBuilder.create();
        try {
            jsonb.toJson(orderIDList, resp.getWriter());
        } catch (IOException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }

    }

    private void getOrderCount(HttpServletResponse resp){
        try {
            int count = dashboardBO.getOrderCount();

            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(count, resp.getWriter());

        } catch (IOException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private void sendServerMsg(HttpServletResponse resp, int status, String msg) {
        resp.setStatus(status);
        try {
            resp.getWriter().println(msg);
        } catch (IOException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }
}
