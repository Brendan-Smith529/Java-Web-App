<%--
Name:Brendan Smith
	Course: CNT 4714 – Fall 2023 – Project Four
	Assignment title: A Three-Tier Distributed Web-Based Application
	Date: December 5, 2023
--%>

<!DOCTYPE html>

<%-- Start Scriptlet --%>
<%
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

    radio {
      margin-left: 30px;
    }

    .perm {
      color: red;
      font-size: 15pt;
    }

    .command {
      background-color: gray;
      font-size: 15pt;
      font-weight: bold;
      display: inline-block;
      width: 90%;
    }

    .options {
      color: black;
      text-align: left;
      padding-top: 10px;
      padding-left: 30px;
      padding-right: 20px;
    }

    .selections {
      text-align: center;
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

    #SQLBox {
      color: white;
      background-color: blue;
      font-size: 20px;
    }

    #yellowText {
      color: yellow;
    }

    #blueText {
      color: blue;
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
    <p>You are connected to the Project 4 Enterprise System database as an <span class="perm">accountant-level</span> user.</p>
    <p>Please select the operation you would like to perform from the list below.</p>

    <br><br><br>

    <form action="AccountantUserApp" method="post">
      <div class="command">
        <!-- Bullet Points -->
        <div class="options">
          <input type="radio" value="max" name="cmd">  <span id="blueText">Get The Maximum Status Value Of All Suppliers</span>
          (Returns a maximum value)</input><br><br>

          <input type="radio" value="sum" name="cmd">  <span id="blueText">Get The Total Weight Of All Parts</span>
          (Returns a sum)</input><br><br>

          <input type="radio" value="numShipments" name="cmd">  <span id="blueText">Get The Total Number Of Shipments</span>
          (Returns the current number of shipments in total)</input><br><br>

          <input type="radio" value="nameAndNumOfWorkers" name="cmd">  <span id="blueText">Get The Name And Number of Workers Of The Job
            With The Most Workers</span>(Returns two values)</input><br><br>

          <input type="radio" value="suppliers" name="cmd">  <span id="blueText">List The Name And Status Of Every Supplier</span>
          (Returns a list of supplier names with status)</input><br><br><br>
        </div>

        <div class="selections">
          <!-- Buttons for command area -->
          <button type="submit" id="greenText">Execute Command</button>
          <button type="button" id="redText" onclick={resetInfo()}>Clear Results</button>
        </div>
      </div>
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
      function resetInfo() {
        $("#data").remove();
      }
    </script>
  </body>
</html>
