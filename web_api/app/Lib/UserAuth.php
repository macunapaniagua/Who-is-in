<?php
namespace App\Lib;
use App\Models\User;
class UserAuth
{
  public static function getUserAuth($request) {
      $facebook_id = $request->header('facebook_id');
      $user_auth = User::where('facebook_id', $facebook_id)->get()->first();
      return User::find($user_auth->id);
  }
}
