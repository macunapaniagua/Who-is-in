<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Player extends Model
{
  public function soccer_game()
  {
      return $this->belongsTo('App\Models\SoccerGame');
  }

  public function soccerField()
  {
      return $this->belongsTo('App\Models\SoccerField');
  }

}
