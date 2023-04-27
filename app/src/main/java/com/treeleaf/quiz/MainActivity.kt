package com.treeleaf.quiz

import android.app.usage.UsageEvents
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.work.WorkInfo
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.treeleaf.quiz.components.snackbar.*
import com.treeleaf.quiz.navigators.customAnimatedComposable
import com.treeleaf.quiz.navigators.navigator
import com.treeleaf.quiz.navigators.rememberCustomAnimatedNavController
import com.treeleaf.quiz.screens.*
import com.treeleaf.quiz.ui.theme.BoringQuizAppTheme
import com.treeleaf.quiz.viewmodel.BoringQuizViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val quizViewModel by viewModels<BoringQuizViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            BoringQuizAppTheme {
                val workInfoLiveData by (applicationContext as BoringQuizApplication).workInfoLiveData!!.observeAsState()
                val alreadyLoggedIn by quizViewModel.alreadyLoggedIn.collectAsState()
                val mainNavController = rememberCustomAnimatedNavController()
                val snackBarState = rememberSnackBarState()
                val questionsCount by quizViewModel.questionsCount.collectAsState()
                var emptyDialog by remember {
                    mutableStateOf(false)
                }
                var forcefulCheck by remember {
                    mutableStateOf(UsageEvents.Event())
                }

                LaunchedEffect(key1 = alreadyLoggedIn) {
                    if (alreadyLoggedIn)
                        mainNavController.navigateToQuestionScreen()
                }

                LaunchedEffect(key1 = workInfoLiveData, key2 = questionsCount, key3 = forcefulCheck) {
                     when (workInfoLiveData?.state) {
                        WorkInfo.State.FAILED, WorkInfo.State.CANCELLED, WorkInfo.State.SUCCEEDED ->
                            if (mainNavController.currentDestination?.route == Screen.QuizSection.QuestionScreen.route)
                                emptyDialog = questionsCount == 0
                        else -> Unit
                    }
                }

                LaunchedEffect(key1 = workInfoLiveData){
                    if(workInfoLiveData?.state == WorkInfo.State.SUCCEEDED && questionsCount == 0)
                        emptyDialog = false.also {
                            quizViewModel.getUnansweredQuestions()
                        }
                }

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EmptyDialog(showDialog = emptyDialog) {
                        finishAffinity()
                    }
                    GlobalDialog(
                        state = workInfoLiveData?.state,
                        message = workInfoLiveData?.outputData?.getString("error")
                    )
                    MainNavHost(
                        mainNavController = mainNavController,
                        snackBarState = snackBarState,
                        forcefulCheck = {
                            forcefulCheck = UsageEvents.Event()
                        }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
    @Composable
    private fun MainNavHost(
        mainNavController: NavHostController,
        snackBarState: SnackBarState,
        startDestination: Screen = Screen.OnBoarding.WelcomeScreen,
        forcefulCheck: () -> Unit
    ) {
        CustomSnackBar(
            snackBarModifier = Modifier.padding(bottom = 10.dp),
            text = "",
            snackBarState = snackBarState,
            useBox = true
        ) {
            AnimatedNavHost(
                navController = mainNavController,
                startDestination = startDestination.route
            ) {
                customAnimatedComposable(route = Screen.OnBoarding.WelcomeScreen.route) {
                    WelcomeScreen {
                        mainNavController.navigator(route = Screen.OnBoarding.NameScreen.route)
                    }
                }
                customAnimatedComposable(route = Screen.OnBoarding.NameScreen.route) {
                    NameScreen(nameState = { quizViewModel.nameState }, onNameChange = {
                        quizViewModel.updateUserName(it)
                    }, onSaveName = {
                        quizViewModel.saveUserName()
                        mainNavController.navigateToQuestionScreen()
                        forcefulCheck()
                    }, onError = {
                        snackBarState.show(
                            overridingText = it,
                            overridingDelay = SnackBarLengthMedium
                        )
                    })
                }
                customAnimatedComposable(route = Screen.QuizSection.QuestionScreen.route) {

                    val timerState by quizViewModel.timerState.collectAsState()
                    val listOfQuestions by quizViewModel.questionsState.collectAsState()
                    val currentIndex by quizViewModel.currentIndex.collectAsState()
                    val pagerState = remember {
                        PagerState(initialPage = currentIndex)
                    }

                    LaunchedEffect(key1 = currentIndex) {
                        pagerState.animateScrollToPage(currentIndex)
                    }

                    QuizQuestionScreenComponents(
                        pagerState = pagerState,
                        timerMillis = timerState,
                        listOfQuestions = listOfQuestions,
                        onNextQuestion = {
                            quizViewModel.updateIndex(pagerState.currentPage + 1)
                        },
                        onTip = {
                            snackBarState.show(
                                overridingText = it,
                                overridingDelay = SnackBarLengthLong
                            )
                        },
                        onSubmit = {
                            mainNavController.navigateToResultScreen(isTimeOut = false)
                        },
                        onAnswerClick = {
                            quizViewModel.onUpdateUsersAnswer(answerDTO = it)
                        })
                }

                customAnimatedComposable(
                    route = Screen.QuizSection.ResultScreen.route + "/{isTimeOut}",
                    enterTransitionDirection = AnimatedContentScope.SlideDirection.Up,
                    exitTransitionDirection = AnimatedContentScope.SlideDirection.Up,
                    popEnterTransitionDirection = AnimatedContentScope.SlideDirection.Down,
                    popExitTransitionDirection = AnimatedContentScope.SlideDirection.Down,
                ) {
                    it.arguments?.getString("isTimeOut")?.toBooleanStrictOrNull()
                        ?.let { isTimeOut ->
                            val name by quizViewModel.nameState.collectAsState()
                            val score by quizViewModel.score.collectAsState()
                            val totalQuestions by quizViewModel.questionsState.collectAsState()

                            ResultScreen(name = name, score = score, isTimeOut = isTimeOut, totalSize = totalQuestions.size) {
                                (applicationContext as? BoringQuizApplication)?.cacheQuestions() /*Call cache again*/
                                mainNavController.navigateToQuestionScreen()
                            }
                        }
                }
            }
        }
    }

    private fun NavHostController.navigateToQuestionScreen() =
        navigator(route = Screen.QuizSection.QuestionScreen.route, clearBackStack = true).also {
            quizViewModel.startTimer(finishListener = {
                navigateToResultScreen(isTimeOut = true)
            })
            quizViewModel.reset()
            if (quizViewModel.questionsState.value.isEmpty())
                quizViewModel.getUnansweredQuestions()
            quizViewModel.getNumberOfUnansweredQuestions()
        }

    private fun NavHostController.navigateToResultScreen(isTimeOut: Boolean) =
        navigator(route = Screen.QuizSection.ResultScreen.route + "/$isTimeOut").also {
            quizViewModel.calculateScore()
        }
}