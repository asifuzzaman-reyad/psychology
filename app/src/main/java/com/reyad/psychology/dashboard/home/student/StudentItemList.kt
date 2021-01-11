package com.reyad.psychology.dashboard.home.student

data class StudentItemList(
    val name: String,
    val id: String,
    val session: String,
    val imageUrl: String,
) {
    constructor() : this( "", "", "", "")
}
