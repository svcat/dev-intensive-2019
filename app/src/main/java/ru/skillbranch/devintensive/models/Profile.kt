package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils

/**
 * Created by Svcat on 2019-08-04.
 */
data class Profile(
    val firstName: String,
    val lastName: String,
    val about: String,
    val repository: String,
    val rating: Int = 0,
    val respect: Int = 0
) {
    val rank: String = "Junior Android Developer"
    val nickName: String = if(firstName.isNotBlank() && lastName.isNotBlank()) Utils.transliteration("$firstName $lastName", "_") else ""

    fun toMap(): Map<String, Any> = mapOf(
            "nickname" to nickName,
            "rank" to rank,
            "firstName" to firstName,
            "lastName" to lastName,
            "about" to about,
            "repository" to repository,
            "rating" to rating,
            "respect" to respect)
}
