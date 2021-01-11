package com.reyad.psychology.messenger

data class UserItems(
    val name: String = "",
    val id: String = "",
    val batch: String = "",
    val imageUrl: String = "",
    val userId: String = "",
) {
    constructor() : this("", "","","", "")
}