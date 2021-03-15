package com.mexcelle.clavaxtechnologies

import com.mexcelle.xiacomemechinetest.ImageReposne
import com.mexcelle.xiacomemechinetest.PostResponse
import retrofit2.Call
import retrofit2.http.*


interface Endpoints {
    @FormUrlEncoded
    @POST("getdata.php")
    fun getImageList(
        @Field("user_id") user_id: String,
        @Field("offset") offset: String,
        @Field("type") type: String
    ): Call<ImageReposne>


    @Multipart
    @POST("savedata.php-up")
    fun getPostResponse(
        @Part("first_name") first_name: String?,
        @Part("last_name") last_name: String?,
        @Part("email") email: String?,
        @Part("phone") phone: String?,
        @Part("image") image: String?,
     //   @Part user_image: Part
        //@Part("image") RequestBody image // i have thried them both but they didnt work
        //@Part("image\"; filename=\"pp.jpg\" ") RequestBody image
    ): Call<PostResponse>

}