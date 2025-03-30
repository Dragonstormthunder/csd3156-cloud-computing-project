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
            // Redirect to avoid resubmission on page refresh
            header("Location: " . $_SERVER['PHP_SELF']);
            exit; // Stop further script execution
         }
         if(isset($_POST['dropTables']))
         {
            DropTables(connection:$connection, dbName:DB_DATABASE);
            // Redirect to avoid resubmission on page refresh
            header("Location: " . $_SERVER['PHP_SELF']);
            exit; // Stop further script execution
         }
         if(isset($_POST['loadData']))
         {
            LoadData(connection:$connection, dbName:DB_DATABASE);
            // Redirect to avoid resubmission on page refresh
            header("Location: " . $_SERVER['PHP_SELF']);
            exit; // Stop further script execution
         }
      ?>

      <form method="post">
        <input type="submit" name="loadTables"
                value="Must Load Table uggggh"/>

         <input type="submit" name="dropTables"
            value="Must Drop Tables Ahhhhh"/>
         
         <input type="submit" name="loadData"
            value="Load Data Yay"/>
      </form>
         
      </form>

      <!-- Display Account table data -->
      <h1>Account Table</h1>
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
                  
                  $imageData = $query_data[1];

                  
                  echo  "<tr>";
                  //account id
                  echo  "<td>",$query_data[0], "</td>";
                  //profile image
                  if($imageData == null)
                  {
                     echo "<td> NULL </td>";
                  }
                  else
                  {
                     $base64Image = base64_encode($imageData);
                     echo  "<td><img src='data:image/jpeg;base64," . $base64Image . "' alt='Image' width='100' height='100'></td>";
                  }
                  //username
                  echo  "<td>",$query_data[2], "</td>"; 
                  //password
                  echo  "<td>",$query_data[3], "</td>";
                  echo  "</tr>";
               }
               
	            mysqli_free_result($result);
            }
            else
            {
               echo "<h1>";
               echo "no table";
               echo "</h1>";
            }
         ?>
      </table>
      <!-- Display Inventory table data -->
      <h1>Inventory Table</h1>
      <table border="1" cellpadding="2" cellspacing="2">
         <td>
            InventoryID
         </td>
         <td>
            Name
         </td>
         <td>
            Description
         </td>
         <td>
            Price
         </td>
         <td>
            Image
         </td>
         <td>
            Number in stock
         </td>
         <td>
            Seller ID
         </td>
         <td>
            Number Sold
         </td>

         <?php
            if(TableExists("Inventory",$connection,DB_DATABASE))
            {
               $result = mysqli_query(mysql: $connection, query:"SELECT * From Inventory");
               while($query_data = mysqli_fetch_row($result)) {

                  $imageData = $query_data[4];

                  echo  "<tr>";
                  echo  "<td>",$query_data[0], "</td>",
                        "<td>",$query_data[1], "</td>",
                        "<td>",$query_data[2], "</td>",
                        "<td>",$query_data[3], "</td>";
                        if($imageData == null)
                        {
                           echo  "<td>NULL</td>";
                        }
                        else
                        {
                           $base64Image = base64_encode($imageData);
                           echo  "<td><img src='data:image/jpeg;base64," . $base64Image . "' alt='Image' width='100' height='100'></td>";
                        }
                  echo  "<td>",$query_data[5], "</td>",
                        "<td>",$query_data[6], "</td>",
                        "<td>",$query_data[7], "</td>";
                  echo "</tr>";
               }
               
	            mysqli_free_result($result);
            }
            else
            {
               echo "<h1>";
               echo "no table";
               echo "</h1>";
            }

         ?>
      </table>
      <!-- Display Orders table data -->
      <h1>Orders Table</h1>
      <table border="1" cellpadding="2" cellspacing="2">
         <td>
            OrderID
         </td>
         <td>
            CustomerID
         </td>
         <td>
            SellerID
         </td>
         <td>
            InventoryID
         </td>
         <td>
            Quantity
         </td>
         <td>
            OrderGroupID
         </td>
         <td>
            Timestamp
         </td>
         <?php
            if(TableExists("Orders",$connection,DB_DATABASE))
            {
               $result = mysqli_query(mysql: $connection, query:"SELECT * From Orders");
               while($query_data = mysqli_fetch_row($result)) {
                  echo  "<tr>";
                  echo  "<td>",$query_data[0], "</td>",
                        "<td>",$query_data[1], "</td>",
                        "<td>",$query_data[2], "</td>",
                        "<td>",$query_data[3], "</td>",
                        "<td>",$query_data[4], "</td>",
                        "<td>",$query_data[5], "</td>",
                        "<td>",$query_data[6], "</td>";
                  echo "</tr>";
               }
               
	            mysqli_free_result($result);
            }
            else
            {
               echo "<h1>";
               echo "no table";
               echo "</h1>";
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
                ProfileImage LONGBLOB,
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
               Image LONGBLOB,
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

   function LoadData($connection,$dbName): void{
      LoadAccount($connection, 1,"test_image.jpg","seller1","password");
      LoadAccount($connection, 2,"test_image.jpg","customer1","password");
      LoadInventory($connection,1,"inventory1","description",10.00,"test_image.jpg",10,0,2);
      LoadInventory($connection,2,"inventory2","description",10.00,"test_image.jpg",10,0,2);
      LoadAccount($connection, 3,"test_image.jpg","customer2","password");
      AddOrders($connection,1,2,1,1,1,1,'2025-03-30 12:00:00');
      AddOrders($connection,2,3,1,1,1,2,'2025-03-30 12:01:00');
      AddOrders($connection,3,3,1,2,1,2,'2025-03-30 12:01:00');
   }

   function LoadAccount($connection ,$_id,$_profileImagePath,$_userName,$_password) : void{

      $image = file_get_contents($_profileImagePath);
      // Check if file_get_contents was successful
      if ($image === false) {
         $query = "INSERT INTO Account (AccountID,Username,`Password`) VALUES (?,?,?);";

         if ($stmt = mysqli_prepare($connection, $query)) {
            // Bind the parameters to the placeholders
            mysqli_stmt_bind_param($stmt, 'iss', $_id, $_userName, $_password);
   
            // Execute the statement
            if (mysqli_stmt_execute($stmt)) {
               echo "<p>Data added successfully!</p>";
            } else {
               echo "<p>Error executing the query: " . mysqli_error($connection) . "</p>";
            }
   
            // Close the prepared statement
            mysqli_stmt_close($stmt);
         }
      } else {
         $query = "INSERT INTO Account (AccountID, ProfileImage,Username,`Password`) VALUES (?, ?,?,?);";

         if ($stmt = mysqli_prepare($connection, $query)) {
            // Bind the parameters to the placeholders
            mysqli_stmt_bind_param($stmt, 'isss', $_id, $image, $_userName, $_password);
   
            // Execute the statement
            if (mysqli_stmt_execute($stmt)) {
               echo "<p>Data added successfully!</p>";
            } else {
               echo "<p>Error executing the query: " . mysqli_error($connection) . "</p>";
            }
   
            // Close the prepared statement
            mysqli_stmt_close($stmt);
         }
      }
   }
   
   function LoadInventory($_connection,$_inventoryID,$_name,$_description,$_price,$_imagePath,$_numberInStock,$_numberSold,$_sellerID)
   {
      $image = file_get_contents($_imagePath);
      // Check if file_get_contents was successful
      if ($image === false) {
         // Prepare the SQL query using placeholders
         $query = "INSERT INTO Inventory (InventoryID, `Name`, `Description`, Price, NumberInStock, SellerID, NumberSold) 
         VALUES (?, ?, ?, ?, ?, ?, ?)";

         if ($stmt = mysqli_prepare($_connection, $query)) {
            // Bind the parameters to the placeholders
            mysqli_stmt_bind_param($stmt, 'issdsis', $_inventoryID, $_name, $_description, $_price, $_numberInStock, $_sellerID, $_numberSold);

            // Execute the statement
            if (mysqli_stmt_execute($stmt)) {
               echo "<p>Data added successfully!</p>";
            } else {
               echo "<p>Error executing the query: " . mysqli_error($_connection) . "</p>";
            }

            // Close the prepared statement
            mysqli_stmt_close($stmt);
         }
      } else {
            // Prepare the SQL query using placeholders
            $query = "INSERT INTO Inventory (InventoryID, `Name`, `Description`, Price, `Image`, NumberInStock, SellerID, NumberSold) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            if ($stmt = mysqli_prepare($_connection, $query)) {
               // Bind the parameters to the placeholders
               mysqli_stmt_bind_param($stmt, 'issdssis', $_inventoryID, $_name, $_description, $_price, $image, $_numberInStock, $_sellerID, $_numberSold);

               // Execute the statement
               if (mysqli_stmt_execute($stmt)) {
                  echo "<p>Data added successfully!</p>";
               } else {
                  echo "<p>Error executing the query: " . mysqli_error($_connection) . "</p>";
               }

               // Close the prepared statement
               mysqli_stmt_close($stmt);
            }
      }
   }

   //manually insert orders
   function LoadOrders($_connection, $_orderID,$_customerID,$_sellerID,$_inventoryID,$_quantity,$_orderGroupID,$_timeStamp)
   {
      $query = "INSERT INTO Orders (OrderID, `CustomerID`, `SellerID`, InventoryID, `Quantity`,OrderGroupID ,`Timestamp`) 
      VALUES (?, ?, ?, ?, ?, ?,?)";

      if ($stmt = mysqli_prepare($_connection, $query)) {
         // Bind the parameters to the placeholders
         mysqli_stmt_bind_param($stmt, 'iiiiiis', $_orderID, $_customerID, $_sellerID,$_inventoryID, $_quantity, $_orderGroupID, $_timeStamp);

         // Execute the statement
         if (mysqli_stmt_execute($stmt)) {
            echo "<p>Data added successfully!</p>";
         } else {
            echo "<p>Error executing the query: " . mysqli_error($_connection) . "</p>";
         }

         // Close the prepared statement
         mysqli_stmt_close($stmt);
      }
   }

   function CheckStock($_connection,$_inventoryID,$_quantity)
   {
      $query = "SELECT NumberInStock FROM Inventory WHERE InventoryID = ?";
      if ($stmt = mysqli_prepare($_connection, $query)) {
         mysqli_stmt_bind_param($stmt, 'i', $_inventoryID);
         mysqli_stmt_execute($stmt);
         mysqli_stmt_bind_result($stmt, $availableQuantity);
         mysqli_stmt_fetch($stmt);
         mysqli_stmt_close($stmt);
         if($availableQuantity < $_quantity)
         {
            return false;
         }
         return true;
      }
      echo "error checking inventory";
      return false;
   }
   
   function SoldInventory($_connection,$_inventoryID,$_quantity): void
   {
      ChangeStock($_connection,$_inventoryID,-$_quantity);
      ChangeInSold($_connection,$_inventoryID,$_quantity);
   }
   function ChangeStock($_connection,$_inventoryID,$_changeInStock):void{
      $query = "UPDATE Inventory SET NumberInStock = NumberInStock + ? WHERE InventoryID = ?";
      if ($updateStmt = mysqli_prepare($_connection, $query)) {
         mysqli_stmt_bind_param($updateStmt, 'ii', $_changeInStock, $_inventoryID);
         if (mysqli_stmt_execute($updateStmt)) {
             echo "Inventory updated successfully.";
         } else {
             echo "Error updating inventory.";
         }
         mysqli_stmt_close($updateStmt);
      }
   }

   function ChangeInSold($_connection,$_inventoryID,$_changeInSold):void{
      $query = "UPDATE Inventory SET NumberSold = NumberSold + ? WHERE InventoryID = ?";
      if ($updateStmt = mysqli_prepare($_connection, $query)) {
         mysqli_stmt_bind_param($updateStmt, 'ii', $_changeInSold, $_inventoryID);
         if (mysqli_stmt_execute($updateStmt)) {
             echo "Inventory updated successfully.";
         } else {
             echo "Error updating inventory.";
         }
         mysqli_stmt_close($updateStmt);
      }
   }

   function AddOrders($_connection, $_orderID,$_customerID,$_sellerID,$_inventoryID,$_quantity,$_orderGroupID,$_timeStamp)
   {
      if(CheckStock($_connection,$_inventoryID,$_quantity))
      {
         LoadOrders($_connection,$_orderID,$_customerID,$_sellerID,$_inventoryID,$_quantity,$_orderGroupID,$_timeStamp);
         SoldInventory($_connection,$_inventoryID,$_quantity);
      }
      else
      {
         echo "not enough stock";
      }
   }
?>