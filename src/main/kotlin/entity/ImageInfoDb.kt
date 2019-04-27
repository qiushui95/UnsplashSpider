package entity

import ninja.sakib.pultusorm.annotations.PrimaryKey

class ImageInfoDb {
    @PrimaryKey
    var id: String = ""
    var width: Int = 0
    var height: Int = 0
    var url: String = ""
    fun value(id: String, width: Int, height: Int, url: String): ImageInfoDb {
        this.id = id
        this.width = width
        this.height = height
        this.url = url
        return this
    }
}