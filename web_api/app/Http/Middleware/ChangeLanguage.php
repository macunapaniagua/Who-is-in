<?php

namespace App\Http\Middleware;

use Closure;
use Session;
use App;

class ChangeLanguage
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return mixed
     */
    public function handle($request, Closure $next)
    {
        $language = substr($request->server('HTTP_ACCEPT_LANGUAGE'), 0, 2);
        App::setLocale($language);
        return $next($request);
    }
}
