<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateSoccerGamesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
      Schema::create('soccer_games', function (Blueprint $table) {
          $table->increments('id');
          $table->integer('group_id');
          $table->foreign('group_id')->references('id')->on('groups')->onDelete('cascade');
          $table->integer('soccer_field_id');
          $table->foreign('soccer_field_id')->references('id')->on('soccer_fields')->onDelete('cascade');
          $table->integer('players_limit');
          $table->string('hour');
          $table->date('date');
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
        Schema::drop('soccer_games');
    }
}
