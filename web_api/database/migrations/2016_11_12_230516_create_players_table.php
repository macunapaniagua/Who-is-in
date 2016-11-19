<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreatePlayersTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
      Schema::create('players', function (Blueprint $table) {
          $table->increments('id');
          $table->integer('users_group_id');
          $table->foreign('users_group_id')->references('id')->on('users_groups')->onDelete('cascade');
          $table->integer('soccer_game_id');
          $table->foreign('soccer_game_id')->references('id')->on('soccer_games')->onDelete('cascade');
          $table->boolean('status');
          $table->timestamps();
      });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::drop('players');
    }
}
