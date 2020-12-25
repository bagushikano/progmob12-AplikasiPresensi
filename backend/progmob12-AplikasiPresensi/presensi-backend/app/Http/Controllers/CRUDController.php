<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Dosen;
use App\Mahasiswa;
use App\Presensi;
use App\DetailPresensi;
use Carbon\Carbon;

class CRUDController extends Controller
{
    public function newPresensi(Request $request){
        $id = Dosen::where('username', $request->username)->get('id_dosen')->first();
        $presensi = Presensi::create([
            'id_dosen' => $id->id_dosen,
            'nama_presensi' => $request->nama_presensi,
            'keterangan' => $request->keterangan,
            'tanggal_open' => $request->tanggal_open,
            'tanggal_close' => $request->tanggal_close,
            'is_open' => 0
        ]);
        return response()->json([
            'message' => 'Presensi Berhasil di Buat',
            'data' => $presensi
        ]);
    }

    public function listPresensiDosen(Request $request){
        $id = Dosen::where('username', $request->dosen)->get('id_dosen')->first();
        $presensi = Presensi::where('id_dosen', $id->id_dosen)->get();
        return response()->json([
            'message' => 'Presensi berhasil di tampilkan',
            'data' => $presensi
        ]);
    }


    public function listPresensiOpenDosen(Request $request){
        $id = Dosen::where('username', $request->dosen)->get('id_dosen')->first();
        $presensi = Presensi::where('id_dosen', $id->id_dosen)->where('is_open', 1)->get();
        return response()->json([
            'message' => 'Presensi berhasil di tampilkan',
            'data' => $presensi
        ]);
    }


    public function editPresensi(Request $request, $presensi){
        $updatePresensi = Presensi::where('id_presensi', $presensi)
        ->update([
            'nama_presensi' => $request->nama_presensi,
            'keterangan' => $request->keterangan,
            'tanggal_open' => $request->tanggal_open,
            'tanggal_close' => $request->tanggal_close
        ]);
        if($updatePresensi>0){
            $dsn = Presensi::where('id_presensi', $presensi)->first();
            return response()->json([
                'message' => 'Data berhasil di Update',
                'data' => $dsn
            ]);
        } else if ($updatePresensi==0){
            return response()->json([
                'message' => 'Data gagal di Update'
            ]);
        }
    }

    public function deletePresensi(Request $request, $presensi){
        $deletePresensi = Presensi::where('id_presensi', $presensi)->delete();
        if($deletePresensi>0){
            return response()->json([
                'message' => 'Data berhasil di delete',
            ]);

        } else if ($updatePresensi==0){
            return response()->json([
                'message' => 'Data gagal di delete'
            ]);
        }
    }


    public function openPresensi(Request $request, $presensi){
        $updatePresensi = Presensi::where('id_presensi', $presensi)
        ->update([
            'is_open' => 1
        ]);
        return response()->json([
            'message' => 'Absensi berhasil di buka'
        ]);
    }


    public function closePresensi(Request $request, $presensi){
        $updatePresensi = Presensi::where('id_presensi', $presensi)
        ->update([
            'is_open' => 0
        ]);
        return response()->json([
            'message' => 'Absensi berhasil di tutup'
        ]);
    }

    public function allPresensi(){
        $presensi = Presensi::with('dosen')->get();
        return response()->json([
            'message' => 'List Absensi berhasil ditampilkan',
            'data' => $presensi
        ]);
    }

    public function listClosePresensi(){
        $presensi = Presensi::with('dosen')->where('is_open', 0)->get();
        return response()->json([
            'message' => 'List Absensi berhasil ditampilkan',
            'data' => $presensi
        ]);
    }

    public function listOpenPresensi(){
        $presensi = Presensi::with('dosen')->where('is_open', 1)->get();
        return response()->json([
            'message' => 'List Absensi berhasil ditampilkan',
            'data' => $presensi
        ]);
    }

    public function newDetailPresensi(Request $request){
        $presensi = DetailPresensi::create([
            'id_presensi' => $request->id_presensi,
            'id_mahasiswa' => $request->id_mahasiswa,
            'date_filled' => $date = now()->setTimezone('GMT+8'),
            'is_approved' => 0
        ]);
        return response()->json([
            'message' => 'Presensi Berhasil di Buat',
            'data' => $presensi
        ]);
    }

    public function listDetailPresensi(Request $request){
        $detail_presensi = DetailPresensi::where('id_presensi', $request->id_presensi)->with('mahasiswa')->get();
        return response()->json([
            'message' => 'Presensi Berhasil di tampilkan',
            'data' => $detail_presensi
        ]);
    }

    public function listDetailPresensiClose(Request $request){
        $detail_presensi = DetailPresensi::where('id_presensi', $request->id_presensi)->with('presensi')->get();
        $data = $detail_presensi->where('is_approved', 0);
        return response()->json([
            'message' => 'Presensi Berhasil di tampilkan',
            'data' => $data
        ]);
    }

    public function listDetailPresensiOpen(Request $request){
        $detail_presensi = DetailPresensi::where('id_presensi', $request->id_presensi)->with('presensi')->get();
        $data = $detail_presensi->where('is_approved', 1);
        return response()->json([
            'message' => 'Presensi Berhasil di tampilkan',
            'data' => $data
        ]);
    }

    public function approvedAbsensi(Request $request, $detailPresensi){
        $approvedPresensi = DetailPresensi::where('id_detail_presensi', $detailPresensi)
        ->update([
            'is_approved' => 1
        ]);
        return response()->json([
            'message' => 'Absensi approved'
        ]);
    }

    public function declineAbsensi(Request $request, $detailPresensi){
        $approvedPresensi = DetailPresensi::where('id_detail_presensi', $detailPresensi)
        ->update([
            'is_approved' => 0
        ]);
        return response()->json([
            'message' => 'Absensi decline'
        ]);
    }
}
