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
 if(TableExists("Inventory",$connection,DB_DATABASE)){

    $sql = "SELECT 
    Inventory.InventoryID,
    Inventory.Name,
    Inventory.Description,
    Inventory.Price,
    Inventory.Image,
    Inventory.NumberInStock,
    Inventory.SellerID,
    Inventory.NumberSold,
    Account.Username,
    Account.ProfileImage
     From Inventory INNER JOIN Account ON Inventory.SellerID = Account.AccountID WHERE  Inventory.InventoryID = ? ";
    $stmt = $connection->prepare($sql);
    $stmt->bind_param("s", $ID);
    $stmt->execute();
    $result = $stmt->get_result();

    $response = [];

    while($query_data = mysqli_fetch_row($result)) {
        $data = [
                    "ID" => $query_data[0],
                    "Name" => $query_data[1],
                    "Description" => $query_data[2],
                    "Price" => $query_data[3],
                    "Image" => $query_data[4],
                    "Stock" => $query_data[5],
                    "SellerID" => $query_data[6],
                    "SoldAmt" => $query_data[7],
                    "SellerUserName" => $query_data[8],
                    "SellerProfilePicture" => $query_data[9]
        ];
        $response[] = $data;

    }
    echo json_encode($response);
 }
?>
