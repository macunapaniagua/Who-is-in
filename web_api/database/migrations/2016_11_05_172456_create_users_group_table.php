<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateUsersGroupTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
      Schema::create('users_groups', function (Blueprint $table) {
          $table->increments('id');
          $table->integer('user_id');
          $table->foreign('user_id')->references('id')->on('users')->onDelete('cascade');
          $table->integer('group_id');
          $table->foreign('group_id')->references('id')->on('groups')->onDelete('cascade');
          $table->boolean('is_admin');
          $table->boolean('is_active');
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
        Schema::drop('users_groups');
    }
}
