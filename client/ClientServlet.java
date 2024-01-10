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

public class ClientServlet extends HttpServlet {
  private Connection connection;
  private Statement statement;
  private int mysqlUpdateValue;
  private ServletContext context;

  public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
  {
    // Assign context
    context = getServletContext();

    // Get command
    String command = req.getParameter("command");

    // Set up message res string
    String message = "";
    
    // Whether command is select or not
    boolean select = false;

    try
    {
      // Get connection to db
      getDBConnection();

      // Create statement
      statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

      // Prep inbound command
      command = command.strip();

      // Extract command
      if (command.length() >= 6 && command.substring(0,6).toLowerCase().equals("select"))
        select = true;


      // Handle inbound user command
      if (select)
      {
        ResultSet resultSet = statement.executeQuery(command);

        // Convert ResultSet to html table
        message = ResultSetToHtmlTable.getHtmlRows(resultSet);
      }

      else
        mysqlUpdateValue = statement.executeUpdate(command);

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
    session.setAttribute("command", command);

    // Create dispatcher and forward it
    RequestDispatcher dispatcher = context.getRequestDispatcher("/clientHome.jsp");
    dispatcher.forward(req, res);
  } // end doPost()

  private void getDBConnection() throws IOException
  {
    // Get input file
    FileInputStream in = new FileInputStream(context.getRealPath("/WEB-INF/lib/client.properties"));

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
