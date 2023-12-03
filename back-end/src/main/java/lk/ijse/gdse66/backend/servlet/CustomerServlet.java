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

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource source;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setContentType("application/json");
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        try (Connection connection = source.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM customer");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                double salary = resultSet.getDouble(4);

                JsonObjectBuilder builder = Json.createObjectBuilder();
                builder.add("cusID",id);
                builder.add("cusName",name);
                builder.add("cusAddress",address);
                builder.add("cusSalary",salary);
                arrayBuilder.add(builder.build());

            }
            sendMsg(resp, arrayBuilder.build(),"Got the Customer",200);
        } catch (SQLException e) {
            sendMsg(resp,null, e.getLocalizedMessage(), 400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setContentType("application/json");
        String id = req.getParameter("cusID");
        String name = req.getParameter("cusName");
        String address = req.getParameter("cusAddress");
        String salary = req.getParameter("cusSalary");
        System.out.println(id+" "+name+" "+address+" "+salary);


        try (Connection connection = source.getConnection()) {
            PreparedStatement pst = connection.prepareStatement(
                    "INSERT INTO company.customer (id, name, address, salary) VALUES (?,?,?,?)");
            pst.setString(1,id);
            pst.setString(2,name);
            pst.setString(3,address);
            pst.setString(4,salary);
            boolean isAdded = pst.executeUpdate() > 0;

            if(isAdded){
                sendMsg(resp, null, "Customer Added Successfully", 200);
            }else {
                sendMsg(resp, null, "Customer Addition Failed", 400);
            }
        } catch (SQLException e) {
            sendMsg(resp, null, e.getLocalizedMessage(), 400);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setContentType("application/json");
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String cusID = jsonObject.getString("cusID");
        String cusName = jsonObject.getString("cusName");
        String cusAddress = jsonObject.getString("cusAddress");
        String cusSalary = jsonObject.getString("cusSalary");
        System.out.println(cusID+" "+cusName+" "+cusAddress+" "+cusSalary );
        try (Connection connection = source.getConnection()){

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Methods", "DELETE");
        response.addHeader("Access-Control-Allow-Methods", "PUT");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Context-Type");
    }

    public <T> void sendMsg(HttpServletResponse resp, JsonArray data, String message, int status) throws IOException {
        resp.setStatus(200);
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("data",data);
        builder.add("message",message);
        builder.add("status",status);
        resp.getWriter().println(builder.build());
    }
}
