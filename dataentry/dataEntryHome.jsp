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
      font-size: 25pt;
      text-align: center;
    }

    h2 {
      font-family: inherit;
      font-size: 20pt;
    }

    p {
      margin-bottom: -15px;
    }
    
    button {
      background-color: #655d1e;
      font-size: 15pt;
      font-weight: bold;
      margin-right: 20px;
    }

    fieldset {
      margin-bottom: 10px;
      text-align: left;
    }

    table {
      border: 3px solid yellow;
      text-align: center;
      width: 75%;
    }

    th {
      border: 2px solid #655d1e;
      padding-top: 5px;
      padding-bottom: 2px;
    }

    td {
      border: 2px solid #655d1e;
      padding-top: 4px;
      padding-bottom: 10px;
    }

    tr {
      border: 2px solid #655d1e;
    }

    .result {
      background-color: blue;
      text-align: left;
    }

    .error {
      background-color: red;
      text-align: left;
    }

    .perm {
      color: red;
      font-size: 12pt;
    }

    .table-container {
      margin-left: auto;
      margin-right: auto;
      display: flex;
      justify-content: center;
    }

    .button-container {
      text-align: center;
    }

    .cmd {
      font-size: 20pt;
      background-color: #655d1e;
      color: yellow;
      width: 60%;
    }

    #yellowText {
      color: yellow;
    }

    #blueText {
      color: cyan;
    }

    #lightblueText {
      color: lightblue;
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
    <h1><span id="redText">Welcome to the Fall 2023 Project 4 Enterprise System</span></h1>
    <h2><span id="blueText">Data Entry Application</span></h2>

    <!-- White line break -->
    <hr>

    <!-- Connection text-->
    <p>You are connected to the Project 4 Enterprise System database as a <span class="perm">data-entry-level</span> user.</p>
    <p>Please enter any SQL query or update command in the box below.</p>

    <br>

    <!-- White line break -->
    <hr>

    <!-- Create Tables -->
    <form action="SupplierUserApp" method="post">
      <fieldset>
        <br>
        <legend>Suppliers Record Insert</legend>
        <div class="table-container">
          <table>
            <tr id="lightblueText">
              <th>snum</th>
              <th>sname</th>
              <th>status</th>
              <th>city</th>
            </tr>

            <tr>
              <td>
                <input type="text" name="snum" class="cmd">
              </td>
              <td>
                <input type="text" name="sname" class="cmd">
              </td>
              <td>
                <input type="text" name="status" class="cmd">
              </td>
              <td>
                <input type="text" name="city" class="cmd">
              </td>
            </tr>
          </table>
        </div>

        <br>

        <div class="button-container">
          <button type="submit" id="greenText" name="suppliersForm">Enter Supplier Record Into Database</button>
          <button type="reset" id="redText" onclick={resetInfo()}>Clear Data And Results</button>
        </div>

        <br>
      </fieldset>
    </form>

    <form action="PartUserApp" method="post">
      <fieldset>
        <br>
        <legend>Parts Record Insert</legend>
        <div class="table-container">
          <table>
            <tr id="lightblueText">
              <th>pnum</th>
              <th>pname</th>
              <th>color</th>
              <th>weight</th>
              <th>city</th>
            </tr>

            <tr>
              <td>
                <input type="text" name="pnum" class="cmd">
              </td>
              <td>
                <input type="text" name="pname" class="cmd">
              </td>
              <td>
                <input type="text" name="color" class="cmd">
              <td>
                <input type="text" name="weight" class="cmd">
              </td>
              <td>
                <input type="text" name="city" class="cmd">
              </td>
            </tr>
          </table>
        </div>

        <br>

        <div class="button-container">
          <button type="submit" id="greenText" name="partsForm">Enter Part Record Into Database</button>
          <button type="reset" id="redText" onclick={resetInfo()}>Clear Data And Results</button>
        </div>

        <br>
      </fieldset>
    </form>

    <form action="JobUserApp" method="post">
      <fieldset>
        <br>
        <legend>Jobs Record Insert</legend>
        <div class="table-container">
          <table>
            <tr id="lightblueText">
              <th>jnum</th>
              <th>jname</th>
              <th>numworkers</th>
              <th>city</th>
            </tr>

            <tr>
              <td>
                <input type="text" name="jnum" class="cmd">
              </td>
              <td>
                <input type="text" name="jname" class="cmd">
              </td>
              <td>
                <input type="text" name="numWorkers" class="cmd">
              </td>
              <td>
                <input type="text" name="city" class="cmd">
              </td>
            </tr>
          </table>
        </div>

        <br>

        <div class="button-container">
          <button type="submit" id="greenText" name="jobsInsertForm">Enter Job Record Into Database</button>
          <button type="reset" id="redText" onclick={resetInfo()}>Clear Data And Results</button>
        </div>

        <br>
      </fieldset>
    </form>

    <form action="ShipmentUserApp" method="post">
      <fieldset>
        <br>
        <legend>Shipments Record Insert</legend>
        <div class="table-container">
          <table>
            <tr id="lightblueText">
              <th>snum</th>
              <th>pnum</th>
              <th>jnum</th>
              <th>quantity</th>
            </tr>

            <tr>
              <td>
                <input type="text" name="snum" class="cmd">
              </td>
              <td>
                <input type="text" name="pnum" class="cmd">
              </td>
              <td>
                <input type="text" name="jnum" class="cmd">
              </td>
              <td>
                <input type="text" name="quantity" class="cmd">
              </td>
            </tr>
          </table>
        </div>

        <br>

        <div class="button-container">
          <button type="submit" id="greenText" name="shipmentsInsertForm">Enter Job Record Into Database</button>
          <button type="reset" id="redText" onclick={resetInfo()}>Clear Data And Results</button>
        </div>

        <br>
      </fieldset>
    </form>

    <center>
      <p>
      <b>Execution Results:</b><br><br>
        <table id="data">
          <%=message%>
        </table>
      </p>
    </center>


    <br><br><br>

    <!-- Button handling -->
    <script>
      function resetInfo() {
        $("#cmd").val("");
        $("#data").remove();
      }
    </script>
  </body>
</html>
