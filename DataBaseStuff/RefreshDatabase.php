<?php include "dbinfo.inc"; ?>
<html>
   <body>
      <h1>RefreshDatabsePage</h1>
      <?php
         $connection = mysqli_connect(hostname: DB_SERVER, username: DB_USERNAME, password: DB_PASSWORD);
         if (mysqli_connect_errno()) {
            echo "Failed to connect to MySQL: " .mysqli_onnecterror();
         }
         $database = mysqli_select_db(mysql: $connection, database: DB_DATABASE);

         if(isset($_POST['loadTables'])) {
            VerifyTables(connection:$connection,dbName:DB_DATABASE);
         }
         if(isset($_POST['dropTables']))
         {
            DropTables(connection:$connection, dbName:DB_DATABASE);
         }
      ?>

      <form method="post">
        <input type="submit" name="loadTables"
                value="Must Load Table uggggh"/>

         <input type="submit" name="dropTables"
            value="Must Drop Tables Ahhhhh"/>
      </form>
         
      </form>

      <!-- Display Account table data -->
      <table border="1" cellpadding="2" cellspacing="2">
         <td>
            AccountID
         </td>
         <td>
            ProfileImage
         </td>
         <td>
            UserName
         </td>
         <td>
            PassWord
         </td>

         <?php
            if(TableExists("Account",$connection,DB_DATABASE))
            {
               $result = mysqli_query(mysql: $connection, query:"SELECT * From Account");
               while($query_data = mysqli_fetch_row($result)) {
                  echo  "<tr>";
                  echo  "<td>",$query_data[0], "</td>",
                        "<td>",$query_data[1], "</td>",
                        "<td>",$query_data[2], "</td>",
                        "<td>",$query_data[3], "</td>";
                  echo "</tr>";
               }
               
	            mysqli_free_result($result);
            }
            if(TableExists("Account",$connection,DB_DATABASE))
            {
               $result = mysqli_query(mysql: $connection, query:"SELECT * From Account");
               while($query_data = mysqli_fetch_row($result)) {
                  echo  "<tr>";
                  echo  "<td>",$query_data[0], "</td>",
                        "<td>",$query_data[1], "</td>",
                        "<td>",$query_data[2], "</td>",
                        "<td>",$query_data[3], "</td>";
                  echo "</tr>";
               }
               
	            mysqli_free_result($result);
            }
         ?>
      </table>
<!-- Clean up. -->
<?php
	  mysqli_close($connection);

?>

</body>
</html>


<?php 
   /* 
      PHP Functions
   */
   function VerifyTables($connection, $dbName) {
      //check whether account table exists
      //if doesnt exist then create
      if(!TableExists(tableName: "Account", connection : $connection, dbName: $dbName))
      {
         CreateAccountTable($connection);
      }
      //check whether inventory table exists
      if(!TableExists(tableName: "Inventory", connection : $connection, dbName: $dbName))
      {
         CreateInventoryTable($connection);
      }
      //check whether inventry table exists
      if(!TableExists(tableName: "Orders", connection : $connection, dbName: $dbName))
      {
         CreateOrdersTable($connection);
      }
   }

   function TableExists($tableName, $connection, $dbName) {
      $t = mysqli_real_escape_string($connection, $tableName);
      $d = mysqli_real_escape_string($connection, $dbName);

      $checktable = mysqli_query($connection,
              "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_NAME = '$t' AND TABLE_SCHEMA = '$d'");

      if(mysqli_num_rows($checktable) > 0) return true;

      return false;
   }

   /* Creation of the account table */
   function CreateAccountTable($connection)
   {
       $query = "
             CREATE TABLE Account (
                AccountID INT(11) PRIMARY KEY,
                ProfileImage VARCHAR(255) NOT NULL,
                Username VARCHAR(20) NOT NULL,
                Password VARCHAR(20) NOT NULL
             )
          ";
          
       if(!mysqli_query($connection, $query)) echo("<p>Error creating table.</p>");
   }
   /* Creation of the Inventory table */
   function CreateInventoryTable($connection)
   {
      $query = "
            CREATE TABLE Inventory (
               InventoryID INT(11) PRIMARY KEY,
               Name VARCHAR(255) NOT NULL,
               Description TEXT,
               Price INT NOT NULL,
               Image VARCHAR(255),
               NumberInStock INT NOT NULL,
               SellerID INT(11) NOT NULL,
               NumberSold INT NOT NULL,
               FOREIGN KEY (SellerID) REFERENCES Account(AccountID)
            )
         ";
         
      if(!mysqli_query($connection, $query)) echo("<p>Error creating table.</p>");
   }
   /* Creation of the orders table */
   function CreateOrdersTable($connection)
   {
      $query = "
            CREATE TABLE Orders (
               OrderID INT(11) PRIMARY KEY,
               CustomerID INT(11) NOT NULL,
               SellerID INT(11) NOT NULL,
               InventoryID INT(11) NOT NULL,
               Quantity INT(10) NOT NULL,
               OrderGroupID INT(11) NOT NULL,
               Timestamp TIMESTAMP NOT NULL,
               FOREIGN KEY (CustomerID) REFERENCES Account(AccountID),
               FOREIGN KEY (SellerID) REFERENCES Account(AccountID),
               FOREIGN KEY (InventoryID) REFERENCES Inventory(InventoryID)
            )
         ";
         
      if(!mysqli_query($connection, $query)) echo("<p>Error creating table.</p>");
   }
   
   function DropTables($connection, $dbName): void
   {   
      mysqli_query($connection, "DROP TABLE IF EXISTS Orders;");
      mysqli_query($connection, "DROP TABLE IF EXISTS Inventory;");
      mysqli_query($connection, "DROP TABLE IF EXISTS Account;");
   }

?>