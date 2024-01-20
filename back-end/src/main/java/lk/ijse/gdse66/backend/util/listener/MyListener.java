package lk.ijse.gdse66.backend.util.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.commons.dbcp.BasicDataSource;

@WebListener
public class MyListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        BasicDataSource source = new BasicDataSource();
        source.setDriverClassName("com.mysql.cj.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3306/company");
        source.setUsername("root");
        source.setPassword("1234");
        source.setMaxActive(5);
        source.setInitialSize(5);

        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("bds",source);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
