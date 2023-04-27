package com.treeleaf.quiz.viewmodel

import android.app.usage.UsageEvents
import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.treeleaf.quiz.dto.AnswerDTO
import com.treeleaf.quiz.dto.QuestionDTO
import com.treeleaf.quiz.dto.toDTO
import com.treeleaf.quiz.repository.QuizRepository
import com.treeleaf.quiz.utils.TimerUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class BoringQuizViewModel @Inject constructor(private val quizRepository: QuizRepository) :
    ViewModel() {
    var countDownTimer: CountDownTimer? = null

    init {
        initializeTimer()
    }

    private var _timerState = MutableStateFlow(0L)
    val timerState = _timerState.asStateFlow()

    private var _nameState = MutableStateFlow("")
    val nameState = _nameState.asStateFlow()

    private var _questionsState = MutableStateFlow(emptyList<QuestionDTO>())
    val questionsState = _questionsState.asStateFlow()

    private var _alreadyLoggedIn = MutableStateFlow(false)
    val alreadyLoggedIn = _alreadyLoggedIn.asStateFlow()

    private var _currentIndex = MutableStateFlow(0)
    val currentIndex = _currentIndex.asStateFlow()

    private var _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    private var _questionsCount = MutableStateFlow(-1)
    val questionsCount = _questionsCount.asStateFlow()

    private var _collectAgain = MutableStateFlow(UsageEvents.Event())
    private val collectAgain = _collectAgain.asStateFlow()

    var finishListener: (() -> Unit)? = null

    init {
        getUserNameFromDatabase()
        getUnansweredQuestions()
    }

    private fun getUserNameFromDatabase() = viewModelScope.launch(Dispatchers.IO) {
        val name = quizRepository.getUserName()
        _alreadyLoggedIn.update { name.isNotEmpty() }
        updateUserName(name)
    }

    fun saveUserName() = viewModelScope.launch(Dispatchers.IO) {
        quizRepository.saveUserName(nameState.value)
    }

    fun updateUserName(name: String) = _nameState.update { name }

    fun updateIndex(currentIndex: Int) = _currentIndex.update { currentIndex }

    fun getNumberOfUnansweredQuestions() = viewModelScope.launch(Dispatchers.Default) {
        _questionsCount.update {
            val count = quizRepository.getNumberOfUnansweredQuestions().count { it.usersAnsweredCorrectly == null }
            count
        }
    }

    fun getUnansweredQuestions() = viewModelScope.launch(Dispatchers.IO) {
        combine(
            flow = quizRepository.getUnansweredQuestions(),
            flow2 = collectAgain
        ) { questions, collectAgain ->
            Pair(questions, collectAgain)
        }.collect { pair ->
            val list = pair.first
            _questionsState.update { questions ->
                questions.ifEmpty { list.filter { it.usersAnsweredCorrectly == null }.take(20).map { it.toDTO() }.also {
                    _questionsCount.update { _ -> it.size }
                } }
            }
            if(timerState.value == 0L)
                startTimer(finishListener = finishListener)
        }
    }

    fun onUpdateUsersAnswer(answerDTO: AnswerDTO) = viewModelScope.launch(Dispatchers.IO) {
        quizRepository.updateUsersAnswer(
            id = answerDTO.questionId,
            answer = answerDTO.correctAnswer.toString()
        )
    }

    fun calculateScore() =
        viewModelScope.launch(Dispatchers.Default) {
            val allQuestions = questionsState.value
            _score.update {
                quizRepository.getQuestionsByIds(allQuestions.mapNotNull { it.id })
                    .count { it.usersAnsweredCorrectly?.toBooleanStrictOrNull() ?: false }
            }
        }


    private fun initializeTimer() {
        countDownTimer =
            object : CountDownTimer(TimeUnit.MINUTES.toMillis(2L), TimeUnit.SECONDS.toMillis(1L)) {
                override fun onTick(p0: Long) {
                    _timerState.update { p0 }
                }

                override fun onFinish() {
                    _timerState.update { 0L }
                    finishListener?.invoke()
                }
            }
    }

    fun startTimer(finishListener: (() -> Unit)? = null) {
        this.finishListener = finishListener
        if(questionsState.value.isNotEmpty()) {
            countDownTimer?.cancel()
            countDownTimer?.start()
        }
    }

    fun reset() {
        _questionsState.update { emptyList() }
        _collectAgain.update { UsageEvents.Event() }
        _currentIndex.update { 0 }
        _questionsCount.update { -1 }
    }

    override fun onCleared() {
        countDownTimer?.cancel()
    }
}