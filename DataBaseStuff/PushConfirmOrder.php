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
