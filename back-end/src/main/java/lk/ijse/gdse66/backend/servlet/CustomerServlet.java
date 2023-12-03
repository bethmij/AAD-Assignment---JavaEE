package lk.ijse.gdse66.backend.servlet;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource source;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String id = req.getParameter("cusID");
        String name = req.getParameter("cusName");
        String address = req.getParameter("cusAddress");
        String salary = req.getParameter("cusSalary");
        System.out.println(id+" "+name+" "+address+" "+salary);


        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company","root","1234");
            Connection connection = source.getConnection();
            PreparedStatement pst = connection.prepareStatement(
                    "INSERT INTO company.customer (id, name, address, salary) VALUES (?,?,?,?)");
            pst.setString(1,id);
            pst.setString(2,name);
            pst.setString(3,address);
            pst.setString(4,salary);
            boolean isAdded = pst.executeUpdate() > 0;

            if(isAdded){
                System.out.println("Added Successfully");
            }else {
                System.out.println("Failed");
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
}
