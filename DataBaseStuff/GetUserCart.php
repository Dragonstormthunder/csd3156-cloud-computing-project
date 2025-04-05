<?php
include "dbinfo.inc";
header("Access-Control-Allow-Origin: * " );
header("Access-Control-Allow-Methods: GET");
header("Content-Type: application/json");

$ID = isset($_GET['ID']) ? $_GET['ID'] : null;

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
 if(TableExists("Orders",$connection,DB_DATABASE)){
    $stmt = $connection->prepare("
        SELECT Orders.CustomerID,Orders.SellerID,Orders.InventoryID,Orders.Quantity,Orders.OrderConfirmed FROM Orders WHERE Orders.CustomerID = ? AND Orders.OrderConfirmed = 0
    ");
    mysqli_stmt_bind_param($stmt, 's', $ID);
    $stmt->execute();
    $result = $stmt->get_result();
    $response=[];
    while($query_data = mysqli_fetch_row($result)) {
        $data = [
                    "CustomerID" => $query_data[0],
                    "SellerID" => $query_data[1],
                    "InventoryID" => $query_data[2],
                    "Quantity" => $query_data[3],
                    "OrderConfirmed" =>$query_data[4],
        ];
        $response[] = $data;

    }
    echo json_encode($response);
 }
?>
