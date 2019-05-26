import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    0.until(10)
        .map {
            launch(Dispatchers.IO) {
                println(it)
                delay(2000)
            }
        }.forEach {
            it.join()
        }
    Unit
}