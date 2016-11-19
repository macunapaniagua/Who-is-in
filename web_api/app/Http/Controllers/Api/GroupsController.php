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

  public function users_group($group_id, Request $request)
  {
    $users_status = $request->input('status');
    $users_group = $this->user_group->where('group_id', $group_id)->where('is_active', $users_status)->get();
    $user_group_list = [];
    for ($i=0; $i < count($users_group); $i++) {
      $user_group_list[$i] = ["user_id" => $users_group[$i]->user_id, "is_admin" => $users_group[$i]->is_admin, "name" => $users_group[$i]->user->name, "picture" => $users_group[$i]->user->picture, "is_active" => $users_group[$i]->is_active];
    }
    return response($user_group_list, 200);
  }

  //crear un nuevo grupo, automaticamente estoy activo y soy administrador
  public function store(GroupsRequest $request)
  {
    $user = UserAuth::getUserAuth($request);
    $new_group = new $this->group;
    $new_group->name = $request->name;
    $new_group->code = RandomString::generateRandomString();
    $new_group->save();

    $user_group = ["user_id" => $user->id, "group_id" => $new_group->id, "is_admin" => true, "is_active" => true];
    UsersGroups::createUserGroup($user_group);
    $group_info = ["group_id" => $new_group->id, "code" => $new_group->code, "name" => $new_group->name];
    $user_info = ["user_id" => $user->id, "is_admin" => true, "name" => $user->name, "picture" => $user->picture, "facebook_id" => $user->facebook_id];
    return response(["group" => $group_info, "user" => $user_info], 201);
  }

  //suscribirse o entrar a un grupo
  public function show($code, Request $request)
  {
    $user = UserAuth::getUserAuth($request);
    $group_search = $this->group->where('code', $code)->get()->first();

    if ($group_search != null) {
        $group_info = ["group_id" => $group_search->id, "code" => $group_search->code, "name" => $group_search->name];
        $user_info = ["user_id" => $user->id, "is_admin" => true, "name" => $user->name, "picture" => $user->picture, "facebook_id" => $user->facebook_id];
        $user_group = $this->user_group->where('group_id', $group_search->id)->where('user_id', $user->id)->get()->first();
        if ($user_group == null) {
          $user_group = ["user_id" => $user->id, "group_id" => $group_search->id, "is_admin" => false, "is_active" => false];
          UsersGroups::createUserGroup($user_group);
        }
        if ($user_group['is_active']) {
          $group_info = ["group_id" => $group_search->id, "code" => $group_search->code, "name" => $group_search->name];
          $user_info = ["user_id" => $user->id, "is_admin" => $user_group->is_admin, "name" => $user->name, "picture" => $user->picture, "facebook_id" => $user->facebook_id];
          return response(["group" => $group_info, "user" => $user_info], 200);
        }
        return response(["group" => $group_info, "user" => $user_info], 201);
    } else {
       return response(['error' => '¡El código del grupo no existe!'], 404);
    }
  }

  public function change_member_status(Request $request)
  {
    $user = UserAuth::getUserAuth($request);
    $user_group = $this->user_group->where('user_id', $request->user_id)->where('group_id', $request->group_id)->get()->first();
    if ($user_group != null) {
      $update_user_group = $this->user_group->findOrFail($user_group->id);
      $update_user_group->is_active = true;
      $update_user_group->save();
      return response($update_user_group, 200);
    }

  }

}
