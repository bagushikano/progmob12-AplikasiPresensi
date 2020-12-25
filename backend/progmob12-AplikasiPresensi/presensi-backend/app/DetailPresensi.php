<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class DetailPresensi extends Model
{
    protected $table = 'tb_detail_presensi';

    protected $primaryKey = 'id_detail_presensi';

    protected $fillable = [
        'id_detail_presensi' , 'id_presensi', 'id_mahasiswa', 'date_filled', 'is_approved'
    ];

    public $timestamps = false;

    public function presensi(){
        return $this->belongsTo(Presensi::class, 'id_presensi', 'id_presensi');
    }

    public function mahasiswa(){
        return $this->belongsTo(Mahasiswa::class, 'id_mahasiswa', 'id_mahasiswa');
    }
}
