<?php
namespace App\Lib;
use App\Models\UserGroup;
class AuthorizeApprovedUserGroup
{
  public static function authorize_approved_user_group($user, $group) {
    $user_group = UserGroup::where('user_id', $user->id)->where('group_id', $group->id)->get()->first();
    return $user_group->is_active ? true : false;
  }
}
