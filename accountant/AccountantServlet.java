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

public class AccountantServlet extends HttpServlet {
  private Connection connection;
  private Statement statement;
  private int mysqlUpdateValue;
  private ServletContext context;

  public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
  {
    // Assign context
    context = getServletContext();

    // Get user choice
    String cmdValue = req.getParameter("cmd");
    System.out.println(cmdValue);

    // Set up message res string
    String command = "";
    String message = "";

    try
    {
      // Get connection to db
      getDBConnection();

      // Switch for what the command is
      switch (cmdValue)
      {
        case "max":
          command = "{call Get_The_Maximum_Status_Of_All_Suppliers()}";
          break;
        case "sum":
          command = "{call Get_The_Sum_Of_All_Parts_Weights()}";
          break;
        case "numShipments":
          command = "{call Get_The_Total_Number_Of_Shipments()}";
          break;
        case "nameAndNumOfWorkers":
          command = "{call Get_The_Name_Of_The_Job_With_The_Most_Workers()}";
          break;
        case "suppliers":
          command = "{call List_The_Name_And_Status_Of_All_Suppliers()}";
          break;
        default:
          command = "{call ERROR()}";
      }

      CallableStatement statement = connection.prepareCall(command);

      boolean returnValue = statement.execute();

      if (returnValue)
      {
        ResultSet results = statement.getResultSet();

        // Convert results to html table
        message = ResultSetToHtmlTable.getHtmlRows(results);
      } 

      else
        message = "Error executing RPC!";

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
    RequestDispatcher dispatcher = context.getRequestDispatcher("/accountantHome.jsp");
    dispatcher.forward(req, res);
  } // end doPost()

  private void getDBConnection() throws IOException
  {
    // Get input file
    FileInputStream in = new FileInputStream(context.getRealPath("/WEB-INF/lib/accountant.properties"));

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
