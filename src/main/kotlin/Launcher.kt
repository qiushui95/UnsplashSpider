import com.mongodb.client.MongoCollection
import entity.ImageInfo
import kotlinx.coroutines.*
import org.bson.Document
import java.lang.Exception
import java.util.*

fun main() = runBlocking {
    val database = dbClient.getDatabase("unsplash")
    val imageCollection = database.getCollection("Images")
    println("当前数量:${imageCollection.countDocuments()}")
    println("请输入需要新增的数量:")
    val scanner = Scanner(System.`in`)
    val inputCount = scanner.nextInt().toLong()
//    val inputCount = 100L
    startPull(1L, inputCount, imageCollection)
    println("结束数量:${imageCollection.countDocuments()}")
}

suspend fun CoroutineScope.startPull(startPage: Long, pullCount: Long, collection: MongoCollection<Document>) {
    println("开始新一轮数据获取")
    val currentCount = collection.countDocuments()
    val needPage = pullCount / UnsplashApi.OFFSET + if (pullCount % UnsplashApi.OFFSET == 0L) {
        0
    } else {
        1
    }
    startPage.until(startPage + needPage).step(10)
        .map {
            it.until(it + 10)
                .map {
                    launch(Dispatchers.IO, start = CoroutineStart.LAZY) {
                        pullPage(it, collection)
                    }
                }
        }.forEach {
            it.forEach {
                it.start()
            }
            it.forEach {
                it.join()
            }
        }
    val afterCount = collection.countDocuments()
    println("本轮执行完成,需要获取${pullCount}新数据,共计获取${afterCount - currentCount},重复${pullCount - afterCount + currentCount}")
    if (afterCount < currentCount + needPage) {
        startPull(startPage + needPage + 1, pullCount - afterCount + currentCount, collection)
    }
}

suspend fun CoroutineScope.pullPage(page: Long, collection: MongoCollection<Document>) {
    println("开始获取第${page}页数据")
    val list = try {
        api.getPages(page).execute().body()
    } catch (e: Exception) {

    }
    (list as? List<ImageInfo>)?.map {
        Document().append("id", it.id)
            .append("width", it.width)
            .append("height", it.height)
            .append("color", it.color)
            .append(
                "urls", Document()
                    .append("raw", it.urls.raw)
                    .append("full", it.urls.full)
                    .append("regular", it.urls.regular)
                    .append("small", it.urls.small)
                    .append("thumb", it.urls.thumb)
            )
    }?.map {
        launch(Dispatchers.IO) {
            saveImage(it, collection)
        }
    }?.forEach {
        it.join()
    }
    println("第${page}页数据获取完成")
}

fun saveImage(document: Document, collection: MongoCollection<Document>) {
    try {
        collection.insertOne(document)
    } catch (e: Exception) {
    }
}
