package lk.ijse.gdse66.backend.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse66.backend.dto.OrderDTO;
import lk.ijse.gdse66.backend.entity.OrderDetailsEntity;
import lk.ijse.gdse66.backend.entity.OrderEntity;
import lk.ijse.gdse66.backend.util.CrudUtil;
import org.apache.commons.dbcp.BasicDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/order")
public class PlaceOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        BasicDataSource source = (BasicDataSource) req.getServletContext().getAttribute("bds");

        String option = req.getParameter("option");
        List<String> orderIDList = new ArrayList<>();

        switch (option){
            case "SEARCH" :
                getOrderDetailsByID(req, resp, source);
                break;

            case "ID" :
                getOrderIDs(resp, source, orderIDList);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("doPost");
        BasicDataSource source = (BasicDataSource) req.getServletContext().getAttribute("bds");

        OrderDTO orderDTO = JsonbBuilder.create().fromJson(req.getReader(), OrderDTO.class);
        String oid = orderDTO.getOrderId();
        LocalDate date = orderDTO.getOrderDate();
        String customerID = orderDTO.getCustomerId();
        List<OrderDetailsEntity> orderDetailsList = orderDTO.getOrderDetails();


        Connection connection = null;
        try {
            connection = source.getConnection();
            connection.setAutoCommit(false);

            String sql = "INSERT INTO orders (oid, date, customerID) VALUES (?,?,?)";
            boolean isOrderAdded = CrudUtil.execute(sql, connection, oid, date, customerID);

            boolean isOrderDetailsAdded = false;
            if (isOrderAdded) {

                for (OrderDetailsEntity orderDetail : orderDetailsList) {

                    String code = orderDetail.getItemCode();
                    int qty = orderDetail.getQtyOnHand();
                    double unitPrice = orderDetail.getUnitPrice();

                    String sql1 = "INSERT INTO orderdetails (oid, itemCode, qty, unitPrice) VALUES (?,?,?,?)";
                    isOrderDetailsAdded = CrudUtil.execute(sql1, connection, oid, code, qty,unitPrice);

                }

                if (isOrderDetailsAdded) {
                    sendServerMsg(resp, HttpServletResponse.SC_OK, "Order Placed Successfully!");
                    connection.commit();
                }
            }
        } catch (SQLException e) {
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException ignored) {}
            System.out.println(e.getLocalizedMessage());
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());

        }finally {
            try {
                assert connection != null;
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
                sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BasicDataSource source = (BasicDataSource) req.getServletContext().getAttribute("bds");

        OrderDTO orderDTO = JsonbBuilder.create().fromJson(req.getReader(), OrderDTO.class);
        String oid = orderDTO.getOrderId();
        LocalDate date = orderDTO.getOrderDate();
        String customerID = orderDTO.getCustomerId();
        List<OrderDetailsEntity> orderDetailsList = orderDTO.getOrderDetails();


        Connection connection = null;
        try {
            connection = source.getConnection();
            connection.setAutoCommit(false);

            String sql = "UPDATE orders SET date=?, customerID=? WHERE oid=?";
            boolean isOrderUpdated = CrudUtil.execute(sql, connection, date, customerID, oid);

            boolean isOrderDetailsUpdated = false;
            if (isOrderUpdated) {

                for (OrderDetailsEntity orderDetail : orderDetailsList) {

                    String code = orderDetail.getItemCode();
                    int qty = orderDetail.getQtyOnHand();
                    double unitPrice = orderDetail.getUnitPrice();

                    String sql1 = "UPDATE orderdetails SET qty=?, unitPrice=? WHERE oid=? AND itemCode=?";
                    isOrderDetailsUpdated = CrudUtil.execute(sql1, connection, qty, unitPrice, oid, code);
                }

                if (isOrderDetailsUpdated) {
                    sendServerMsg(resp, HttpServletResponse.SC_OK, "Order Updated Successfully!");
                    connection.commit();
                }
            }
        } catch (SQLException e) {
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException ignored) {}
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());

        }finally {
            try {
                assert connection != null;
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
            }
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {

    }

    private void getOrderDetailsByID(HttpServletRequest req, HttpServletResponse resp, BasicDataSource source) throws IOException {
        List<OrderDetailsEntity> orderList = new ArrayList<>();
        String id = req.getParameter("id");
        OrderDTO orderDTO = new OrderDTO();

        try (Connection connection = source.getConnection()){
            String sql = "SELECT o.*, od.itemCode, od.qty, od.unitPrice FROM orders o JOIN orderdetails od on o.oid = od.oid WHERE o.oid=?";
            ResultSet resultSet = CrudUtil.execute(sql, connection, id);

            while (resultSet.next()) {
                String orderID = resultSet.getString(1);
                LocalDate date = resultSet.getDate(2).toLocalDate();
                String customerID = resultSet.getString(3);
                String itemCode = resultSet.getString(4);
                int qtyOnHand = resultSet.getInt(5);
                double unitPrice = resultSet.getDouble(6);

//                OrderEntity order = new OrderEntity(orderID, date, customerID);
                OrderDetailsEntity orderDetails = new OrderDetailsEntity(orderID, itemCode, qtyOnHand, unitPrice);
                orderList.add(orderDetails);

                orderDTO = new OrderDTO(orderID, date, customerID, null);

            }

            orderDTO.setOrderDetails(orderList);
            Jsonb jsonb = JsonbBuilder.create();
            jsonb.toJson(orderDTO, resp.getWriter());

        } catch (SQLException e) {
            sendServerMsg(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    private void getOrderIDs(HttpServletResponse resp, BasicDataSource source, List<String> orderIDList) throws IOException {
        try (Connection connection = source.getConnection()){
            String sql = "SELECT oid FROM orders";
            ResultSet resultSet = CrudUtil.execute(sql, connection);

            while (resultSet.next()) {
                orderIDList.add(resultSet.getString(1));
            }

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
