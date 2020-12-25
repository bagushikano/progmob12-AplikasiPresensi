package com.progmobklp12.aplikasipresensi.api;

import com.progmobklp12.aplikasipresensi.model.MessageResponseModel;
import com.progmobklp12.aplikasipresensi.model.dosen.ListDosenResponse;
import com.progmobklp12.aplikasipresensi.model.dosen.LoginDosenResponse;
import com.progmobklp12.aplikasipresensi.model.dosen.UpdatePasswordDosenResponse;
import com.progmobklp12.aplikasipresensi.model.dosen.UpdateProfileDosenResponse;
import com.progmobklp12.aplikasipresensi.model.mahasiswa.ListMahasiswaResponse;
import com.progmobklp12.aplikasipresensi.model.mahasiswa.LoginMahasiswaResponse;
import com.progmobklp12.aplikasipresensi.model.dosen.RegisterDosenResponse;
import com.progmobklp12.aplikasipresensi.model.mahasiswa.RegisterMahasiswaResponse;
import com.progmobklp12.aplikasipresensi.model.presensi.DetailPresensiResponse;
import com.progmobklp12.aplikasipresensi.model.presensi.PresensiCreateResponse;
import com.progmobklp12.aplikasipresensi.model.presensi.PresensiDosenResponse;
import com.progmobklp12.aplikasipresensi.model.presensi.PresensiEditResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BaseApi {

    //dosen stuff

    @FormUrlEncoded
    @POST("dosen/login")
    Call<LoginDosenResponse> loginDosen(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("dosen/register")
    Call<RegisterDosenResponse> registerDosen(
            @Field("nama") String nama,
            @Field("nip") String nip,
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("update/dosen/{usernameOld}")
    Call<UpdateProfileDosenResponse> editProfileDosen(
            @Path("usernameOld") String usernameOld,
            @Field("nama") String nama,
            @Field("nip") String nip,
            @Field("username") String usernameNew,
            @Field("password") String password
    );

    @GET("dosen/all")
    Call<ListDosenResponse> listDosenAll();

    //mahasiswa stuff

    @FormUrlEncoded
    @POST("update/dosen/password/{username}")
    Call<UpdatePasswordDosenResponse> updatePasswordDosen(
            @Path("username") String username,
            @Field("oldPassword") String oldPassword,
            @Field("newPassword") String newPassword
    );

    @FormUrlEncoded
    @POST("mahasiswa/login")
    Call<LoginMahasiswaResponse> loginMahasiswa(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("mahasiswa/register")
    Call<RegisterMahasiswaResponse> registerMahasiswa(
            @Field("nama") String nama,
            @Field("nim") String nip,
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("mahasiswa/all")
    Call<ListMahasiswaResponse> listMahasiswaAll();

    // presensi stuff

    @GET("presensi/all/open/{dosen}")
    Call<PresensiDosenResponse> listPresensiOpenDosen(
            @Path("dosen") String username
    );

    @GET("presensi/all/{dosen}")
    Call<PresensiDosenResponse> listPresensiDosen(
            @Path("dosen") String username
    );

    @POST("presensi/close/{presensi}")
    Call<MessageResponseModel> closePresensi(
            @Path("presensi") int presensi
    );

    @POST("presensi/open/{presensi}")
    Call<MessageResponseModel> openPresensi(
            @Path("presensi") int presensi
    );

    @POST("presensi/all/delete/{presensi}")
    Call<MessageResponseModel> deletePresensi (
            @Path("presensi") int presensi
    );

    @GET("presensi/dosen/signed/{presensi}")
    Call<DetailPresensiResponse> detailPresensiDosen (
            @Path("presensi") int presensi
    );

    @POST("presensi/dosen/approved/{detailpresensi}")
    Call<MessageResponseModel> approvePresensi (
            @Path("detailpresensi") int presensi
    );

    @POST("presensi/dosen/decline/{detailpresensi}")
    Call<MessageResponseModel> declinePresensi (
            @Path("detailpresensi") int presensi
    );

    @FormUrlEncoded
    @POST("presensi/new")
    Call<PresensiCreateResponse> createPresensi(
            @Field("username") String username,
            @Field("nama_presensi") String namaPresensi,
            @Field("keterangan") String keterangan,
            @Field("tanggal_open") String tanggalOpen,
            @Field("tanggal_close") String tanggalClose
    );

    @FormUrlEncoded
    @POST("presensi/update/{presensi}")
    Call<PresensiEditResponse> editPresensi(
            @Path("presensi") int presensi,
            @Field("nama_presensi") String namaPresensi,
            @Field("keterangan") String keterangan,
            @Field("tanggal_open") String tanggalOpen,
            @Field("tanggal_close") String tanggalClose
    );

}
