<?php
include "dbinfo.inc";
header("Access-Control-Allow-Origin: * " );
header("Access-Control-Allow-Methods: POST");
header("Content-Type: application/json");

$ID = isset($_GET['ID']) ? $_GET['ID'] : null;                       //seller id
$Name = isset($_GET['Name']) ? $_GET['Name'] : null;                 //inventoryName
$Desc = isset($_GET['Desc']) ? $_GET['Desc'] : null;                 //Description
$Quant = isset($_GET['Quant']) ? $_GET['Quant'] : null;              //Quantity
$Price = isset($_GET['Price']) ? $_GET['Price'] : null;              //Price
$ImagePath = isset($_GET['ImagePath']) ? $_GET['ImagePath'] : null;  //ImagePath

$image = $_FILES['image'];
if ($image['error'] !== UPLOAD_ERR_OK) {
    echo json_encode(["success" => false, "error" => "Image upload error."]);
    exit;
}

$fileTmpPath = $image['tmp_name'];
$fileName = preg_replace("/[^a-zA-Z0-9\._-]/", "_", $image['name']);
$fileSize = $image['size'];
$fileType = $image['type'];
$uploadDir = 'uploads/';
$uploadPath = $uploadDir . $fileName;

if (!is_dir($uploadDir)) {
    mkdir($uploadDir, 0755, true);
}

if (!move_uploaded_file($fileTmpPath, $uploadPath)) {
    echo json_encode(["success" => false, "error" => "Failed to move uploaded file."]);
    exit;
}


$connection = mysqli_connect(hostname: DB_SERVER, username: DB_USERNAME, password: DB_PASSWORD);
if (mysqli_connect_errno()) {
    echo "Failed to connect to MySQL: " .mysqli_onnecterror();
 }
 $database = mysqli_select_db(mysql: $connection, database: DB_DATABASE);

 
function TableExists($tableName, $connection, $dbName) {
    $t = mysqli_real_escape_string($connection, $tableName);
    $d = mysqli_real_escape_string($connection, $dbName);

    $checktable = mysqli_query($connection,
            "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_NAME = '$t' AND TABLE_SCHEMA = '$d'");

    if(mysqli_num_rows($checktable) > 0) return true;

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
      mysqli_stmt_bind_param($stmt, 'ssdssis', $_name, $_description, $_price, $_imagePath, $_numberInStock,
       $_sellerID, $_numberSold);

      if(mysqli_stmt_execute($stmt)){
         echo json_encode([
               "success" => true,
               "message" => "Image and data uploaded successfully.",
         ]);
      }else{
         echo json_encode([
               "success" => false,
               "message" => "failed upload.",
         ]);
      }
   }
?>
