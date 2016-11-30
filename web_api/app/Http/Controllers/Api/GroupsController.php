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
use App\Lib\AuthorizeUserGroup;
use App\Lib\AuthorizeApprovedUserGroup;
use App\Http\Requests\GroupsRequest;
use App\Models\User;

class GroupsController extends Controller
{
  protected $group, $user_group, $user;

  public function __construct(Group $group, UserGroup $user_group, User $user)
  {
    $this->group = $group;
    $this->user_group = $user_group;
    $this->user = $user;
  }

  public function users_group($group_id, Request $request)
  {
    $user = UserAuth::getUserAuth($request);
    if ($user == "1") {
      return response(['error' => trans('api_messages.error_facebook_id')], 403);
    } else if($user == "2") {
      return response(['error' => trans('api_messages.error_player')], 403);
    } else {
      $group = $this->group->find($group_id);
      if(AuthorizeUserGroup::authorize_user_group($user, $group) != null){
        if(AuthorizeApprovedUserGroup::authorize_approved_user_group($user, $group)){
          $users_status = $request->input('status');
          $users_group = $this->user_group->where('group_id', $group_id)->where('is_active', $users_status)->get();
          $user_group_list = [];
          for ($i=0; $i < count($users_group); $i++) {
            $user_group_list[$i] = ["user_id" => $users_group[$i]->user_id, "is_admin" => $users_group[$i]->is_admin, "name" => $users_group[$i]->user->name, "picture" => $users_group[$i]->user->picture, "is_active" => $users_group[$i]->is_active];
          }
          return response($user_group_list, 200);
        }
        return response(['error' => trans('api_messages.error_request')], 401);
      }
      return response(['error' => trans('api_messages.error_group')], 403);
    }
  }

  //crear un nuevo grupo, automaticamente estoy activo y soy administrador
  public function store(Request $request)
  {
    $user = UserAuth::getUserAuth($request);
    if ($user == "1") {
      return response(['error' => trans('api_messages.error_facebook_id')], 403);
    } else if($user == "2") {
      return response(['error' => trans('api_messages.error_player')], 403);
    } else {
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
  }

  //suscribirse o entrar a un grupo
  public function show($code, Request $request)
  {
    $user = UserAuth::getUserAuth($request);
    if ($user == "1") {
      return response(['error' => trans('api_messages.error_facebook_id')], 403);
    } else if($user == "2") {
      return response(['error' => trans('api_messages.error_player')], 403);
    } else {
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
        return response([ 'error' => trans('api_messages.error_code')], 404);
      }
    }
  }

  public function change_member_status(Request $request)
  {
    $user = UserAuth::getUserAuth($request);
    if ($user == "1") {
      return response(['error' => trans('api_messages.error_facebook_id')], 403);
    } else if($user == "2") {
      return response(['error' => trans('api_messages.error_player')], 403);
    } else {
      $user_group = $this->user_group->where('user_id', $request->user_id)->where('group_id', $request->group_id)->get()->first();
      if ($user_group != null) {
        $update_user_group = $this->user_group->findOrFail($user_group->id);
        $update_user_group->is_active = true;
        $update_user_group->save();
        $user_updated = $this->user->find($update_user_group->user_id);
        $user_info = ["user_id" => $user_updated->id, "is_admin" => $user_updated->is_admin, "name" => $user_updated->name, "picture" => $user_updated->picture, "facebook_id" => $user_updated->facebook_id];
        return response($user_info, 200);
      }
    }
  }

  public function remove_member(Request $request)
  {
    $user = UserAuth::getUserAuth($request);
    if ($user == "1") {
      return response(['error' => trans('api_messages.error_facebook_id')], 403);
    } else if($user == "2") {
      return response(['error' => trans('api_messages.error_player')], 403);
    } else {
      $user_group = $this->user_group->where('user_id', $request->user_id)->where('group_id', $request->group_id)->get()->first();
      if ($user_group != null) {
        $update_user_group = $this->user_group->findOrFail($user_group->id);
        $user_deleted = $this->user->find($update_user_group->user_id);
        $update_user_group->delete();
        $user_info = ["user_id" => $user_deleted->id, "is_admin" => $user_deleted->is_admin, "name" => $user_deleted->name, "picture" => $user_deleted->picture, "facebook_id" => $user_deleted->facebook_id];
        return response($user_info, 200);
      }
    }
  }
}
