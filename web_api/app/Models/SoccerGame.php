<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class SoccerGame extends Model
{
  public function group()
  {
      return $this->belongsTo('App\Models\Group');
  }

  public function soccerField()
  {
      return $this->belongsTo('App\Models\SoccerField');
  }

  public function player()
  {
      return $this->hasMany('App\Models\Player');
  }
}
