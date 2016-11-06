<?php
namespace App\Lib;
use App\Models\UserGroup;
class UsersGroups
{
  public static function createUserGroup($user_group) {
    UserGroup::create($user_group);
  }
}
