<%--
Name:Brendan Smith
	Course: CNT 4714 – Fall 2023 – Project Four
	Assignment title: A Three-Tier Distributed Web-Based Application
	Date: December 5, 2023
--%>

<!DOCTYPE html>

<%-- Start Scriptlet --%>
<%
  String command = (String) session.getAttribute("command");
  if (command == null) command = "select * from suppliers";
  String message = (String) session.getAttribute("message");
  if (message == null) message = " ";
  %>

<html>

<head>
  <meta charset="utf-8">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>

  <style type='text/css'>
    body {
      background-color: black;
      color: white;
      font-family: Verdana, Arial, sans-serif;
      text-align: center;
    }
    
    h1 {
      font-size: 30pt;
      text-align: center;
    }

    h2 {
      font-family: inherit;
      font-size: 25pt;
    }

    p {
      font-size: 15pt;
      margin-bottom: -15px;
    }
    
    button {
      background-color: #655d1e;
      font-size: 15pt;
      font-weight: bold;
      margin-right: 20px;
    }
    
    table {
      border: 2px;
    }

    th {
      background-color: #ff0000;
    }

    tr {
      color: black;
      text-align: center;
    }

    .even {
      background-color: lightgray;
    }

    .odd {
      background-color: white;
    }

    .error {
      background-color: #ff0000;
    }

    .perm {
      color: red;
      font-size: 15pt;
    }

    .update {
      background-color: lime;
      font-weight: bold;
      font-size: 20px;
    }

    #SQLBox {
      color: white;
      background-color: blue;
      font-size: 20px;
    }

    #yellowText {
      color: yellow;
    }

    #greenText {
      color: lime;
    }

    #redText {
      color: red;
    }
  </style>
  
  <title>CNT 4714 Fall 2023 - Project 4</title>
</head>

  <body>
    <!-- Greeting messages at top -->
    <h1><span id="yellowText">Welcome to the Fall 2023 Project 4 Enterprise System</span></h1>
    <h2><span id="greenText">A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</span></h2>

    <!-- White line break -->
    <hr>

    <!-- Connection text-->
    <p>You are connected to the Project 4 Enterprise System database as a <span class="perm">root-level</span> user.</p>
    <p>Please enter any SQL query or update command in the box below.</p>

    <br><br><br>

    <form action="RootUserApp" method="post">
      <!-- SQL command input area -->
      <textarea name="command" id="SQLBox" rows="10" cols="75"><%=command%></textarea>

      <br><br>

      <!-- Buttons for command area -->
      <button type="submit" id="greenText">Execute Command</button>
      <button type="button" id="redText" onclick={resetForm()}>Reset Form</button>
      <button type="button" id="yellowText" onclick={clearResults()}>Clear Results</button>
    </form>

    <br><br>

    <p>All execution results will appear below this line.</p>

    <br><hr>

    <center>
      <p>
      <b>Execution Results:</b><br><br>
        <table id="data">
          <%=message%>
        </table>
      </p>
    </center>


    <!-- Button handling -->
    <script>
      function resetForm() {
        $("#SQLBox").val("");
      }

      function clearResults() {
        $("#data").remove();
      }
    </script>
  </body>
</html>
