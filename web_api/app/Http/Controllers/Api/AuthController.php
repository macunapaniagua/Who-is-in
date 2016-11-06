<?php

namespace App\Http\Controllers\Api;

use Illuminate\Http\Request;
use App\Http\Controllers\Controller;
use App\Http\Requests;
use App\Models\User;
use Auth;
use App\Http\Requests\AuthUserRequest;

class AuthController extends Controller
{
  protected $user;

  public function __construct(User $user)
  {
      $this->user = $user;
  }

  public function authenticate(AuthUserRequest $request)
  {
    $user = $this->user->where('facebook_id', $request->facebook_id)->get()->first();
    if ($user != null) {
      $user_autheticated = $this->user->find($user->id);
      return response($user_autheticated, 200);
    } else {
      $store_user = $this->user;
      $store_user->name = $request->name;
      $store_user->picture = $request->picture;
      $store_user->facebook_id = $request->facebook_id;
      $store_user->save();
      return response($store_user, 200);
    }

  }
}
