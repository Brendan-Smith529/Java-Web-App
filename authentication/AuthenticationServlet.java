/* Name: Brendan Smith
Course: CNT 4714 – Fall 2023 – Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: December 5, 2023
*/

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class AuthenticationServlet extends HttpServlet
{
  protected void doPost( HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
  {
    // Assign context
    ServletContext context = getServletContext();

    // Get user inputted username and password
    String username = req.getParameter("username");
    String password = req.getParameter("password");

    // Create var for if user is found
    boolean validCredentials = false;

    // Create Scanner for file
    Scanner in = new Scanner(new File(context.getRealPath("/WEB-INF/lib/credentials.txt")));

    // Search through file for credentials
    while (!validCredentials && in.hasNextLine())
    {
      // Split into user and password
      String [] input = in.nextLine().split(",");

      // Strip whitespace for if file is formatted in "user, pass" instead of "user,pass"
      input[1] = input[1].strip();
      System.out.println(input[0] + " " + input[1]);

      // Set validCredentials to true if they're found in the file
      if (input[0].equals(username) && input[1].equals(password))
      {
        validCredentials = true;
        break;
      }
    }
    
    // If the credentials don't exist in the file: send to error page
    if (!validCredentials)
      res.sendRedirect("/Project-4/errorpage.html");

    else
    {
      // Create patterns to determine auth levels
      Pattern rootPattern = Pattern.compile("root", Pattern.CASE_INSENSITIVE);
      Pattern clientPattern = Pattern.compile("client", Pattern.CASE_INSENSITIVE);
      Pattern dataPattern = Pattern.compile("data", Pattern.CASE_INSENSITIVE);

      // Send users with "root" in their username to rootHome
      if (rootPattern.matcher(username).find())
        res.sendRedirect("/Project-4/rootHome.jsp");

      // Send users with "client" in their username to clientHome
      else if (clientPattern.matcher(username).find())
        res.sendRedirect("/Project-4/clientHome.jsp");

      // Send users with "data" in their username to dataEntryHome
      else if (dataPattern.matcher(username).find())
        res.sendRedirect("/Project-4/dataEntryHome.jsp");

      // Default: send users to accountantHome
      else res.sendRedirect("/Project-4/accountantHome.jsp");
    }
  }
}
