package uz.mobiler.pagingg.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import uz.mobiler.pagingg.models.UserData

interface ApiService {

    @GET("api/users")
    fun getUsers(@Query("page") page: Int): Call<UserData>
}