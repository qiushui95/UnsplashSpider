import entity.ImageInfo
import retrofit2.Call
import retrofit2.http.*

interface UnsplashApi {
    companion object {
        const val OFFSET = 10
    }

    @Headers(
        "User-Agent:Mozilla/5.0 (Windows NT 10.0; WOW64; rv:60.0) Gecko/20100101 Firefox/60.0",
        "X-Requested-With:XMLHttpRequest",
        "Referer:https://unsplash.com/",
        "Host:unsplash.com",
        "Connection:keep-alive"
    )
    @GET("photos?per_page=10&order_by=latest")
    fun getPages(@Query("page") page: Long, @Query("per_page") offset: Int = OFFSET): Call<List<ImageInfo>>
}
