<?php
include "dbinfo.inc";
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST, GET, OPTIONS");
header("Content-Type: application/x-www-form-urlencoded");

// Preflight check: respond early to OPTIONS
//if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
//   http_response_code(200); // Send HTTP OK
//   exit(); // Stop further execution
//}

// For POST/GET: respond with 200 if needed
//if ($_SERVER['REQUEST_METHOD'] === 'POST' || $_SERVER['REQUEST_METHOD'] === 'GET') {
//   http_response_code(200); // This is optional — most servers default to 200 if not error
//}

$ID = $_POST['ID'] ?? null;                   //seller id
$Name = $_POST['Name'] ?? null;               //inventoryName
$Desc = $_POST['Desc'] ?? null;               //Description
$Quant = $_POST['Quant'] ?? null;              //Quantity
$Price = $_POST['Price'] ?? null;              //Price
$ImagePath = $_POST['ImagePath'] ?? null;      //ImagePath

$connection = mysqli_connect(hostname: DB_SERVER, username: DB_USERNAME, password: DB_PASSWORD);
if (mysqli_connect_errno()) {
   echo "Failed to connect to MySQL: " . mysqli_onnecterror();
}
$database = mysqli_select_db(mysql: $connection, database: DB_DATABASE);


function TableExists($tableName, $connection, $dbName)
{
   $t = mysqli_real_escape_string($connection, $tableName);
   $d = mysqli_real_escape_string($connection, $dbName);

   $checktable = mysqli_query(
      $connection,
      "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_NAME = '$t' AND TABLE_SCHEMA = '$d'"
   );

   if (mysqli_num_rows($checktable) > 0) return true;

   return false;
}

// Prepare the SQL query using placeholders
$query = "INSERT INTO Inventory (`Name`, `Description`, Price, `Image`, NumberInStock, SellerID, NumberSold)
      VALUES ( ?, ?, ?, ?, ?, ?, ?)";

$_name = $Name;
$_description = $Desc;
$_price = $Price;
$_imagePath = $ImagePath;
$_numberInStock = $Quant;
$_sellerID = $ID;
$_numberSold = 0;


if ($stmt = mysqli_prepare($connection, $query)) {
   // Bind the parameters to the placeholders
   mysqli_stmt_bind_param(
      $stmt,
      'ssisiii',
      $_name,
      $_description,
      $_price,
      $_imagePath,
      $_numberInStock,
      $_sellerID,
      $_numberSold
   );   

   if (mysqli_stmt_execute($stmt)) {
      echo json_encode([
         "success" => true,
         "message" => "Image and data uploaded successfully.",
      ]);
   } else {
      echo json_encode([
         "success" => false,
         "message" => "failed upload.",
      ]);
   }
}

?>