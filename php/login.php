<?php
/**
 * Created by PhpStorm.
 * User: leon
 * Date: 4/3/15
 * Time: 10:35 AM
 */

require_once "db.inc.php";
echo '<?xml version="1.0" encoding="UTF-8" ?>';

if(!isset($_POST['user'])) {
    echo '<login status="no" msg="missing username" />';
    exit;
}

if(!isset($_POST['pw'])) {
    echo '<login status="no" msg="missing password" />';
    exit;
}

process($_POST['user'],$_POST['pw']);


function process($user,$pw)
{
    $pdo = pdo_connect();
    $userid = getUser($pdo,$user,$pw);
    echo "<login status=\"yes\" id = \"$userid\" />";
}




function getUser($pdo, $user, $password) {
    // Does the user exist in the database?
    $userQ = $pdo->quote($user);
    $query = "SELECT id, password from User where user=$userQ";

    $rows = $pdo->query($query);

    if($row = $rows->fetch()) {
        // We found the record in the database
        // Check the password
        if($row['password'] != $password) {
            echo '<hatter status="no" msg="password error" />';
            exit;
        }

        return $row['id'];
    }

    echo '<login status="no" msg="user error" />';
    exit;
}