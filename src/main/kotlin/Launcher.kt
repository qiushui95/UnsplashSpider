import entity.ImageInfoDb

fun main() {
    var currentCount = dbClient.count(ImageInfoDb())
    val targetCount = currentCount + 100
    var page = 1
    while (currentCount < targetCount) {
        val list = api.getPages(page).execute().body() ?: listOf()
        list.map {
            it.toImageInfoDb()
        }.forEach {
            dbClient.save(it)
        }
        val afterCount = dbClient.count(ImageInfoDb())
        val insertCount = afterCount - currentCount
        val repeatCount = list.size - insertCount
        println("第${page}页读取${list.size}条数据,插入${insertCount}条,重复${repeatCount}条")
        page++
        currentCount = afterCount
    }
    println("数据爬取完成,当前数据量:$currentCount")
}