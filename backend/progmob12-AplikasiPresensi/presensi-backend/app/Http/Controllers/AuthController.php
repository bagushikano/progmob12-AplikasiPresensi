<?php

namespace App\Http\Controllers;

use Illuminate\Auth\Events\Attempting;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use App\Dosen;
use App\Mahasiswa;

class AuthController extends Controller
{
    function registerDosen(Request $request) {
        $cekdosen = Dosen::where('username', $request->username)->count();
        $cekmhs = Mahasiswa::where('username', $request->username)->count();
        if($cekdosen > 0 || $cekmhs > 0){
            return response()->json([
                'message' => 'username sama'
            ]);
        }
        else {
            $dsn = Dosen::create([
                'nama' => $request->nama,
                'nip' => $request->nip,
                'username' => $request->username,
                'password' => bcrypt($request->password)
            ]);
            return response()->json([
                'message' => 'Berhasil Register',
                'data' => $dsn
            ]);
        }
    }

    function loginDosen(Request $request) {
        $dosen = Dosen::where('username', $request->username)->first();
        if(Auth::attempt(['username' => $request->username, 'password' => $request->password])) {
            return response()->json([
                'message' => "login sukses",
                'data' => $dosen
            ]);
        } else{
            return response()->json([
                'message' => "login gagal"
            ]);
        }
    }

    function registerMahasiwa(Request $request) {
        $cekdosen = Dosen::where('username', $request->username)->count();
        $cekmhs = Mahasiswa::where('username', $request->username)->count();
        if($cekdosen > 0 || $cekmhs > 0){
            return response()->json([
                'message' => 'username sama'
            ]);
        }
        else {
            $mhs = Mahasiswa::create([
                'nama' => $request->nama,
                'nim' => $request->nim,
                'username' => $request->username,
                'password' => bcrypt($request->password)
            ]);
            return response()->json([
                'message' => 'Berhasil Register',
                'data' => $mhs
            ]);
        }
    }

    function loginMahasiswa(Request $request) {
        $mahasiswa = Mahasiswa::where('username', $request->username)->first();
        if(Auth::guard('mahasiswa')->attempt(['username' => $request->username, 'password' => $request->password])) {
            return response()->json([
                'message' => "login sukses",
                'data' => $mahasiswa
            ]);
        } else{
            return response()->json([
                'message' => "login gagal"
            ]);
        }
    }

    public function editProfilMahasiswa(Request $request, $mahasiswa){
        $cekdosen = Dosen::where('username', $request->username)->count();
        $cekmhs = Mahasiswa::where('username', $request->username)->count();
        if(($cekdosen > 0 || $cekmhs > 0) && strcmp($request->username, $mahasiswa) !== 0){
            return response()->json([
                'message' => 'username sama'
            ]);
        }
        else {
            if(Auth::guard('mahasiswa')->attempt(['username' => $mahasiswa, 'password' => $request->password])) {
                $updateMahasiswa = Mahasiswa::where('username', $mahasiswa)
                ->update([
                    'nama' => $request->nama,
                    'nim' => $request->nim,
                    'username' => $request->username,
                ]);
                if($updateMahasiswa>0){
                    $mhs = Mahasiswa::where('username', $request->username)->get();
                    return response()->json([
                        'message' => 'Data berhasil di Update',
                        'data' => $mhs
                    ]);
                } else if ($updateMahasiswa==0){
                    return response()->json([
                        'message' => 'Data gagal di Update'
                    ]);
                }
            } else{
                return response()->json([
                    'message' => "password salah"
                ]);
            }
        }
    }

    public function editProfilDosen(Request $request, $dosen){
        $cekdosen = Dosen::where('username', $request->username)->count();
        $cekmhs = Mahasiswa::where('username', $request->username)->count();
        if(($cekdosen > 0 || $cekmhs > 0) && strcmp($request->username, $dosen) !== 0){
            return response()->json([
                'message' => 'username sama'
            ]);
        }
        else {
            if(Auth::attempt(['username' => $dosen, 'password' => $request->password])) {
                $updateDosen = Dosen::where('username', $dosen)
                ->update([
                    'nama' => $request->nama,
                    'nip' => $request->nip,
                    'username' => $request->username,
                ]);
                if($updateDosen>0){
                    $dsn = Dosen::where('username', $request->username)->get();
                    return response()->json([
                        'message' => 'Data berhasil di Update',
                        'data' => $dsn
                    ]);
                } else if ($updateDosen==0){
                    return response()->json([
                        'message' => 'Data gagal di Update'
                    ]);
                }
            } else{
                return response()->json([
                    'message' => "password salah"
                ]);
            }
        }
    }

    public function changeDosenPassword(Request $request, $dosen){
        if(Auth::attempt(['username' => $dosen, 'password' => $request->oldPassword])) {
            $updateDosen = Dosen::where('username', $dosen)
            ->update([
                'password' => bcrypt($request->newPassword)
            ]);
            if($updateDosen>0){
                $dsn = Dosen::where('username', $request->username)->get();
                return response()->json([
                    'message' => 'Password berhasil di Update',
                ]);
            } else if ($updateDosen==0){
                return response()->json([
                    'message' => 'Password gagal di Update'
                ]);
            }
        } else{
            return response()->json([
                'message' => "password salah"
            ]);
        }
    }


    public function changeMahasiswaPassword(Request $request, $mahasiswa){
        if(Auth::guard('mahasiswa')->attempt(['username' => $mahasiswa, 'password' => $request->oldPassword])) {
            $updateMahasiswa = Mahasiswa::where('username', $mahasiswa)
            ->update([
                'password' => bcrypt($request->newPassword)
            ]);
            if($updateMahasiswa>0){
                $mhs = Mahasiswa::where('username', $request->username)->get();
                return response()->json([
                    'message' => 'Password berhasil di Update',
                ]);
            } else if ($updateMahasiswa==0){
                return response()->json([
                    'message' => 'Password gagal di Update'
                ]);
            }
        } else{
            return response()->json([
                'message' => "password salah"
            ]);
        }
    }

    public function getAllDosen(Request $request){
        $dosen = Dosen::get();
        return response()->json([
            'message' => 'List dosen berhasil di tampilkan',
            'data' => $dosen
        ]);
    }

    public function getAllMahasiswa(Request $request){
        $mahasiswa = Mahasiswa::get();
        return response()->json([
            'message' => 'List mahasiswa berhasil di tampilkan',
            'data' => $mahasiswa
        ]);
    }

}
