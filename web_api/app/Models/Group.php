<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Group extends Model
{
  public function user_group()
  {
      return $this->hasMany('App\Models\UserGroup');
  }
}
