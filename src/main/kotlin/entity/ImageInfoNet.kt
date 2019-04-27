package entity

data class ImageInfoNet(val id: String, val width: Int, val height: Int, val urls: Url) {
    fun toImageInfoDb() = ImageInfoDb().value(id, width, height, urls.raw)
}