<?php
include "dbinfo.inc";
header("Access-Control-Allow-Origin:" . DB_ACCESS_ALLOW_ORIGIN);
header("Access-Control-Allow-Methods: GET");
header("Content-Type: application/json");

// Return the current timestamp as a JSON response
$response = ["timestamp" => date("Y-m-d H:i:s")];

echo json_encode($response);
?>