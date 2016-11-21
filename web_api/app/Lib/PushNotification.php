<?php
namespace App\Lib;
use LaravelFCM\Message\OptionsBuilder;
use LaravelFCM\Message\PayloadDataBuilder;
use LaravelFCM\Message\PayloadNotificationBuilder;
use FCM;
use LaravelFCM\Message\Topics;

class PushNotification
{
    public static function sendNotification($title, $body, $group_id) {
      $notificationBuilder = new PayloadNotificationBuilder($title);
      $notificationBuilder->setBody($body)
                        ->setSound('default');

      $notification = $notificationBuilder->build();

      $topic = new Topics();
      $topic->topic('soccer_'.$group_id);

      $topicResponse = FCM::sendToTopic($topic, null, $notification, null);

      $topicResponse->isSuccess();
      $topicResponse->shouldRetry();
      $topicResponse->error();
    }
}
