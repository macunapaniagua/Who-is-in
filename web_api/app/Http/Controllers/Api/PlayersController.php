<?php

namespace App\Http\Controllers\Api;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\Http\Requests;
use App\Models\Player;
use App\Lib\UserAuth;
use App\Models\UserGroup;
use App\Models\SoccerGame;
use App\Lib\AuthorizeUserGroup;
use App\Lib\AuthorizeApprovedUserGroup;
use App\Models\Group;

class PlayersController extends Controller
{
  protected $player, $user_group, $soccer_game, $group;

  public function __construct(Player $player, SoccerGame $soccer_game, UserGroup $user_group, Group $group)
  {
      $this->player = $player;
      $this->user_group = $user_group;
      $this->soccer_game = $soccer_game;
      $this->group = $group;
  }

  public function players_soccer_game($soccer_game_id, Request $request)
  {
    $user = UserAuth::getUserAuth($request);
    $group = $this->group->find($group_id);
    if(AuthorizeUserGroup::authorize_user_group($user, $group) != null){
      if(AuthorizeApprovedUserGroup::authorize_approved_user_group($user, $group)){
        $players_soccer_game = $this->player->where('soccer_game_id', $soccer_game_id)->get();

        $players_list = [];
        for ($i=0; $i < count($players_soccer_game); $i++) {
          $players_list[$i] = ["player_id" => $players_soccer_game[$i]->id, "user_id" => $players_soccer_game[$i]->user_group->user_id, "name" => $players_soccer_game[$i]->user_group->user->name, "picture" => $players_soccer_game[$i]->user_group->user->picture];
        }
        return response($players_list, 200);
      }
      return response(['error' => '¡El administrador aún no ha aprobado su solicitud!'], 401);
    }
    return response(['error' => '¡Usted ha sido expulsado este grupo!'], 403);
  }

  public function join_soccer_game(Request $request)
  {
    $user = UserAuth::getUserAuth($request);
    $user_group = $this->user_group->where('user_id', $user->id)->where('group_id', $request->group_id)->get()->first();
    $new_soccer_game_player = new $this->player;
    $new_soccer_game_player->users_group_id = $user_group->id;
    $new_soccer_game_player->soccer_game_id = $request->soccer_game_id;
    $new_soccer_game_player->save();

    return response($new_soccer_game_player, 201);
  }

  public function leave_soccer_game($player_id, Request $request)
  {
    try {
      $delete_player_soccer_game = $this->player->findOrFail($player_id);
      $delete_player_soccer_game->delete();
      return response("Usuario ha dejado el juego", 200);
    }catch(\Exception $e){
    }
  }

}
