<?php
include "dbinfo.inc";
header("Access-Control-Allow-Origin: * " );
header("Access-Control-Allow-Methods: GET");
header("Content-Type: application/x-www-form-urlencoded");

$username = $_POST['Username'] ?? null;     //username
$pw = $_POST['pw'] ?? null;                 //password
$imageurl = $_POST['ImageURL'] ?? null;     //imageurl  

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
 if(TableExists("Account",$connection,DB_DATABASE)){
    $sql = "INSERT INTO Account (ProfileImage,Username, Password)
            VALUES (?,?,?)
        ";
    $_profileimg = $imageurl;
    $_username = $username;
    $_pw = $pw;

    $stmt = mysqli_prepare($connection, $sql);
    mysqli_stmt_bind_param(
        $stmt,
        'sss',
        $_profileimg,
        $_username,
        $_pw
    );   
    
    if (mysqli_stmt_execute($stmt)) {
        echo json_encode([
            "success" => true,
            "message" => "Added to account correctly.",
        ]);
    } else {
        echo json_encode([
            "success" => false,
            "message" => "couldnt create account.",
        ]);
    }
 }
?>
