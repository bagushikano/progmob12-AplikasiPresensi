<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Foundation\Auth\User as Authenticatable;


class Mahasiswa extends Authenticatable
{
    protected $guard = 'mahasiswa';

    protected $table = 'tb_mahasiswa';

    protected $primaryKey = 'id_mahasiswa';

    protected $fillable = ['nama', 'nim', 'username', 'password'];

    protected $hidden = ['password'];

    public $timestamps = false;

    public function detailPresensi(){
        return $this->hasMany(DetailPresensi::class, "id_mahasiswa", "id_detail_presensi");
    }
}
