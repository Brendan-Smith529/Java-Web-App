/* Name: Brendan Smith
Course: CNT 4714 – Fall 2023 – Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: December 5, 2023
*/

import java.sql.*;

public class ResultSetToHtmlTable
{
  public static synchronized String getHtmlRows(ResultSet results) throws SQLException
  {
    StringBuilder htmlTable = new StringBuilder();
    ResultSetMetaData metaData = results.getMetaData();
    int numColumns = metaData.getColumnCount();

    // Set table header row
    htmlTable.append("<tr>");

    // Add table data
    for (int i = 1; i <= numColumns; ++i)
      htmlTable.append("<th><b>" + metaData.getColumnName(i) + "</b></th>");

    // Close table header
    htmlTable.append("</tr>");

    // Create counter
    int counter = 0;
    boolean isEven = false;

    // Set remainder of table
    while (results.next())
    {
      isEven = counter++ % 2 == 0;
      String className = isEven ? "\"even\">" : "\"odd\">";

      htmlTable.append("<tr class=" + className);

      for (int i = 1; i <= numColumns; i++)
        htmlTable.append("<td>" + results.getString(i) + "</td>");

      htmlTable.append("</tr>");
    }

    return htmlTable.toString();
  }
}
