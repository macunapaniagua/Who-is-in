<?php

namespace App\Http\Controllers\Api;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\Http\Requests;
use App\Models\SoccerGame;
use App\Lib\UserAuth;

class SoccerGamesController extends Controller
{
  protected $soccer_game;

  public function __construct(SoccerGame $soccer_game)
  {
    $this->soccer_game = $soccer_game;
  }

  public function show($group_id, Request $request)
  {
    $user = UserAuth::getUserAuth($request);
    $soccer_games = $this->soccer_game->where('group_id', $group_id)->get();
    $soccer_game_info = [];
    $count = 0;
    foreach ($soccer_games as $soccer_game) {
      $soccer_game_info[$count] = ["id" => $soccer_game->id, "soccer_field_id" => $soccer_game->soccer_field_id, "players_limit" => $soccer_game->players_limit, "hour" => $soccer_game->hour, "date" => $soccer_game->date, "soccer_field" => $soccer_game->soccer_field->name];
      $count++;
    }
    return response($soccer_game_info, 200);
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
