<?php

use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/
Route::post('dosen/register', 'AuthController@registerDosen');
Route::post('dosen/login', 'AuthController@loginDosen');
Route::get('dosen/all', 'AuthController@getAllDosen'); //get all dosen data

Route::post('mahasiswa/register', 'AuthController@registerMahasiwa');
Route::post('mahasiswa/login', 'AuthController@loginMahasiswa');
Route::get('mahasiswa/all', 'AuthController@getAllMahasiswa'); //get all mahasiswa data

// update data user
Route::post('update/mahasiswa/{mahasiswa}', 'AuthController@editProfilMahasiswa');
Route::post('update/dosen/{dosen}', 'AuthController@editProfilDosen');

// ganti pass user
Route::post('update/mahasiswa/password/{mahasiswa}', 'AuthController@changeMahasiswaPassword');
Route::post('update/dosen/password/{dosen}', 'AuthController@changeDosenPassword');

Route::post('presensi/new', 'CRUDController@newPresensi'); //create presensi
Route::get('presensi/all/{dosen}', 'CRUDController@listPresensiDosen'); //presensi yang di buat oleh dosen yg lagi login
Route::get('presensi/all/open/{dosen}', 'CRUDController@listPresensiOpenDosen'); //presensi yang open yang di buat oleh dosen lagi login
Route::post('presensi/all/delete/{presensi}', 'CRUDController@deletePresensi'); //delete presensi
Route::post('presensi/update/{presensi}', 'CRUDController@editPresensi');
Route::post('presensi/open/{presensi}', 'CRUDController@openPresensi');
Route::post('presensi/close/{presensi}', 'CRUDController@closePresensi');

Route::get('presensi/mahasiswa/all', 'CRUDController@allPresensi'); //semua presensi dari semua dosen
Route::post('presensi/mahasiswa/detail', 'CRUDController@detailPresensiMahasiswa'); //detail presensi untuk mahasiswa yg udh ngisi
// in case needed start here
Route::get('presensi/mahasiswa/close', 'CRUDController@listClosePresensi');
Route::get('presensi/mahasiswa/open', 'CRUDController@listOpenPresensi');
// in case needed stop
Route::post('presensi/mahasiswa/sign', 'CRUDController@newDetailPresensi'); //isi presensi buat mahasiswa (create detail presensi)
Route::post('presensi/mahasiswa/getpresensi', 'CRUDController@mahasiswaPresensi'); //presensi with dosen

// in case needed
Route::get('presensi/dosen/signed/{id_presensi}', 'CRUDController@listDetailPresensi');
Route::get('presensi/dosen/unapproved/{id_presensi}', 'CRUDController@listDetailPresensiClose');
Route::get('presensi/dosen/approved/{id_presensi}', 'CRUDController@listDetailPresensiOpen');
Route::post('presensi/dosen/approved/{id_presensi}', 'CRUDController@approvedAbsensi');
Route::post('presensi/dosen/decline/{id_presensi}', 'CRUDController@declineAbsensi');
