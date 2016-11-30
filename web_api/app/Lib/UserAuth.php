<?php
namespace App\Lib;
use App\Models\User;
class UserAuth
{
  public static function getUserAuth($request) {
      $facebook_id = $request->header('Authorization');
      if (isset($facebook_id)) {
        $user_auth = User::where('facebook_id', $facebook_id)->get()->first();
        if (!isset($user_auth)) {
          return "2";
        }
        return User::find($user_auth->id);
      }
      return "1";
  }
}
