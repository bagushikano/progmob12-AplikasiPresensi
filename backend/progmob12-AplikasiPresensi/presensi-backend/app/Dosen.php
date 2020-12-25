<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Foundation\Auth\User as Authenticatable;

class Dosen extends Authenticatable
{
    protected $table = 'tb_dosen';

    protected $primaryKey = 'id_dosen';

    protected $fillable = ['nama', 'nip', 'username', 'password'];

    protected $hidden = ['password'];

    public $timestamps = false;
    
    public function presensi(){
        return $this->hasMany(Presensi::class, "id_dosen", "id_presensi");
    }
}
