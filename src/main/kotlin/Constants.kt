import com.mongodb.MongoClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private val retrofit = Retrofit.Builder().baseUrl("https://unsplash.com/napi/")
    .addConverterFactory(GsonConverterFactory.create())
//    .addConverterFactory(ScalarsConverterFactory.create())
    .build()

val api = retrofit.create(UnsplashApi::class.java)

val dbClient = MongoClient("45.32.10.81", 27017)