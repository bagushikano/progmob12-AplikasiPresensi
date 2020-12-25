<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Presensi extends Model
{
    protected $table = 'tb_presensi';

    protected $primaryKey = 'id_presensi';

    protected $fillable = [
        'id_presensi', 'id_dosen', 'nama_presensi', 'keterangan', 'tanggal_open', 'tanggal_close', 'is_open'
    ];

    public $timestamps = false;

    public function dosen(){
        return $this->belongsTo(Dosen::class, 'id_dosen', 'id_dosen');
    }

    public function detailPresensi(){
        return $this->hasMany(DetailPresensi::class, "id_presensi", "id_detail_presensi");
    }
}
