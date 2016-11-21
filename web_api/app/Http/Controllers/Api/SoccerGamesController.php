<?php

namespace App\Http\Controllers\Api;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\Http\Requests;
use App\Models\SoccerGame;
use App\Lib\UserAuth;
use App\Lib\AuthorizeUserGroup;
use App\Lib\AuthorizeApprovedUserGroup;
use App\Models\Group;
use App\Models\Player;
use App\Models\UserGroup;

class SoccerGamesController extends Controller
{
  protected $soccer_game, $group;

  public function __construct(Group $group, SoccerGame $soccer_game, Player $player, UserGroup $user_group)
  {
    $this->soccer_game = $soccer_game;
    $this->group = $group;
    $this->player = $player;
    $this->user_group = $user_group;
  }

  public function all_soccer_games($group_id, Request $request)
  {
    $user = UserAuth::getUserAuth($request);
    $group = $this->group->find($group_id);
    if(AuthorizeUserGroup::authorize_user_group($user, $group) != null){
      if(AuthorizeApprovedUserGroup::authorize_approved_user_group($user, $group)){
        $soccer_games = $this->soccer_game->where('group_id', $group_id)->get();
        $soccer_game_info = [];
        $count = 0;
        foreach ($soccer_games as $soccer_game) {
          $user_group = $this->user_group->where('user_id', $user->id)->where('group_id', $group->id)->get()->first();
          $user_status = $this->player->where('soccer_game_id', $soccer_game->id)->where('users_group_id', $user_group->id)->get()->count();
          $players_count = $this->player->where('soccer_game_id', $soccer_game->id)->get()->count();
          $soccer_game_info[$count] = ["id" => $soccer_game->id, "soccer_field_id" => $soccer_game->soccer_field_id, "datetime" => $soccer_game->date.' '.$soccer_game->hour, "soccer_field" => $soccer_game->soccer_field->name, "confirmations" => $players_count.'/'.$soccer_game->players_limit, "user_status" => $user_status == 1 ? true : false];
          $count++;
        }
        return response($soccer_game_info, 200);
      }
      return response(['error' => '¡El administrador aún no ha aprobado su solicitud!'], 401);
    }
    return response(['error' => '¡Usted ha sido expulsado este grupo!'], 403);
  }

  public function store(Request $request)
  {
    $user = UserAuth::getUserAuth($request);
    $new_soccer_game = new $this->soccer_game;
    $new_soccer_game->group_id = $request->group_id;
    $new_soccer_game->soccer_field_id = $request->soccer_field_id;
    $new_soccer_game->hour = $request->hour;
    $new_soccer_game->date = $request->date;
    $new_soccer_game->players_limit = $request->players_limit;
    $new_soccer_game->save();

    return response($new_soccer_game, 201);
  }

  public function update($id, Request $request)
  {
    $update_soccer_game = $this->soccer_game->findOrFail($id);
    $update_soccer_game->group_id = $request->group_id;
    $update_soccer_game->soccer_field_id = $request->soccer_field_id;
    $update_soccer_game->hour = $request->hour;
    $update_soccer_game->date = $request->date;
    $update_soccer_game->players_limit = $request->players_limit;
    $update_soccer_game->save();
    return response($update_soccer_game, 200);
  }

  public function destroy($id, Request $request)
  {
    try {
      $delete_soccer_game = $this->soccer_game->findOrFail($id);
      $delete_soccer_game->delete();
      return response("", 200);
    }catch(\Exception $e){
      return response(["error" => "¡No se puede eliminar la mejenga porque está siendo utilizada!"], 401);
    }
  }
}
