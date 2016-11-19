<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateSoccerFieldsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
      Schema::create('soccer_fields', function (Blueprint $table) {
          $table->increments('id');
          $table->string('name');
          $table->double('latitude');
          $table->double('longitude');
          $table->string('phone');
          $table->integer('total');
          $table->integer('players_account');
          $table->integer('group_id');
          $table->foreign('group_id')->references('id')->on('groups')->onDelete('cascade');
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
        Schema::drop('soccer_fields');
    }
}
