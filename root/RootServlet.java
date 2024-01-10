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
import java.util.regex.*;
import javax.sql.DataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

public class RootServlet extends HttpServlet {
  private Connection connection;
  private Statement statement;
  private Statement businessLogicStatement;
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

    Pattern quantityPattern = Pattern.compile("shipments", Pattern.CASE_INSENSITIVE);

    // Boolean to determine whether we use business logic
    boolean businessLogic = quantityPattern.matcher(command).find();

    try
    {
      // Get connection to db
      getDBConnection();

      // Create statement
      statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

      // Prep inbound command
      command = command.strip();

      // Determine wheterh command is query
      if (command.length() >= 6 && command.substring(0,6).toLowerCase().equals("select"))
        select = true;


      // Handle inbound user command
      if (select)
      {
        ResultSet resultSet = statement.executeQuery(command);

        // Convert ResultSet to html table
        message = ResultSetToHtmlTable.getHtmlRows(resultSet);
      }

      else if (!businessLogic)
      {
        StringBuilder messagePrep = new StringBuilder();

        // Execute user statement and get result
        mysqlUpdateValue = statement.executeUpdate(command);

        // Add statement success code
        messagePrep.append("<tr class=\"update\"><td>The statement executed successfully. ");
        messagePrep.append("A total of " + mysqlUpdateValue + " row(s) were updated.");

        // Add that business logic was not used
        messagePrep.append("<br><br>Business Logic Not Triggered!<br><br></td></tr>");

        message = messagePrep.toString();
      }

      else
      {
        StringBuilder messagePrep = new StringBuilder();

        // Create new database for business logic
        prepBusinessLogic();

        // Execute user statement and get result
        mysqlUpdateValue = statement.executeUpdate(command);

        // Add statement success code
        messagePrep.append("<br><br><tr class=\"update\"><td>The statement executed successfully.");
        messagePrep.append("<br>" + mysqlUpdateValue + " row(s) affected.<br><br>");

        // Message about business logic detected
        messagePrep.append("Business Logic Detected! - Updating Supplier Status.<br><br>");

        // Execute business logic
        int numSuppliersUpdated = executeBusinessLogic();

        // Update about business logic
        messagePrep.append("Business Logic updated " + numSuppliersUpdated + " supplier status marks.<br><br></td></tr>");

        message = messagePrep.toString();
      }

      statement.close();
    }

    catch (SQLException e)
    {
      message = "<br><br><tr class=\"error\"><td><font color=#ffffff><b>Error executing the SQL statement:</b><br>" + e.getMessage() + "</tr></td></font>";
    }

    // Get the session
    HttpSession session = req.getSession();

    // Set the session attributes
    session.setAttribute("message", message);
    session.setAttribute("command", command);

    // Create dispatcher and forward it
    RequestDispatcher dispatcher = context.getRequestDispatcher("/rootHome.jsp");
    dispatcher.forward(req, res);
  } // end doPost()


  /* Business Logic */
  private void prepBusinessLogic() throws SQLException
  {
    // Create statement
    businessLogicStatement = connection.createStatement();

    // Drop table (if exists)
    businessLogicStatement.executeUpdate("drop table if exists beforeShipments; ");

    // Create table
    businessLogicStatement.executeUpdate("create table beforeShipments like shipments; ");

    // Copy shipments into new table
    businessLogicStatement.executeUpdate("insert into beforeShipments select * from shipments;");
  }

  private int executeBusinessLogic() throws SQLException
  {
    StringBuilder command = new StringBuilder();

    // Add command line by line
    command.append("update suppliers ");
    command.append("set status = status + 5 ");
    command.append("where suppliers.snum in ");
    command.append("(select distinct snum ");
    command.append("from shipments ");
    command.append("where shipments.quantity >= 100 ");
    command.append("and ");
    command.append("not exists (select * ");
    command.append("from beforeShipments ");
    command.append("where shipments.snum = beforeShipments.snum ");
    command.append("and shipments.pnum = beforeShipments.pnum ");
    command.append("and shipments.jnum = beforeShipments.jnum ");
    command.append("and beforeShipments.quantity >= 100 ");
    command.append(") ");
    command.append("); ");

    // Run command and get result
    int numSuppliersUpdated = businessLogicStatement.executeUpdate(command.toString());

    // Drop created table after use
    businessLogicStatement.executeUpdate("drop table beforeShipments;");

    // Close statement
    businessLogicStatement.close();

    return numSuppliersUpdated;
  }

  private void getDBConnection() throws IOException
  {
    FileInputStream in = new FileInputStream(context.getRealPath("WEB-INF/lib/root.properties"));
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
