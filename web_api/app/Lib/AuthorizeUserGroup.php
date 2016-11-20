<?php
namespace App\Lib;
use App\Models\UserGroup;
class AuthorizeUserGroup
{
  public static function authorize_user_group($user, $group) {
    return UserGroup::where('user_id', $user->id)->where('group_id', $group->id)->get()->first();
  }
}
