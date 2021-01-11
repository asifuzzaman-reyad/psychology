package com.reyad.psychology.dashboard.study

class PdfItemList(
    val teacher: String,
    val code: String,
    val lessonNo: String,
    val lessonTopic: String,
    val fileUrl: String
) {
    constructor() : this("", "","", "", "")
}