<!-- 
 * @file    RefreshDatabase.php
 * @author  Goh Jun Lin Wayne
 * @par     Email: 2200628\@sit.singaporetech.edu.sg
 * @par     Course: CSD3156 Mobile and Cloud Computing
 * @par     Project: Cloud Computing Project
 *
 * @brief   This file creates a php page to create and populate a database
 *
 * This database will be displayed in a format so it is easily viewed
-->
<?php include "dbinfo.inc"; ?>
<html>

<body>
   <h1>RefreshDatabsePage</h1>
   <?php
   $connection = mysqli_connect(hostname: DB_SERVER, username: DB_USERNAME, password: DB_PASSWORD);
   if (mysqli_connect_errno()) {
      echo "Failed to connect to MySQL: " . mysqli_onnecterror();
   }
   $database = mysqli_select_db(mysql: $connection, database: DB_DATABASE);

   if (isset($_POST['loadTables'])) {
      VerifyTables(connection: $connection, dbName: DB_DATABASE);
      // Redirect to avoid resubmission on page refresh
      header("Location: " . $_SERVER['PHP_SELF']);
      exit; // Stop further script execution
   }
   if (isset($_POST['dropTables'])) {
      DropTables(connection: $connection, dbName: DB_DATABASE);
      // Redirect to avoid resubmission on page refresh
      header("Location: " . $_SERVER['PHP_SELF']);
      exit; // Stop further script execution
   }
   if (isset($_POST['loadData'])) {
      LoadData(connection: $connection, dbName: DB_DATABASE);
      // Redirect to avoid resubmission on page refresh
      header("Location: " . $_SERVER['PHP_SELF']);
      exit; // Stop further script execution
   }

   if (isset($_POST['testConfirm'])) {
      testConfirm(connection: $connection, dbName: DB_DATABASE);
      // Redirect to avoid resubmission on page refresh
      header("Location: " . $_SERVER['PHP_SELF']);
      exit; // Stop further script execution
   }
   ?>

   <form method="post">
      <input type="submit" name="loadTables" value="Must Load Table uggggh" />

      <input type="submit" name="dropTables" value="Must Drop Tables Ahhhhh" />

      <input type="submit" name="loadData" value="Load Data Yay" />

      <input type="submit" name="testConfirm" value="test Data Yeahhh" />
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
      if (TableExists("Account", $connection, DB_DATABASE)) {
         $result = mysqli_query(mysql: $connection, query: "SELECT * From Account");
         while ($query_data = mysqli_fetch_row($result)) {

            $imageData = $query_data[1];


            echo "<tr>";
            //account id
            echo "<td>", $query_data[0], "</td>";
            //profile image
            if ($imageData == null) {
               echo "<td> NULL </td>";
            } else {
               echo "<td><img src='" . $imageData . "' alt='Image' width='100' height='100'></td>";
            }
            //username
            echo "<td>", $query_data[2], "</td>";
            //password
            echo "<td>", $query_data[3], "</td>";
            echo "</tr>";
         }

         mysqli_free_result($result);
      } else {
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
      if (TableExists("Inventory", $connection, DB_DATABASE)) {
         $result = mysqli_query(mysql: $connection, query: "SELECT * From Inventory");
         while ($query_data = mysqli_fetch_row($result)) {

            $imageData = $query_data[4];

            echo "<tr>";
            echo "<td>", $query_data[0], "</td>",
               "<td>", $query_data[1], "</td>",
               "<td>", $query_data[2], "</td>",
               "<td>", $query_data[3], "</td>";
            if ($imageData == null) {
               echo "<td>NULL</td>";
            } else {
               echo "<td><img src='" . $imageData . "' alt='Image' width='100' height='100'></td>";
            }
            echo "<td>", $query_data[5], "</td>",
               "<td>", $query_data[6], "</td>",
               "<td>", $query_data[7], "</td>";
            echo "</tr>";
         }

         mysqli_free_result($result);
      } else {
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
      <td>
         OrderConfirmed
      </td>
      <?php
      if (TableExists("Orders", $connection, DB_DATABASE)) {
         $result = mysqli_query(mysql: $connection, query: "SELECT * From Orders");
         while ($query_data = mysqli_fetch_row($result)) {
            echo "<tr>";
            echo "<td>", $query_data[0], "</td>",
               "<td>", $query_data[1], "</td>",
               "<td>", $query_data[2], "</td>",
               "<td>", $query_data[3], "</td>",
               "<td>", $query_data[4], "</td>",
               "<td>", $query_data[5], "</td>",
               "<td>", $query_data[6], "</td>",
               "<td>", $query_data[7], "</td>";
            echo "</tr>";
         }

         mysqli_free_result($result);
      } else {
         echo "<h1>";
         echo "no table";
         echo "</h1>";
      }
      ?>
   </table>
   <h1>Order Groups Table</h1>
   <table border="1" cellpadding="2" cellspacing="2">
      <?php
      if (TableExists("Orders", $connection, DB_DATABASE) && TableExists("Inventory", $connection, DB_DATABASE)) {
         $result = mysqli_query(mysql: $connection, query: "SELECT Orders.OrderConfirmed,Orders.OrderGroupID,Orders.Quantity,Orders.CustomerID,Inventory.Name,Account.UserName,Inventory.Price
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
               echo "<td>";
               echo "Order Confirmation Status";
               echo "</td>";
               $previousGroup = $row['OrderGroupID'];
            }

            echo "<tr>";
            echo "<td>", $row['Name'], "</td>";
            echo "<td>", $row['Quantity'], "</td>";
            echo "<td>", $row['Price'], "</td>";
            echo "<td>", $row['UserName'], "</td>";
            echo "<td>", $row['OrderConfirmed'], "</td>";
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
/**
 * Verifies if the required tables exist and creates them if they do not.
 *
 * This function checks the existence of three essential tables (`Account`, `Inventory`, 
 * and `Orders`) in the database. If any of these tables do not exist, the function 
 * triggers the creation of the missing tables using the respective creation functions.
 * It also sets the global MySQL variable `max_allowed_packet` to 64MB to handle larger queries.
 *
 * @param mysqli $_connection The database connection object.
 * @param string $dbName The name of the database to check the tables in.
 *
 * @return void This function does not return any value. It performs table existence checks
 *              and creates missing tables.
 */
function VerifyTables($connection, $dbName)
{
   //check whether account table exists
   //if doesnt exist then create
   if (!TableExists(tableName: "Account", connection: $connection, dbName: $dbName)) {
      CreateAccountTable($connection);
   }
   //check whether inventory table exists
   if (!TableExists(tableName: "Inventory", connection: $connection, dbName: $dbName)) {
      CreateInventoryTable($connection);
   }
   //check whether inventry table exists
   if (!TableExists(tableName: "Orders", connection: $connection, dbName: $dbName)) {
      CreateOrdersTable($connection);
   }
   $connection->query("SET GLOBAL max_allowed_packet=64*1024*1024");
}
/**
 * Checks if a table exists in the specified database.
 *
 * This function queries the `information_schema` to check if a given table exists 
 * in the provided database. It uses `SELECT` to check for the table's existence 
 * and returns a boolean value based on the result.
 *
 * @param string $tableName The name of the table to check.
 * @param mysqli $_connection The database connection object.
 * @param string $dbName The name of the database to check for the table in.
 *
 * @return bool Returns `true` if the table exists, `false` otherwise.
 */
function TableExists($tableName, $connection, $dbName)
{
   $t = mysqli_real_escape_string($connection, $tableName);
   $d = mysqli_real_escape_string($connection, $dbName);

   $checktable = mysqli_query(
      $connection,
      "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_NAME = '$t' AND TABLE_SCHEMA = '$d'"
   );

   if (mysqli_num_rows($checktable) > 0)
      return true;

   return false;
}
/**
 * Creates the `Account` table in the database.
 *
 * This function creates a table named `Account` with the following columns:
 * - `AccountID`: Primary key, auto-increment integer.
 * - `ProfileImage`: Stores the profile image as a VARCHAR.
 * - `Username`: A `VARCHAR(20)` field for the username.
 * - `Password`: A `VARCHAR(20)` field for the user's password.
 * 
 * If the table creation fails, an error message is displayed.
 *
 * @param mysqli $_connection The database connection object.
 *
 * @return void This function does not return any value. It creates the `Account` table
 *              in the database.
 */
function CreateAccountTable($connection)
{
   $query = "
             CREATE TABLE Account (
                AccountID INT(11) PRIMARY KEY AUTO_INCREMENT ,
                ProfileImage VARCHAR(400),
                Username VARCHAR(20) NOT NULL,
                Password VARCHAR(20) NOT NULL
             )
          ";

   if (!mysqli_query($connection, $query))
      echo ("<p>Error creating table.</p>");
}
/**
 * Creates the `Inventory` table in the database.
 *
 * This function creates a table named `Inventory` with the following columns:
 * - `InventoryID`: Primary key, auto-increment integer.
 * - `Name`: The name of the inventory item (`VARCHAR(255)`).
 * - `Description`: A `TEXT` field for the item description.
 * - `Price`: The price of the item (integer).
 * - `Image`: A VARCHAR to store the link to the image of the inventory item.
 * - `NumberInStock`: The number of items available in stock.
 * - `SellerID`: A foreign key referencing the `Account` table's `AccountID`.
 * - `NumberSold`: The number of items sold.
 *
 * If the table creation fails, an error message is displayed.
 *
 * @param mysqli $_connection The database connection object.
 *
 * @return void This function does not return any value. It creates the `Inventory` table
 *              in the database.
 */
function CreateInventoryTable($connection)
{
   $query = "
            CREATE TABLE Inventory (
               InventoryID INT(11) PRIMARY KEY AUTO_INCREMENT ,
               Name VARCHAR(255) NOT NULL,
               Description TEXT,
               Price INT NOT NULL,
               Image VARCHAR(400),
               NumberInStock INT NOT NULL,
               SellerID INT(11) NOT NULL,
               NumberSold INT NOT NULL,
               FOREIGN KEY (SellerID) REFERENCES Account(AccountID)
            )
         ";

   if (!mysqli_query($connection, $query))
      echo ("<p>Error creating table.</p>");
}
/**
 * Creates the `Orders` table in the database.
 *
 * This function creates a table named `Orders` with the following columns:
 * - `OrderID`: Primary key, auto-increment integer.
 * - `CustomerID`: A foreign key referencing the `Account` table's `AccountID`.
 * - `SellerID`: A foreign key referencing the `Account` table's `AccountID`.
 * - `InventoryID`: A foreign key referencing the `Inventory` table's `InventoryID`.
 * - `Quantity`: The quantity of items ordered.
 * - `OrderGroupID`: A unique identifier for the order group.
 * - `Timestamp`: A timestamp for when the order was created.
 *
 * If the table creation fails, an error message is displayed.
 *
 * @param mysqli $_connection The database connection object.
 *
 * @return void This function does not return any value. It creates the `Orders` table
 *              in the database.

*/
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
               OrderConfirmed BOOL NOT NULL,
               FOREIGN KEY (CustomerID) REFERENCES Account(AccountID),
               FOREIGN KEY (SellerID) REFERENCES Account(AccountID),
               FOREIGN KEY (InventoryID) REFERENCES Inventory(InventoryID)
            )
         ";

   if (!mysqli_query($connection, $query))
      echo ("<p>Error creating table.</p>");
}
/**
 * Drops specified tables from the database.
 *
 * This function removes the tables `Orders`, `Inventory`, and `Account` from the database, 
 * if they exist. It uses the `DROP TABLE IF EXISTS` SQL command to ensure that no error 
 * occurs if the tables do not exist. This function is useful for database resets or 
 * cleaning up the schema.
 *
 * @param mysqli $_connection The database connection object.
 * @param string $dbName The name of the database (not directly used in the function 
 *                       but included for future flexibility or modifications).
 *
 * @return void This function does not return any value. It performs the operation 
 *              to drop the specified tables.
 */
function DropTables($connection, $dbName): void
{
   mysqli_query($connection, "DROP TABLE IF EXISTS Orders;");
   mysqli_query($connection, "DROP TABLE IF EXISTS Inventory;");
   mysqli_query($connection, "DROP TABLE IF EXISTS Account;");
}
/**
 * Loads sample account and inventory data into the database.
 *
 * This function populates the database with initial data by creating user accounts 
 * and adding inventory items, specifically sofas. It also creates orders for various 
 * inventory items and associates them with customers and sellers.
 * The data is loaded using predefined profile images and inventory descriptions.
 * 
 * The function performs the following tasks:
 * 1. Loads sample account data with images and user information.
 * 2. Loads sofa inventory data, including names, descriptions, prices, stock, and images.
 * 3. Creates orders for different inventory items and stores them in the Orders table.
 *
 * @param mysqli $_connection The database connection object.
 * @param string $dbName The name of the database being used (not utilized in the function but included as a parameter).
 * 
 * @return void This function does not return any value. It performs multiple insert operations to populate the database.
 */
function LoadData($connection, $dbName): void
{

   $profileImagelinks = array(
      "https://media.discordapp.net/attachments/1205364935429857351/1357978791783301242/profile00.jpg?ex=67f22bcd&is=67f0da4d&hm=51892a5afad7190e7ac58c0c771ad025e350ba3d71482e80a55ca42b7f975198&",
      "https://media.discordapp.net/attachments/1205364935429857351/1357978792051871905/profile01.jpg?ex=67f22bce&is=67f0da4e&hm=97566385ddaff854dc316a48d6a89ba0e71f02d50c95863b86a5b74559740546&",
      "https://media.discordapp.net/attachments/1205364935429857351/1357978792420835421/profile02.jpg?ex=67f22bce&is=67f0da4e&hm=97af7fb2ae6a9d511a2fd79f48d217efb4c22305b04a9fdb79f90335c53b4349&",
      "https://media.discordapp.net/attachments/1205364935429857351/1357978789606588436/profile03.jpg?ex=67f22bcd&is=67f0da4d&hm=6b2a820b0e562ad6e20cfaeccd88d77fa5cb54442f4bb03a98fab649dca514a4&",
      "https://media.discordapp.net/attachments/1205364935429857351/1357978790025891940/profile04.jpg?ex=67f22bcd&is=67f0da4d&hm=fb919f4edef2bd8deef12fafff4abd5214c23bb35ec948b361cc62577a47af49&",
      "https://media.discordapp.net/attachments/1205364935429857351/1357978790579666985/profile05.jpg?ex=67f22bcd&is=67f0da4d&hm=4fdeb8590096fb3b8d88627825e91268d816eafe52613995a7b8c0afe5b00fc8&",
      "https://media.discordapp.net/attachments/1205364935429857351/1357978791200296992/profile06.jpg?ex=67f22bcd&is=67f0da4d&hm=d3ac7c3c4bc68c63a5af5b45a9da2bed3b2bb6d4f6012c6729096dd974434ce6&"
   );

   LoadAccount($connection, $profileImagelinks[0], "Emma Natalie Soh", "password");
   LoadAccount($connection, $profileImagelinks[6], "sponge Inventory", "password");
   LoadAccount($connection, $profileImagelinks[1], "Goh Jun Lin, Wayne", "password");
   LoadAccount($connection, $profileImagelinks[2], "Guo Chen", "password");
   LoadAccount($connection, $profileImagelinks[3], "Kenzie Lim", "password");
   LoadAccount($connection, $profileImagelinks[4], "Lee Cjeng, Jacob", "password");
   LoadAccount($connection, $profileImagelinks[5], "Trina Lim", "password");

   $sofaImageFileLoc = "sofa/";
   $inventoryText = "inventory";
   $SofaName = "sofa";
   $additionalFormatting = "0";

   $sofaImageLink = array(
      "https://media.discordapp.net/attachments/1205364935429857351/1357979857509683330/sofa00.jpg?ex=67f22ccc&is=67f0db4c&hm=1d8ed81220a8013b9eaa33c0d8f3945afb314f299f6405fb51666e5a090664e0&=&format=webp&width=1166&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357979859384795247/sofa01.jpg?ex=67f22ccc&is=67f0db4c&hm=673b21cf6b98dc07c674b572b699d8481de5e90b68014333e1d806139376cb2d&=&format=webp&width=1167&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357979860580176055/sofa02.jpg?ex=67f22ccc&is=67f0db4c&hm=f0b9c99cf0782706e163a0d434b86696b1534cbce4640695fc7870fa3cbca124&=&format=webp&width=623&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357979862203371540/sofa03.jpg?ex=67f22ccd&is=67f0db4d&hm=e046834343993d22b2f607c6a04af4d4adff53c1dfa1d47799528fdac13a3acf&=&format=webp&width=518&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357979863914512455/sofa04.jpg?ex=67f22ccd&is=67f0db4d&hm=32cf5a2d20a9cd72af0735bde81d14f0c22d6efd12e8efe3ad34414ac7db128c&=&format=webp&width=1082&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357980133847339018/sofa05.jpg?ex=67f22d0d&is=67f0db8d&hm=05050d4f67a9d7b82cb502ca094807051661cd2e2b62a6f5b4381270d5afbb80&=&format=webp&width=1174&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357980135801884702/sofa06.jpg?ex=67f22d0e&is=67f0db8e&hm=6be399e3e42042ae678022bef99bfafe5f209017d3186895e5d3eadff79b55ed&=&format=webp&width=1167&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357980136930017440/sofa07.jpg?ex=67f22d0e&is=67f0db8e&hm=33c26af6702acad057c3565ff70236083227edaf5e43d47a33760ae4ef8435d3&=&format=webp&width=1167&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357980137605304521/sofa08.jpg?ex=67f22d0e&is=67f0db8e&hm=2c7cdadcc272c8ff7b4ed8b1d1ec4564b089f0dd80f6b97ed0e1165232396506&=&format=webp&width=1383&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357980138842886205/sofa09.jpg?ex=67f22d0f&is=67f0db8f&hm=03298aa95842953d2c8ff856668c0f8dcf02499a93254e62c089c635986f332e&=&format=webp&width=1167&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357980132379197560/sofa10.jpg?ex=67f22d0d&is=67f0db8d&hm=6d6a570525444d7fd763d128cd51e4afb390b5a0b30d7bad46d0112d2ba91f45&=&format=webp&width=518&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357980630826094863/sofa11.jpg?ex=67f22d84&is=67f0dc04&hm=ea29deb506027079bb0e49c08777f1bfd7cf12adb1b131225f2b128bf384fc8b&=&format=webp&width=1018&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357980632180850708/sofa12.jpg?ex=67f22d84&is=67f0dc04&hm=8bc772d831a49a4641348779413d6cbea0df82932d1b54e31070f8d307eceb7b&=&format=webp&width=1167&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357980633829216357/sofa13.jpg?ex=67f22d85&is=67f0dc05&hm=6e67dfd99b056923cc9cb2b0e1296f2ffb143afdea7843feeddfb5dc008ca3ae&=&format=webp&width=1167&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357980635393687673/sofa14.jpg?ex=67f22d85&is=67f0dc05&hm=3cb9746b33b515be8b2dc0db3689ef3c06a078ba7ddb206629b73d1a076548b9&=&format=webp&width=1038&height=778",
      "https://cdn.discordapp.com/attachments/1205364935429857351/1357980636215775332/sofa15.jpg?ex=67f22d85&is=67f0dc05&hm=935f9343f7a5c377be4ec800e982c1af9267c313e12f0cf65478c817f97a390f&=&format=webp&width=1383&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357980882807427103/sofa16.jpg?ex=67f22dc0&is=67f0dc40&hm=323650b596068442b72cac84db768d01269ec5a40bfd3382a9ceb954a34304ff&=&format=webp&width=1038&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357980884069908550/sofa17.jpg?ex=67f22dc0&is=67f0dc40&hm=6b8721900f9ec89356e4eb205a3d4cf8015e2830ce2ad570548a1310987792ed&=&format=webp&width=1163&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357980886443753563/sofa18.jpg?ex=67f22dc1&is=67f0dc41&hm=2ee50d93b09e031154f1dd5fe8345d0326ae8222229fdcec394130b477c24a03&=&format=webp&width=1167&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357980881641406484/sofa19.jpg?ex=67f22dc0&is=67f0dc40&hm=4f6a4201d0eb1057ac35a44dca1ab066965a799559e91412af742216da51d56d&=&format=webp&width=518&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357981250857734154/sofa20.jpg?ex=67f22e18&is=67f0dc98&hm=041d88d8f2f805d4fd54cb3bf488e19da523ca382f25bdfbb63b930606f343ac&=&format=webp&width=1172&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357981251973415064/sofa21.jpg?ex=67f22e18&is=67f0dc98&hm=ca486394c207d9214a7b9f4a6eefe6f656054ca4009f77fe87018f60f126564c&=&format=webp&width=1038&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357981252690509876/sofa22.jpg?ex=67f22e18&is=67f0dc98&hm=0da266513656f500a48c96dbd1b25dd0179271110cea3509d483ef189cb98673&=&format=webp&width=778&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357981254074630194/sofa23.jpg?ex=67f22e19&is=67f0dc99&hm=1b6c31161e203ebe86dd74702bba49879f1fd4b26af4d582e5efa03b395c2dff&=&format=webp&width=1167&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357981254892523541/sofa24.jpg?ex=67f22e19&is=67f0dc99&hm=20994bde08cbbac1c9d826463a8b3be1b5581742604ca13529c574b7122a25e2&=&format=webp&width=623&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357981256482291783/sofa25.jpg?ex=67f22e19&is=67f0dc99&hm=e2447b0fbbdc53878350a127a93893a47c2d1a206dd11a3d4c2e081bcb897c74&=&format=webp&width=1063&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357981258126196898/sofa26.jpg?ex=67f22e1a&is=67f0dc9a&hm=669650108ded2e54d5c5d37ead8662a716389a3fcc603471a626fca33539f910&=&format=webp&width=778&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357981259271508128/sofa27.jpg?ex=67f22e1a&is=67f0dc9a&hm=0eaf77201b05061e4e4933a332f360bc3f7014024c28094f52aee82673b3c9d4&=&format=webp&width=1085&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357981261204947075/sofa28.jpg?ex=67f22e1a&is=67f0dc9a&hm=ffe4e616385125c4f0efc47b0deb722f21ad8d35a4e8b68502e770dba5b25fcf&=&format=webp&width=1167&height=778",
      "https://media.discordapp.net/attachments/1205364935429857351/1357981249993441361/sofa29.jpg?ex=67f22e18&is=67f0dc98&hm=3f41e8c99db45bd37d9ad4ba377530a6ddc208eaea2fc92defd0e2336e61a156&=&format=webp&width=583&height=778"
   );

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

   $sofaNames = array(
      "Coastal Blue Velvet Sofa",
      "Urban Gray Minimalist Sofa",
      "Sunbeam Mustard Loveseat",
      "Harmony Two-Tone Cushion Sofa",
      "Zest Yellow Mid-Century Sofa",
      "Metro Black Leather Lounge",
      "Amber Classic Fabric Sofa",
      "Royal Blue Tufted Sofa",
      "Nordic White Modern Sofa",
      "Olive Plush Floor Lounger",
      "Blush Pink Accent Chair",
      "Skyline Sectional Sofa",
      "Galaxy Gray Statement Sofa",
      "Rustic Red & Brown Sofa",
      "Emerald Roll-Arm Velvet Sofa",
      "Cream Sunlit Modern Sofa",
      "Tangerine Leather Statement Sofa",
      "Cloud Gray Contemporary Sofa",
      "Family Comfort L-Shaped Sofa",
      "Golden Luxe Draped Sofa",
      "Vintage Blue Tufted Sofa",
      "Regal Chesterfield Leather Sofa",
      "Cognac Mid-Century Sofa",
      "Forest Green Velvet Lounge",
      "Slate Gray Sectional Sofa",
      "Warm Beige Living Set",
      "Boho Natural Frame Sofa",
      "Navy Luxe Velvet Sofa",
      "Ivory Minimalist Sofa",
      "Neutral Chic Cushion Sofa"
   );


   for ($i = 0; $i < 30; ++$i) {
      if ($i == 10) {
         $additionalFormatting = "";
      }
      LoadInventory($connection, $sofaNames[$i], $sofaDesc[$i], 10.00, $sofaImageLink[$i], 10, 0, 2);
   }

   //LoadInventory($connection,"inventory1","description",10.00,"test_image.jpg",10,0,2);
   //LoadInventory($connection,"inventory2","description",10.00,"test_image.jpg",10,0,2);
   //LoadAccount($connection,"test_image.jpg","customer2","password");


   AddOrders($connection, 2, 1, 1, 1, 1, '2025-03-30 12:00:00');
   AddOrders($connection, 2, 1, 3, 1, 1, '2025-03-30 12:00:00');
   AddOrders($connection, 3, 1, 1, 1, 2, '2025-03-30 12:01:00');
   AddOrders($connection, 3, 1, 2, 1, 2, '2025-03-30 12:01:00');
   AddOrders($connection, 4, 1, 4, 1, 3, '2025-03-30 12:02:00');
   AddOrders($connection, 4, 1, 5, 1, 3, '2025-03-30 12:02:00');
   AddOrders($connection, 3, 1, 4, 1, 4, '2025-03-30 12:03:00');
   AddOrders($connection, 3, 1, 5, 1, 4, '2025-03-30 12:03:00');
   AddOrders($connection, 4, 1, 10, 1, 5, '2025-03-30 12:04:00');
   AddOrders($connection, 4, 1, 15, 1, 5, '2025-03-30 12:04:00');

   LoadOrders($connection, 4, 1, 15,10,6, '2025-03-30 12:04:00', false);
}

function testConfirm($connection, $dbName) : void{
   ConfirmOrderGroup($connection,6);
}
/**
 * Loads the account into database
 *
 * This function takes in the database connection, profile image path,
 * user name, and password as parameters and Adds the account data.
 *
 * @param mysqli $connection The database connection object.
 * @param string $_profileImagePath The file path to the user's profile image.
 * @param string $_userName The username for the account.
 * @param string $_password The password associated with the account.
 * 
 * @return void This function does not return any value.
 */
function LoadAccount($connection, $_profileImagePath, $_userName, $_password): void
{

   $query = "INSERT INTO Account (ProfileImage,Username,`Password`) VALUES ( ?,?,?);";

   if ($stmt = mysqli_prepare($connection, $query)) {
      // Bind the parameters to the placeholders
      mysqli_stmt_bind_param($stmt, 'sss', $_profileImagePath, $_userName, $_password);

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
/**
 * Loads inventory data into the database.
 *
 * This function takes in inventory item details such as name, description, price, image path, 
 * number in stock, number sold, and seller ID. It checks if the image exists and, if so, stores 
 * the image in the database along with the rest of the inventory information. If no image is provided, 
 * it inserts the inventory data without an image.
 *
 * @param mysqli $_connection The database connection object.
 * @param string $_name The name of the inventory item.
 * @param string $_description The description of the inventory item.
 * @param float $_price The price of the inventory item.
 * @param string $_imagePath The file path to the inventory item's image.
 * @param int $_numberInStock The number of items in stock.
 * @param int $_numberSold The number of items sold.
 * @param int $_sellerID The ID of the seller associated with the inventory item.
 * 
 * @return void This function does not return any value. It directly inserts data into the database.
 */
function LoadInventory($_connection, $_name, $_description, $_price, $_imagePath, $_numberInStock, $_numberSold, $_sellerID)
{

   // Prepare the SQL query using placeholders
   $query = "INSERT INTO Inventory (`Name`, `Description`, Price, `Image`, NumberInStock, SellerID, NumberSold) 
      VALUES ( ?, ?, ?, ?, ?, ?, ?)";

   if ($stmt = mysqli_prepare($_connection, $query)) {
      // Bind the parameters to the placeholders
      mysqli_stmt_bind_param($stmt, 'ssdssis', $_name, $_description, $_price, $_imagePath, $_numberInStock, $_sellerID, $_numberSold);

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

/**
 * Inserts a new order into the Orders table.
 *
 * This function takes in details about a customer's order, including customer ID, seller ID, 
 * inventory ID, quantity, order group ID, and timestamp, then inserts this information into 
 * the Orders table in the database.
 *
 * @param mysqli $_connection The database connection object.
 * @param int $_customerID The ID of the customer making the order.
 * @param int $_sellerID The ID of the seller associated with the order.
 * @param int $_inventoryID The ID of the inventory item being ordered.
 * @param int $_quantity The quantity of the item being ordered.
 * @param int $_orderGroupID The group ID to associate orders within a group.
 * @param bool $_orderConfirmed The boolean if the order is confirmed
 * @param string $_timeStamp The timestamp when the order was placed.
 * 
 * @return void This function does not return any value. It performs an insert operation.
 */
function LoadOrders($_connection, $_customerID, $_sellerID, $_inventoryID, $_quantity, $_orderGroupID, $_timeStamp, $_orderConfirmed)
{
   $query = "INSERT INTO Orders ( `CustomerID`, `SellerID`, InventoryID, `Quantity`,OrderGroupID ,`Timestamp`,`OrderConfirmed`) 
      VALUES ( ?, ?, ?, ?, ?,?,?)";

   if ($stmt = mysqli_prepare($_connection, $query)) {
      // Bind the parameters to the placeholders
      mysqli_stmt_bind_param($stmt, 'iiiiisi', $_customerID, $_sellerID, $_inventoryID, $_quantity, $_orderGroupID, $_timeStamp, $_orderConfirmed);

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
/**
 * Checks if the required quantity of an item is available in stock.
 *
 * This function queries the Inventory table to retrieve the current stock quantity 
 * for a specific inventory item and compares it with the requested quantity.
 * It returns true if enough stock is available, otherwise false.
 *
 * @param mysqli $_connection The database connection object.
 * @param int $_inventoryID The ID of the inventory item to check stock for.
 * @param int $_quantity The quantity requested by the customer.
 * 
 * @return bool Returns true if the requested quantity is available, false otherwise.
 */
function CheckStock($_connection, $_inventoryID, $_quantity)
{
   $query = "SELECT NumberInStock FROM Inventory WHERE InventoryID = ?";
   if ($stmt = mysqli_prepare($_connection, $query)) {
      mysqli_stmt_bind_param($stmt, 'i', $_inventoryID);
      mysqli_stmt_execute($stmt);
      mysqli_stmt_bind_result($stmt, $availableQuantity);
      mysqli_stmt_fetch($stmt);
      mysqli_stmt_close($stmt);
      if ($availableQuantity < $_quantity) {
         return false;
      }
      return true;
   }
   echo "error checking inventory";
   return false;
}
/**
 * Updates the inventory and sold quantity when an item is sold.
 *
 * This function decreases the inventory stock and increases the number of items sold
 * in the Inventory table based on the quantity of items sold.
 *
 * @param mysqli $_connection The database connection object.
 * @param int $_inventoryID The ID of the inventory item being sold.
 * @param int $_quantity The quantity of the item being sold.
 * 
 * @return void This function does not return any value. It updates the inventory data.
 */
function SoldInventory($_connection, $_inventoryID, $_quantity): void
{
   ChangeStock($_connection, $_inventoryID, -$_quantity);
   ChangeInSold($_connection, $_inventoryID, $_quantity);
}
/**
 * Updates the stock quantity of an inventory item.
 *
 * This function adjusts the number of items in stock by a specified change amount 
 * (positive or negative) for a given inventory item in the Inventory table.
 *
 * @param mysqli $_connection The database connection object.
 * @param int $_inventoryID The ID of the inventory item to update.
 * @param int $_changeInStock The change in stock quantity (can be positive or negative).
 * 
 * @return void This function does not return any value. It updates the inventory stock.
 */
function ChangeStock($_connection, $_inventoryID, $_changeInStock): void
{
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
/**
 * Updates the number of items sold for a specific inventory item.
 *
 * This function increments the number of items sold in the Inventory table for a 
 * given inventory item by a specified quantity.
 *
 * @param mysqli $_connection The database connection object.
 * @param int $_inventoryID The ID of the inventory item to update.
 * @param int $_changeInSold The change in the number of items sold (should be positive).
 * 
 * @return void This function does not return any value. It updates the sold quantity.
 */
function ChangeInSold($_connection, $_inventoryID, $_changeInSold): void
{
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

/**
 * Adds a new order if there is sufficient stock, and updates inventory.
 *
 * This function first checks if there is enough stock available for the requested item. 
 * If there is enough stock, it loads the order into the database and then updates the 
 * inventory by adjusting the stock and the number of items sold.
 *
 * @param mysqli $_connection The database connection object.
 * @param int $_customerID The ID of the customer placing the order.
 * @param int $_sellerID The ID of the seller for the order.
 * @param int $_inventoryID The ID of the inventory item being ordered.
 * @param int $_quantity The quantity of the item being ordered.
 * @param int $_orderGroupID The ID of the order group for grouping related orders.
 * @param string $_timeStamp The timestamp of when the order is placed.
 * 
 * @return void This function does not return any value. It performs order processing and inventory updates.
 */
function AddOrders($_connection, $_customerID, $_sellerID, $_inventoryID, $_quantity, $_orderGroupID, $_timeStamp)
{
   if (CheckStock($_connection, $_inventoryID, $_quantity)) {
      LoadOrders($_connection, $_customerID, $_sellerID, $_inventoryID, $_quantity, $_orderGroupID, $_timeStamp, true);
      SoldInventory($_connection, $_inventoryID, $_quantity);
   } else {
      echo "not enough stock";
   }
}

function ConfirmOrderGroup($_connection ,$_orderGroup)
{
   $query = "SELECT
   Inventory.InventoryID, 
   Inventory.NumberInStock,
   Orders.Quantity,
   Orders.OrderGroupID,
   Orders.OrderConfirmed
   FROM Inventory 
   INNER JOIN Orders ON Orders.InventoryID = Inventory.InventoryID
   WHERE Orders.OrderGroupID = ?";

   $stmt = $_connection->prepare($query);
   $stmt->bind_param("i", $_orderGroup);
   $stmt->execute();
   $result = $stmt->get_result();
   //check if all number in stock < quantity
   while ($query_data = mysqli_fetch_row($result)) {
      if($query_data[1] < $query_data[2])
      {
         return;
      }
   }
   mysqli_data_seek($result, 0);  // Reset result set pointer for the second loop

   //update data
   while ($query_data = mysqli_fetch_row($result)) {
      SoldInventory($_connection, $query_data[0], $query_data[2]);
      $query = "UPDATE Orders SET OrderConfirmed = true WHERE Orders.OrderGroupID = ?";
      $stmt = $_connection->prepare($query);
      $stmt->bind_param("i", $_orderGroup);
      $stmt->execute();
   }

}
?>