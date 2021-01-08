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
        $id = Dosen::where('username', $request->username)->get()->first();
        $presensi = Presensi::create([
            'id_dosen' => $id->id_dosen,
            'nama_presensi' => $request->nama_presensi,
            'keterangan' => $request->keterangan,
            'tanggal_open' => $request->tanggal_open,
            'tanggal_close' => $request->tanggal_close,
            'is_open' => 0
        ]);

        $notiftitle = "Ada presensi baru oleh ". $id->nama;
        $notifcontent = "Presensi baru dengan nama ". $request->nama_presensi;

        $url = 'https://fcm.googleapis.com/fcm/send';
        $fields = array(
            "to" => "/topics/all",
            "android" => array (
                "notification"=> array (
                    "tag" => "presensinew"
                )
            ),
            "data" => array(
                "title" => $notiftitle,
                "body" => $notifcontent,
                "type" => "all"
            )
        );
        $headers = array(
            'Authorization: key=AAAARvWFBSk:APA91bHfVZVkufvKbriuO7McpV-CguHTWwa7e9nuswg18F7N3qSjgEefKJsDqZTAKcZj26x0mEYgGaymJ_WtDuApADGhUsI9IbdxQJn1YTo4GC-Q738Rq4uvWabsbQ1pFTbr2k_o1T2Z',
            'Content-type: Application/json'
        );

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
        curl_exec($ch);
        curl_close($ch);

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
        $date = now()->setTimezone('GMT+8');
        $updatePresensi = Presensi::where('id_presensi', $presensi)
        ->update([
            'is_open' => 1,
            'tanggal_open' => $date
        ]);

        $presensi = Presensi::where('id_presensi', $presensi)->get()->first();

        $notiftitle = "Presensi di buka";
        $notifcontent = "Presensi dengan nama ". $presensi->nama_presensi ." telah di buka";

        $url = 'https://fcm.googleapis.com/fcm/send';
        $fields = array(
            "to" => "/topics/all",
            "android" => array (
                "notification"=> array (
                    "tag" => "presensiopen"
                )
            ),
            "data" => array(
                "title" => $notiftitle,
                "body" => $notifcontent,
                "type" => "all"
            )
        );
        $headers = array(
            'Authorization: key=AAAARvWFBSk:APA91bHfVZVkufvKbriuO7McpV-CguHTWwa7e9nuswg18F7N3qSjgEefKJsDqZTAKcZj26x0mEYgGaymJ_WtDuApADGhUsI9IbdxQJn1YTo4GC-Q738Rq4uvWabsbQ1pFTbr2k_o1T2Z',
            'Content-type: Application/json'
        );

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
        curl_exec($ch);
        curl_close($ch);

        return response()->json([
            'message' => 'Absensi berhasil di buka'
        ]);
    }


    public function closePresensi(Request $request, $presensi){
        $date = now()->setTimezone('GMT+8');
        $updatePresensi = Presensi::where('id_presensi', $presensi)
        ->update([
            'is_open' => 0,
            'tanggal_close'=> $date
        ]);

        $presensi = Presensi::where('id_presensi', $presensi)->get()->first();

        $notiftitle = "Presensi di tutup";
        $notifcontent = "Presensi dengan nama ". $presensi->nama_presensi ." telah di tutup";

        $url = 'https://fcm.googleapis.com/fcm/send';
        $fields = array(
            "to" => "/topics/all",
            "android" => array (
                "notification"=> array (
                    "tag" => "presensiclose"
                )
            ),
            "data" => array(
                "title" => $notiftitle,
                "body" => $notifcontent,
                "type" => "all"
            )
        );
        $headers = array(
            'Authorization: key=AAAARvWFBSk:APA91bHfVZVkufvKbriuO7McpV-CguHTWwa7e9nuswg18F7N3qSjgEefKJsDqZTAKcZj26x0mEYgGaymJ_WtDuApADGhUsI9IbdxQJn1YTo4GC-Q738Rq4uvWabsbQ1pFTbr2k_o1T2Z',
            'Content-type: Application/json'
        );

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
        curl_exec($ch);
        curl_close($ch);

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

    public function detailPresensiMahasiswa(Request $request){
        $id = Mahasiswa::where('username', $request->mahasiswa)->get('id_mahasiswa')->first();
        $presensi = DetailPresensi::where('id_mahasiswa', $id->id_mahasiswa)->where('id_presensi', $request->presensi)->get();
        if($presensi->count()>0) {
            return response()->json([
                'message' => 'Presensi telah di isi',
                'data' => $presensi
            ]);
        }
        else{
            return response()->json([
                'message' => 'Presensi blm di isi',
            ]);
        }

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

    public function mahasiswaPresensi(Request $request){
        $presensi = Presensi::with('dosen')->where('id_presensi', $request->presensi)->get();
        return response()->json([
            'message' => 'List Absensi berhasil ditampilkan',
            'data' => $presensi
        ]);
    }

    public function newDetailPresensi(Request $request){
        $id_mahasiswa = Mahasiswa::where('username', $request->username)->get('id_mahasiswa')->first();
        $presensi = DetailPresensi::create([
            'id_presensi' => $request->id_presensi,
            'id_mahasiswa' => $id_mahasiswa->id_mahasiswa,
            'is_approved' => 0,
            'date_filled'=> now()->setTimezone('GMT+8')
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

        $detailpresensi = DetailPresensi::where('id_detail_presensi', $detailPresensi)->get()->first();
        $presensi = Presensi::where("id_presensi", $detailpresensi->id_presensi)->get()->first();
        $mahasiswa = Mahasiswa::where("id_mahasiswa", $detailpresensi->id_mahasiswa)->get()->first();

        $notiftitle = "Presensi anda di approve";
        $notifcontent = "Presensi anda pada ". $presensi->nama_presensi ." telah di approve";

        $url = 'https://fcm.googleapis.com/fcm/send';
        $fields = array(
            "to" => "/topics/all",
            "android" => array (
                "notification"=> array (
                    "tag" => "approvenotif"
                )
            ),
            "data" => array(
                "title" => $notiftitle,
                "body" => $notifcontent,
                "type" => "notall",
                "username" => $mahasiswa->username
            )
        );
        $headers = array(
            'Authorization: key=AAAARvWFBSk:APA91bHfVZVkufvKbriuO7McpV-CguHTWwa7e9nuswg18F7N3qSjgEefKJsDqZTAKcZj26x0mEYgGaymJ_WtDuApADGhUsI9IbdxQJn1YTo4GC-Q738Rq4uvWabsbQ1pFTbr2k_o1T2Z',
            'Content-type: Application/json'
        );

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
        curl_exec($ch);
        curl_close($ch);


        return response()->json([
            'message' => 'Absensi approved'
        ]);
    }

    public function declineAbsensi(Request $request, $detailPresensi){
        $approvedPresensi = DetailPresensi::where('id_detail_presensi', $detailPresensi)
        ->update([
            'is_approved' => 0
        ]);

        $detailpresensi = DetailPresensi::where('id_detail_presensi', $detailPresensi)->get()->first();
        $presensi = Presensi::where("id_presensi", $detailpresensi->id_presensi)->get()->first();
        $mahasiswa = Mahasiswa::where("id_mahasiswa", $detailpresensi->id_mahasiswa)->get()->first();

        $notiftitle = "Presensi anda di decline";
        $notifcontent = "Presensi anda pada ". $presensi->nama_presensi ." telah di decline";

        $url = 'https://fcm.googleapis.com/fcm/send';
        $fields = array(
            "to" => "/topics/all",
            "android" => array (
                "notification"=> array (
                    "tag" => "declinenotif"
                )
            ),
            "data" => array(
                "title" => $notiftitle,
                "body" => $notifcontent,
                "type" => "notall",
                "username" => $mahasiswa->username
            )
        );
        $headers = array(
            'Authorization: key=AAAARvWFBSk:APA91bHfVZVkufvKbriuO7McpV-CguHTWwa7e9nuswg18F7N3qSjgEefKJsDqZTAKcZj26x0mEYgGaymJ_WtDuApADGhUsI9IbdxQJn1YTo4GC-Q738Rq4uvWabsbQ1pFTbr2k_o1T2Z',
            'Content-type: Application/json'
        );

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
        curl_exec($ch);
        curl_close($ch);

        return response()->json([
            'message' => 'Absensi decline'
        ]);
    }
}
