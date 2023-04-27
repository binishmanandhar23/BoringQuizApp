package com.treeleaf.quiz.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.treeleaf.quiz.network.module.BoringQuizServiceModule
import com.treeleaf.quiz.state.NetworkResponseState

class BoringQuizWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    private val boringQuizService = BoringQuizServiceModule.provideMainModule(context = context)

    override suspend fun doWork(): Result =
        boringQuizService.getQuestions().let { networkResponseState ->
            when (networkResponseState) {
                is NetworkResponseState.Success -> Result.success()
                is NetworkResponseState.Error -> Result.failure(
                    Data.Builder().putString("error", networkResponseState.message).build()
                )
                else -> Result.retry()
            }
        }
}