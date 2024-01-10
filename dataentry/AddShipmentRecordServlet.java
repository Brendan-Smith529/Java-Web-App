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

public class AddShipmentRecordServlet extends HttpServlet {
  private Connection connection;
  private PreparedStatement statement;
  private Statement businessLogicStatement;
  private int mysqlUpdateValue;
  private ServletContext context;

  public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
  {
    // Assign context
    context = getServletContext();

    // Get parameters (snum: string, pnum: string, jnum: string, quantity: int)
    String snum = req.getParameter("snum");
    String pnum = req.getParameter("pnum");
    String jnum = req.getParameter("jnum");
    int quantity = Integer.parseInt(req.getParameter("quantity"));

    // Set up message res string
    String message = "";

    // Boolean to determine business logic
    boolean businessLogic = (quantity >= 100) ? true : false;
    
    try
    {
      // Get connection to db
      getDBConnection();

      // Create updating command
      String updatingCommand = "insert into shipments values (?, ?, ?, ?)";

      // Create statement
      statement = connection.prepareStatement(updatingCommand);

      // Bind values to statement
      statement.setString(1, snum);
      statement.setString(2, pnum);
      statement.setString(3, jnum);
      statement.setInt(4, quantity);

      // Prep business logic if needed
      if (businessLogic)
        prepBusinessLogic();

      // Get result of statement execution
      int returnValue = statement.executeUpdate();

      if (returnValue != 0)
      {
        ResultSet results = statement.getResultSet();

        // Create general string
        String genMessage = "New jobs record: (%s, %s, %s, %d) - successfully entered into the database. Business logic ";

        if (!businessLogic)
          message = "<td class=\"result\"><b>" + String.format(genMessage, snum, pnum, jnum, quantity) + "not triggered.</b></td>";

        else
        {
          executeBusinessLogic();
          message = "<td class=\"result\"><b>" + String.format(genMessage, snum, pnum, jnum, quantity) + " triggered.</b></td>";
        }
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

  private void executeBusinessLogic() throws SQLException
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
    businessLogicStatement.executeUpdate(command.toString());

    // Drop created table after use
    businessLogicStatement.executeUpdate("drop table beforeShipments;");

    // Close statement
    businessLogicStatement.close();
  }

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
