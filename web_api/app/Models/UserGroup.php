<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class UserGroup extends Model
{
    protected $table = "users_groups";

    protected $fillable = ["user_id", "group_id", "is_admin", "is_active"];

    public function user()
    {
        return $this->hasOne('App\Models\User');
    }

    public function group()
    {
        return $this->hasOne('App\Models\Group');
    }
}
