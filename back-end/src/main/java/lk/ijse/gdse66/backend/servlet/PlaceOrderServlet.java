package lk.ijse.gdse66.backend.servlet;

import jakarta.json.*;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

@WebServlet (urlPatterns = "/order")
public class PlaceOrderServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource source;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String option = req.getParameter("option");

        if(option.equals("SEARCH")){
            String id = req.getParameter("id");
            JsonArrayBuilder orderDetails = Json.createArrayBuilder();
            JsonArrayBuilder itemList = Json.createArrayBuilder();
            JsonObjectBuilder order = Json.createObjectBuilder();;

            try (Connection connection = source.getConnection()){
                PreparedStatement pst = connection.prepareStatement(
                        "SELECT o.*, od.itemCode FROM orders o JOIN orderdetails od on o.oid = od.oid WHERE o.oid=?"
                );
                pst.setString(1,id);
                ResultSet resultSet = pst.executeQuery();
                while (resultSet.next()) {
                    order = Json.createObjectBuilder();
                    order.add("oid", resultSet.getString(1));
                    order.add("date", (JsonValue) resultSet.getDate(2));
                    order.add("customerID", resultSet.getString(3));
                    itemList.add(resultSet.getString(4));
                }
                order.add("items",itemList.build());
                sendMsg(resp,orderDetails.add(order.build()).build(), "Got the details", 200);

            } catch (SQLException e) {
                sendMsg(resp,JsonValue.EMPTY_JSON_ARRAY,e.getLocalizedMessage(),400);
            }

        }else if(option.equals("ID")){
            try (Connection connection = source.getConnection()){
                PreparedStatement pst = connection.prepareStatement("SELECT oid FROM orders");
                ResultSet resultSet = pst.executeQuery();
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                while (resultSet.next()) {
                    arrayBuilder.add(resultSet.getString(1));
                }

                sendMsg(resp, arrayBuilder.build(), "Got the ID List",200);
            } catch (SQLException e) {
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, e.getLocalizedMessage(),400);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        JsonObject jsonObject = Json.createReader(req.getReader()).readObject();
        String oid = jsonObject.getString("oid");
        String date = jsonObject.getString("date");
        String customerID = jsonObject.getString("customerID");
        JsonArray orderDetailsList = jsonObject.getJsonArray("orderDetails");
//        System.out.println(oid+" "+date+" "+customerID+" "+orderDetailsList);

        Connection connection = null;
        try {
            connection = source.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO orders (oid, date, customerID) VALUES (?,?,?)");
            preparedStatement.setString(1, oid);
            preparedStatement.setDate(2, Date.valueOf(date));
            preparedStatement.setString(3, customerID);
            boolean isOrderAdded = preparedStatement.executeUpdate() > 0;

            boolean isOrderDetailsAdded = false;
            if (isOrderAdded) {

                for (JsonValue jsonValue : orderDetailsList) {
                    JsonObject orderDetails = jsonValue.asJsonObject();
                    String oid1 = jsonObject.getString("oid");
                    String code = orderDetails.getString("code");
                    int qty = orderDetails.getInt("qty");
                    double unitPrice = orderDetails.getInt("unitPrice");
//                    System.out.println(code+" "+qty+" "+unitPrice);
                    PreparedStatement pst = connection.prepareStatement(
                            "INSERT INTO orderdetails (oid, itemCode, qty, unitPrice) VALUES (?,?,?,?)");
                    pst.setString(1, oid1);
                    pst.setString(2, code);
                    pst.setInt(3, qty);
                    pst.setDouble(4, unitPrice);
                    isOrderDetailsAdded = pst.executeUpdate() > 0;
                }

                if (isOrderDetailsAdded) {
                    sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, "Order Placed Successfully", 200);
                    connection.commit();
                }
            }
        } catch (SQLException e) {
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, ex.getLocalizedMessage(), 400);
            }
            System.out.println(e.getMessage());
            sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, e.getLocalizedMessage(), 400);
        }finally {
            try {
                assert connection != null;
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, e.getLocalizedMessage(), 400);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        JsonObject jsonObject = Json.createReader(req.getReader()).readObject();
        String oid = jsonObject.getString("oid");
        String date = jsonObject.getString("date");
        String customerID = jsonObject.getString("customerID");
        JsonArray orderDetailsList = jsonObject.getJsonArray("orderDetails");

        Connection connection = null;
        try {
            connection = source.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE  orders SET date=?, customerID=? WHERE oid=?");
            preparedStatement.setString(3, oid);
            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.setString(2, customerID);
            boolean isOrderUpdated = preparedStatement.executeUpdate() > 0;

            boolean isOrderDetailsUpdated = false;
            if (isOrderUpdated) {

                for (JsonValue jsonValue : orderDetailsList) {
                    JsonObject orderDetails = jsonValue.asJsonObject();
                    String oid1 = jsonObject.getString("oid");
                    String code = orderDetails.getString("code");
                    int qty = orderDetails.getInt("qty");
                    double unitPrice = orderDetails.getInt("unitPrice");

                    PreparedStatement pst = connection.prepareStatement(
                            "UPDATE orderdetails SET itemCode=?, qty=?, unitPrice=? WHERE oid=?");
                    pst.setString(4, oid1);
                    pst.setString(1, code);
                    pst.setInt(2, qty);
                    pst.setDouble(3, unitPrice);
                    isOrderDetailsUpdated = pst.executeUpdate() > 0;
                }

                if (isOrderDetailsUpdated) {
                    sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, "Order Updated Successfully", 200);
                    connection.commit();
                }
            }
        } catch (SQLException e) {
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, ex.getLocalizedMessage(), 400);
            }
            System.out.println(e.getMessage());
            sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, e.getLocalizedMessage(), 400);
        }finally {
            try {
                assert connection != null;
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                sendMsg(resp, JsonValue.EMPTY_JSON_ARRAY, e.getLocalizedMessage(), 400);
            }
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doOptions(req, resp);
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
