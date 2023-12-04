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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet (urlPatterns = "/order")
public class PlaceOrderServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource source;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String option = req.getParameter("option");

        if(option.equals("ID")){
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
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
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
