<?php

namespace App\Http\Controllers\Api;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\Http\Requests;
use App\Models\Group;
use App\Models\UserGroup;
use App\Lib\RandomString;
use App\Lib\UserAuth;
use App\Lib\UsersGroups;
use App\Http\Requests\GroupsRequest;

class GroupsController extends Controller
{
  protected $group, $user_group;

  public function __construct(Group $group, UserGroup $user_group)
  {
      $this->group = $group;
      $this->user_group = $user_group;
  }

  public function store(GroupsRequest $request)
  {
    $user = UserAuth::getUserAuth($request);
    $new_group = new $this->group;
    $new_group->name = $request->name;
    $new_group->code = RandomString::generateRandomString();
    $new_group->save();

    $user_group = ["user_id" => $user->id, "group_id" => $new_group->id, "is_admin" => true, "is_active" => false];
    UsersGroups::createUserGroup($user_group);
    $group_info = ["user_id" => $user->id, "group_id" => $new_group->id, "code" => $new_group->code, "is_admin" => true, "name" => $new_group->name];
    return response($group_info, 201);
  }

  public function show($code, Request $request)
  {
    $user = UserAuth::getUserAuth($request);
    $user_group_object = null;
    $group_search = $this->group->where('code', $code)->get()->first();
    if ($group_search != null) {
        $suscribed = false;
        $user_group = $this->user_group->where('group_id', $group_search->id)->where('user_id', $user->id)->get()->first();
        if ($user_group == null) {

          $user_group = ["user_id" => $user->id, "group_id" => $group_search->id, "is_admin" => false, "is_active" => false];
          UsersGroups::createUserGroup($user_group);
        }

        $group_info = ["user_id" => $user->id, "group_id" => $group_search->id, "code" => $group_search->code, "is_admin" => $user_group->is_admin, "name" => $group_search->name];
        return response($group_info, 200);
    } else {
       return response(['error' => '¡El código del grupo no existe!'], 404);
    }

  }

}
