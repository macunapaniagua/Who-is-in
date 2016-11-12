<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class SoccerField extends Model
{
  public function user()
  {
      return $this->belongsTo('App\Models\User');
  }

  public function soccerGame()
  {
    return $this->hasMany('App\Models\SoccerGame');
  }
}
