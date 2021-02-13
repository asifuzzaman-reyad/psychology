package com.reyad.psychology.dashboard.home.student

data class StudentItemList(
    val name: String,
    val id: String,
    val session: String,
    val blood: String,
    val hall: String,
    val mobile: String,
    val imageUrl: String,
) {
    constructor() : this( "", "", "", "", "", "", "")
}
