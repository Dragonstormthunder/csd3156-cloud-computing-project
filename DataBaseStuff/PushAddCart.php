<?php
include "dbinfo.inc";
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST, GET, OPTIONS");
header("Content-Type: application/x-www-form-urlencoded");

$CustomerID = $_POST['CustomerID'] ?? null;                 //customer id
$SellerID = $_POST['SellerID'] ?? null;                     //seller id
$InventoryID = $_POST['InventoryID'] ?? null;               //inventory id
$Quant = $_POST['Quant'] ?? null;                           //Quantity


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

if(TableExists("Orders",$connection,DB_DATABASE)){
    $sql = "SELECT Orders.OrderGroupID From Orders WHERE  Orders.customerID = ? AND Orders.OrderConfirmed = 0";
    $stmt = $connection->prepare($sql);
    $stmt->bind_param("i", $CustomerID);
    $stmt->execute();
    $result = $stmt->get_result();

    echo mysqli_num_rows($result);
    if(mysqli_num_rows($result) == 0){
        $_sql = "SELECT MAX(Orders.OrderGroupID) From Orders";
        $_stmt = $connection->prepare($_sql);
        $_stmt->execute();
        $_result = $_stmt->get_result();
        $OrderGrpID = $_result->fetch_column() + 1;
        //echo $_result->fetch_column();
    }   
    else{
        $OrderGrpID = $result->fetch_column();
        //echo $result->fetch_column();
    }
    echo $OrderGrpID;

    $sql2 = "INSERT INTO Orders (CustomerID,SellerID,InventoryID,Quantity,OrderGroupID,OrderConfirmed)
        VALUES(?,?,?,?,?,?)";
    $_customerID = $CustomerID;
    $_sellerID = $SellerID; 
    $_InventoyID = $InventoryID;
    $_quantity = $Quant;
    $_orderGrp = $OrderGrpID;
    $_orderConfirmed = 0;

    $stmt2 = mysqli_prepare($connection, $sql2);
    // Bind the parameters to the placeholders
    mysqli_stmt_bind_param(
        $stmt2,
        'iiiiii',
        $_customerID,
        $_sellerID,
        $_InventoyID,
        $_quantity,
        $_orderGrp,
        $_orderConfirmed
    );   
    
    if (mysqli_stmt_execute($stmt2)) {
        echo json_encode([
            "success" => true,
            "message" => "Added to cart correctly.",
        ]);
    } else {
        echo json_encode([
            "success" => false,
            "message" => "failed upload to cart.",
        ]);
    }
    }

?>