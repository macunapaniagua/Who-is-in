<?php

namespace App\Http\Controllers\Api;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\Http\Requests;
use App\Models\SoccerField;
use App\Lib\UserAuth;
use App\Lib\AuthorizeUserGroup;
use App\Lib\AuthorizeApprovedUserGroup;
use App\Models\Group;

class SoccerFieldsController extends Controller
{
  protected $soccer_field;

  public function __construct(Group $group, SoccerField $soccer_field)
  {
      $this->soccer_field = $soccer_field;
      $this->group = $group;
  }

  public function show($group_id, Request $request)
  {
    $user = UserAuth::getUserAuth($request);
    $group = $this->group->find($group_id);
    if(AuthorizeUserGroup::authorize_user_group($user, $group) != null){
      if(AuthorizeApprovedUserGroup::authorize_approved_user_group($user, $group)){
        $soccer_fields = $this->soccer_field->where('group_id', $group_id)->get();
        return response($soccer_fields, 200);
      }
      return response(['error' => '¡El administrador aún no ha aprobado su solicitud!'], 401);
    }
    return response(['error' => '¡Usted ha sido expulsado este grupo!'], 403);
  }

  public function store(Request $request)
  {
    $user = UserAuth::getUserAuth($request);
    $new_soccer_field = new $this->soccer_field;
    $new_soccer_field->name = $request->name;
    $new_soccer_field->latitude = $request->latitude;
    $new_soccer_field->longitude = $request->longitude;
    $new_soccer_field->phone = $request->phone;
    $new_soccer_field->total = $request->total;
    $new_soccer_field->players_account = $request->players_account;
    $new_soccer_field->group_id = $request->group_id;
    $new_soccer_field->save();

    return response($new_soccer_field, 201);
  }

  public function update($id, Request $request)
  {
    $update_soccer_field = $this->soccer_field->findOrFail($id);
      $update_soccer_field->name = $request->name;
      $update_soccer_field->latitude = $request->latitude;
      $update_soccer_field->longitude = $request->longitude;
      $update_soccer_field->phone = $request->phone;
      $update_soccer_field->total = $request->total;
      $update_soccer_field->players_account = $request->players_account;
      $update_soccer_field->group_id = $request->group_id;
      $update_soccer_field->save();
      return response($update_soccer_field, 200);
  }

  public function destroy($id, Request $request)
  {
      try {
          $delete_soccer_field = $this->soccer_field->findOrFail($id);
          $delete_soccer_field->delete();
          return response("", 200);
      }catch(\Exception $e){
          return response(["error" => "¡No se puede eliminar la cancha porque está siendo utilizada!"], 401);
      }
  }
}
