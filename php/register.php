<?php
/**
 * Created by PhpStorm.
 * User: leon
 * Date: 4/3/15
 * Time: 10:36 AM
 */

require_once "db.inc.php";
echo '<?xml version="1.0" encoding="UTF-8" ?>';

if(!isset($_POST['user'])) {
    echo '<register status="no" msg="missing username" />';
    exit;
}

if(!isset($_POST['pw'])) {
    echo '<register status="no" msg="missing password" />';
    exit;
}

if(!isset($_POST['pw1'])) {
    echo '<register status="no" msg="missing password check" />';
    exit;
}

process($_POST['user'],$_POST['pw'],$_POST['pw1']);




function process($user,$pw,$pwc)
{
    $pdo = pdo_connect();
    if($pw != $pwc)
    {
        echo '<register status="no" msg="two password doesn\'t match" />';
    }

    //connect to the db

    if(check_exist($pdo,$user))
    {
        echo '<register status="no" msg="user already exist" />';
    }

    register($pdo,$user,$pw);
}

function check_exist($pdo,$user)
{
    $userQ = $pdo->quote($user);
    $query = "SELECT id from User where user=$userQ";

    $rows = $pdo->query($query);

    if($row = $rows->fetch())
    {
        return true;
    }
    else
    {
        return false;
    }
}

function register($pdo,$user,$pw)
{
    $userQ = $pdo->quote($user);
    $pwQ = $pdo->quote($pw);

    $query = <<<QUERY
REPLACE INTO user(Username,Password)
VALUES($userQ,$pwQ)
QUERY;
    if(!$pdo->query($query))
    {
        echo '<register status="no" msg="insertfail">';
        exit;
    }
    else
    {
        echo '<register status="yes">';
        exit;
    }


}