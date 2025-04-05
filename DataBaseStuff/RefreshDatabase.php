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
      <h1>Order Groups Table</h1>
      <table border="1" cellpadding="2" cellspacing="2">
         <?php
            if(TableExists("Orders",$connection,DB_DATABASE) && TableExists("Inventory",$connection,DB_DATABASE))
            {
               $result = mysqli_query(mysql: $connection, query:"SELECT Orders.OrderGroupID,Orders.Quantity,Orders.CustomerID,Inventory.Name,Account.UserName,Inventory.Price
                From Orders INNER JOIN Inventory ON Orders.InventoryID = Inventory.InventoryID INNER JOIN Account ON Inventory.SellerID = Account.AccountID ORDER BY Orders.CustomerID ASC;");

               $previousGroup = null;
               // Loop through the result set and display the order and inventory details
               while ($row = mysqli_fetch_assoc($result)) {
                   // Check if the order belongs to a new order group
                  if ($previousGroup != $row['OrderGroupID']) {
                     // New group, display group info
                     if ($previousGroup !== null) {
                        echo "</tr>"; // Close the previous group
                        echo "</table>";
                     }
                     echo "<p> customerID :";
                     echo $row['CustomerID'];
                     echo "<p>";
                     echo "<p> OrderGroup :";
                     echo $row['OrderGroupID'];
                     echo "<p>";
                     echo "<table border= '1' cellpadding='2' cellspacing='2' style='margin-bottom: 20px;'>";
                     echo "<tr>";
                     echo "<tr>";
                     echo "<td>";
                     echo "Inventory Name";
                     echo "</td>";
                     echo "<td>";
                     echo "Quantity";
                     echo "</td>";     
                     echo "<td>";
                     echo "Price";
                     echo "</td>";     
                     echo "<td>";
                     echo "SellerName";
                     echo "</td>";     
                     $previousGroup = $row['OrderGroupID'];
                  }    

                  echo "<tr>";
                  echo "<td>" ,$row['Name'] ,"</td>";
                  echo "<td>" ,$row['Quantity'] ,"</td>";
                  echo "<td>" ,$row['Price'], "</td>";
                  echo "<td>" ,$row['UserName'], "</td>";
                  echo "</tr>";
               }
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
      $connection->query("SET GLOBAL max_allowed_packet=64*1024*1024");
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
                AccountID INT(11) PRIMARY KEY AUTO_INCREMENT ,
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
               InventoryID INT(11) PRIMARY KEY AUTO_INCREMENT ,
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
               OrderID INT(11) PRIMARY KEY AUTO_INCREMENT ,
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

      $profileImageFileLoc = "profile/";
      LoadAccount($connection ,$profileImageFileLoc . "profile00.jpg","Emma Natalie Soh","password");
      LoadAccount($connection, $profileImageFileLoc . "profile06.jpg","sponge Inventory","password");
      LoadAccount($connection ,$profileImageFileLoc . "profile01.jpg","Goh Jun Lin, Wayne","password");
      LoadAccount($connection ,$profileImageFileLoc . "profile02.jpg","Guo Chen","password");
      LoadAccount($connection ,$profileImageFileLoc . "profile03.jpg","Kenzie Lim","password");
      LoadAccount($connection ,$profileImageFileLoc . "profile04.jpg","Lee Cjeng, Jacob","password");
      LoadAccount($connection ,$profileImageFileLoc . "profile05.jpg","Trina Lim","password");

      $sofaImageFileLoc = "sofa/";
      $inventoryText = "inventory";
      $SofaName = "sofa";
      $additionalFormatting = "0";

      $sofaDesc = array(
         "A modern blue velvet sofa with a sleek silhouette, ideal for contemporary living rooms.",
         "Minimalist gray fabric sofa with clean lines, perfect for a sophisticated and neutral décor.",
         "Compact mustard-yellow loveseat with a tufted back, great for small apartments or reading nooks.",
         "Cozy white and beige sofa with colorful cushions, adding warmth and comfort to any space.",
         "Vibrant yellow mid-century sofa with wooden legs, bringing energy and style to modern interiors.",
         "Industrial-style black leather sofa with metal legs, ideal for loft spaces and contemporary homes.",
         "Classic brown fabric sofa with plush cushions, offering a timeless and inviting appeal.",
         "Elegant blue velvet sofa with a tufted back, perfect for a luxurious living space.",
         "Simple and modern white sofa, a versatile choice for minimalist and Scandinavian interiors.",
         "Unique green modular floor sofa, great for casual lounging and creative spaces.",
         "Chic pink accent chair with a rounded back, adding a touch of elegance to any corner.",
         "Stylish blue and yellow sectional sofa, offering both comfort and modern aesthetics.",
         "Futuristic gray curved sofa, making a statement in contemporary interiors.",
         "Warm-toned brown and red cushioned sofa, creating a cozy and inviting atmosphere.",
         "Vintage-inspired green velvet sofa with rolled arms, exuding classic elegance.",
         "Sunlit cream-colored sofa with a sleek design, perfect for a bright and airy living room.",
         "Bold orange leather sofa with a minimalist frame, ideal for a modern and stylish home.",
         "Light gray fabric sofa with a contemporary touch, fitting seamlessly into any décor.",
         "Spacious L-shaped sofa in soft gray, offering both comfort and functionality for families.",
         "Luxurious golden draped sofa setting, ideal for elegant and dramatic interiors.",
         "Classic blue sofa with a tufted back and rolled arms, perfect for a refined look.",
         "Rich brown leather Chesterfield sofa, a timeless piece for sophisticated spaces.",
         "Mid-century modern caramel leather sofa with wooden legs, combining retro and contemporary charm.",
         "Deep green velvet sofa with a streamlined design, bringing a touch of luxury to any room.",
         "Elegant dark gray sectional sofa with plush seating, ideal for modern homes.",
         "Cozy beige sofa set in a stylish living room, creating a warm and inviting feel.",
         "Natural wood and fabric sofa with a bohemian vibe, perfect for relaxed interiors.",
         "Dark navy-blue velvet sofa with gold accents, adding a luxurious touch to any setting.",
         "Soft white fabric sofa with a cozy throw, great for a minimalist and serene look.",
         "Neutral beige sofa with modern cushions, a versatile choice for any home style."
     );

      for($i = 0; $i < 30 ;++$i)
      {
         if($i == 10)
         {
            $additionalFormatting = "";
         }
         LoadInventory($connection,$inventoryText . $i ,$sofaDesc[$i],10.00,$sofaImageFileLoc . $SofaName . $additionalFormatting . $i . ".jpg",10,0,2);
      }

      //LoadInventory($connection,"inventory1","description",10.00,"test_image.jpg",10,0,2);
      //LoadInventory($connection,"inventory2","description",10.00,"test_image.jpg",10,0,2);
      //LoadAccount($connection,"test_image.jpg","customer2","password");


      AddOrders($connection,2,1,1,1,1,'2025-03-30 12:00:00');
      AddOrders($connection,2,1,3,1,1,'2025-03-30 12:00:00');
      AddOrders($connection,3,1,1,1,2,'2025-03-30 12:01:00');
      AddOrders($connection,3,1,2,1,2,'2025-03-30 12:01:00');
      AddOrders($connection,4,1,4,1,3,'2025-03-30 12:02:00');
      AddOrders($connection,4,1,5,1,3,'2025-03-30 12:02:00');
      AddOrders($connection,3,1,4,1,4,'2025-03-30 12:03:00');
      AddOrders($connection,3,1,5,1,4,'2025-03-30 12:03:00');
      AddOrders($connection,4,1,10,1,5,'2025-03-30 12:04:00');
      AddOrders($connection,4,1,15,1,5,'2025-03-30 12:04:00');
   }

   function LoadAccount($connection,$_profileImagePath,$_userName,$_password) : void{

      $image = null;
      if (file_exists($_profileImagePath)) {
         $image = file_get_contents($_profileImagePath);
      }
      // Check if file_get_contents was successful
      if ($image === false) {
         $query = "INSERT INTO Account (Username,`Password`) VALUES (?,?);";

         if ($stmt = mysqli_prepare($connection, $query)) {
            // Bind the parameters to the placeholders
            mysqli_stmt_bind_param($stmt, 'ss', $_userName, $_password);
   
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
         $query = "INSERT INTO Account (ProfileImage,Username,`Password`) VALUES ( ?,?,?);";

         if ($stmt = mysqli_prepare($connection, $query)) {
            // Bind the parameters to the placeholders
            mysqli_stmt_bind_param($stmt, 'sss', $image, $_userName, $_password);
   
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
   
   function LoadInventory($_connection,$_name,$_description,$_price,$_imagePath,$_numberInStock,$_numberSold,$_sellerID)
   {
      $image = null;
      if (file_exists($_imagePath)) {
         $image = file_get_contents($_imagePath);
      }
      // Check if file_get_contents was successful
      if ($image === false) {
         // Prepare the SQL query using placeholders
         $query = "INSERT INTO Inventory ( `Name`, `Description`, Price, NumberInStock, SellerID, NumberSold) 
         VALUES ( ?, ?, ?, ?, ?, ?)";

         if ($stmt = mysqli_prepare($_connection, $query)) {
            // Bind the parameters to the placeholders
            mysqli_stmt_bind_param($stmt, 'ssdsis', $_name, $_description, $_price, $_numberInStock, $_sellerID, $_numberSold);

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
            $query = "INSERT INTO Inventory (`Name`, `Description`, Price, `Image`, NumberInStock, SellerID, NumberSold) 
            VALUES ( ?, ?, ?, ?, ?, ?, ?)";

            if ($stmt = mysqli_prepare($_connection, $query)) {
               // Bind the parameters to the placeholders
               mysqli_stmt_bind_param($stmt, 'ssdssis', $_name, $_description, $_price, $image, $_numberInStock, $_sellerID, $_numberSold);

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
   function LoadOrders($_connection,$_customerID,$_sellerID,$_inventoryID,$_quantity,$_orderGroupID,$_timeStamp)
   {
      $query = "INSERT INTO Orders ( `CustomerID`, `SellerID`, InventoryID, `Quantity`,OrderGroupID ,`Timestamp`) 
      VALUES ( ?, ?, ?, ?, ?,?)";

      if ($stmt = mysqli_prepare($_connection, $query)) {
         // Bind the parameters to the placeholders
         mysqli_stmt_bind_param($stmt, 'iiiiis', $_customerID, $_sellerID,$_inventoryID, $_quantity, $_orderGroupID, $_timeStamp);

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

   function AddOrders($_connection,$_customerID,$_sellerID,$_inventoryID,$_quantity,$_orderGroupID,$_timeStamp)
   {
      if(CheckStock($_connection,$_inventoryID,$_quantity))
      {
         LoadOrders($_connection,$_customerID,$_sellerID,$_inventoryID,$_quantity,$_orderGroupID,$_timeStamp);
         SoldInventory($_connection,$_inventoryID,$_quantity);
      }
      else
      {
         echo "not enough stock";
      }
   }
?>