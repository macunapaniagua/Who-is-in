<?php
namespace App\Lib;

class Notification
{
    public static function sendNotification($soccerField, $date, $hour) {
        $auth_key = 'AIzaSyCIhyyHSJxzyoaxzAB-R3gjrp6ScV3LQtk';
        $token    = 'AAAATN0i70U:APA91bEN5Vc-VHZxdq4GJrXy6WRmwVx14NE-96EwAax8AIhsX1TFERA9TupASpzWiv74p4m59mF3wBYAzFA_kqB-nEauMowwL6zffkmGTVsbzFwBjCRi_IzxewSlzf900pCMS9ProgtTfCYffk3G6ni_T2VCzyQLhA';
        exec('curl -H "Content-type: application/json" -H "Authorization:key='.$auth_key.'"  -X POST -d '{ "to": "'.$token.'","data": { "message": "'.$soccerField." ".$hour." ".$date.'"}}' https://fcm.googleapis.com/fcm/send');
   }
}
