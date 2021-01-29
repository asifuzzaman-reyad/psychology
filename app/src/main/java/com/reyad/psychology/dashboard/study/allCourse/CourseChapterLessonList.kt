package com.reyad.psychology.dashboard.study.allCourse

class CourseChapterLessonList(
    val chapter: String,
    val serialNo: String,
    val lessonName: String,
    val source: String,
    val fileUrl: String,
) {
    constructor() : this("", "", "", "", "")
}