package com.reyad.psychology.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.reyad.psychology.dashboard.HomeRepository
import com.reyad.psychology.dashboard.home.teacher.TeacherItemList

class HomeViewModel(app: Application): AndroidViewModel(app) {

    private val dataRepo = HomeRepository(app)
    val b13Data = dataRepo.repoData13
    val b14Data = dataRepo.repoData14

}