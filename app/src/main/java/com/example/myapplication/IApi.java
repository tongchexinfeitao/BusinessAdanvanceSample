package com.example.myapplication;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface IApi {
    @FormUrlEncoded
    @POST("user/v2/login")
    Observable<LoginBean> login(@Field("email") String email, @Field("pwd") String pwd);


    @Multipart
    @POST()
    Observable<UploadPhotoBean> uploadPhoto(@Url String url,
                                            @Header("userId") String userId,
                                            @Header("sessionId") String sessionId,
                                            @Part MultipartBody.Part part);

}
