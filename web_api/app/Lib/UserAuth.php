<?php
namespace App\Lib;
use App\Models\User;
class UserAuth
{
  public static function getUserAuth($request) {
      $facebook_id = $request->header('facebook_id');
      if (isset($facebook_id)) {
        $user_auth = User::where('facebook_id', $facebook_id)->get()->first();
        if (!isset($user_auth)) {
          return response(trans('api_messages.error_player'));
        }
        return User::find($user_auth->id);
      }
      return response(trans('api_messages.error_facebook_id'));
  }
}
