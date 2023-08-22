package com.example.mangacollectionapp.api

import com.squareup.moshi.Json

data class MangaAPIObject(
    @Json(name =  "mal_id")
    val mal_id : Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "chapters")
    val chapters: String?,
    @Json(name = "volumes")
    val volumes: Int?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "published")
    val published: PublishDate,
    @Json(name = "synopsis")
    val synopsis : String?,
    @Json(name = "authors")
    val authors : List<Author>,
    @Json(name = "genres")
    val genres : List<Genre>,
    @Json(name = "images")
    val image : Image
)

data class Image(
    @Json(name = "jpg")
    val jpg : ImageURL
)

data class ImageURL(
    @Json(name = "image_url")
    val image_url : String

)

data class Genre(
    @Json(name = "name")
    val name : String
)

data class Author(
    @Json(name = "name")
    val name : String
)

data class PublishDate(
    @Json(name = "prop")
    val prop: Prop,
)

data class Prop(
    @Json(name = "from")
    val from: DateDetails,
    @Json(name = "to")
    val to: DateDetails
)

data class DateDetails(
    val day: Int?,
    val month: Int?,
    val year: Int?
)

data class MangaAPIObjectResponse(
    @Json(name="data")
    val data : List<MangaAPIObject>
)
