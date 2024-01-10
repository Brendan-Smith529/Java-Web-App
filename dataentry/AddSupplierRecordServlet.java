/* Name: Brendan Smith
Course: CNT 4714 – Fall 2023 – Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: December 5, 2023
*/

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;
import java.util.*;
import javax.sql.DataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

public class AddSupplierRecordServlet extends HttpServlet {
  private Connection connection;
  private PreparedStatement statement;
  private int mysqlUpdateValue;
  private ServletContext context;

  public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
  {
    // Assign context
    context = getServletContext();

    // Get parameters (snum: string, sname: string, status: string, city: string)
    String snum = req.getParameter("snum");
    String sname = req.getParameter("sname");
    String status = req.getParameter("status");
    String city = req.getParameter("city");

    // Set up message res string
    String message = "";
    
    try
    {
      // Get connection to db
      getDBConnection();

      // Create updating command
      String updatingCommand = "insert into suppliers values (?, ?, ?, ?)";

      // Create statement
      statement = connection.prepareStatement(updatingCommand);

      // Bind values to statement
      statement.setString(1, snum);
      statement.setString(2, sname);
      statement.setString(3, status);
      statement.setString(4, city);

      // Get result of statement execution
      int returnValue = statement.executeUpdate();

      if (returnValue != 0)
      {
        ResultSet results = statement.getResultSet();

        // Create general string
        String genMessage = "New suppliers record: (%s, %s, %s, %s) - successfully entered into the database.";
        message = "<td class=\"result\"><b>" + String.format(genMessage, snum, sname, status, city) + "</b></td>";
      }

      else
        message = "Error entering record into the database. Database not updated!";

      statement.close();
    }

    catch (SQLException e)
    {
      message = "<tr class=\"error\"><td><font color=#ffffff><b>Error executing the SQL statement:</b><br>" + e.getMessage() + "</tr></td></font>";
    }

    // Get the session
    HttpSession session = req.getSession();

    // Set the session attributes
    session.setAttribute("message", message);

    // Create dispatcher and forward it
    RequestDispatcher dispatcher = context.getRequestDispatcher("/dataEntryHome.jsp");
    dispatcher.forward(req, res);
  } // end doPost()

  private void getDBConnection() throws IOException
  {
    FileInputStream in = new FileInputStream(context.getRealPath("WEB-INF/lib/data.properties"));
    // Create datasource object
    MysqlDataSource dataSource = null;

    Properties props = new Properties();

    // Read file into properties
    props.load(in);

    try
    {
      // Initialize Data Source
      dataSource = new MysqlDataSource();
      dataSource.setURL(props.getProperty("MYSQL_DB_URL"));
      dataSource.setUser(props.getProperty("MYSQL_DB_USERNAME"));
      dataSource.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));

      this.connection = dataSource.getConnection();
    }

    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
}
