<?php
/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It's a breeze. Simply tell Laravel the URIs it should respond to
| and give it the controller to call when that URI is requested.
|
*/
Route::get('/', function () {
    return view('welcome');
});
Route::group(['middleware' => 'api'], function () {
  Route::group(['prefix' => 'api'], function() {
    Route::post('authenticate', 'Api\AuthController@authenticate');
    Route::resource('groups', 'Api\GroupsController');
    Route::get('users_group/{group_id}', 'Api\GroupsController@users_group');
    Route::resource('soccer_fields', 'Api\SoccerFieldsController');
    Route::resource('soccer_games', 'Api\SoccerGamesController');
    Route::get('players_soccer_game/{soccer_match_id}', 'Api\PlayersController@players_soccer_game');
    Route::post('user_group_status', 'Api\GroupsController@change_member_status');
    Route::post('user_group_remove', 'Api\GroupsController@remove_member');
    Route::get('all_soccer_games/{group_id}', 'Api\SoccerGamesController@all_soccer_games');
    Route::post('join_soccer_game', 'Api\PlayersController@join_soccer_game');
    Route::post('leave_soccer_game/{players_id}', 'Api\PlayersController@leave_soccer_game');

  });
});
