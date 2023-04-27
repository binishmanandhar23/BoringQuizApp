package com.treeleaf.quiz

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.treeleaf.quiz.worker.BoringQuizWorker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BoringQuizApplication: Application() {
    private val workerManager by lazy {  WorkManager.getInstance(this) }

    var workInfoLiveData: LiveData<WorkInfo>? = null

    override fun onCreate() {
        cacheQuestions()
        super.onCreate()
    }

    fun cacheQuestions(){
        val cacheWorkRequest = OneTimeWorkRequestBuilder<BoringQuizWorker>().build()
        workerManager.enqueue(cacheWorkRequest)
        workInfoLiveData = workerManager.getWorkInfoByIdLiveData(cacheWorkRequest.id)
    }
}