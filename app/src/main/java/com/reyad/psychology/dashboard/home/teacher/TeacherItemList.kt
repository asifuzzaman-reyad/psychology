package com.reyad.psychology.dashboard.home.teacher

data class TeacherItemList(
    val name: String,
    val post: String,
    val phd: String,
    val email: String,
    val mobile: String,
    val interest: String,
    val imageUrl: String,
    val publication: String,
) {
    constructor() : this("", "", "", "", "", "", "", "")
}