package com.treeleaf.quiz.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.treeleaf.quiz.R
import com.treeleaf.quiz.dto.AnswerDTO
import com.treeleaf.quiz.dto.QuestionDTO
import com.treeleaf.quiz.utils.TimerUtils
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuizQuestionScreenComponents(
    pagerState: PagerState,
    timerMillis: Long,
    listOfQuestions: List<QuestionDTO>,
    onAnswerClick: (AnswerDTO) -> Unit,
    onNextQuestion: (page: Int) -> Unit,
    onTip: (String) -> Unit, onSubmit: () -> Unit
) {
    val currentPage by remember(pagerState.currentPage) {
        derivedStateOf { pagerState.currentPage }
    }
    val questionDTO by remember(listOfQuestions, currentPage) {
        derivedStateOf {
            listOfQuestions.getOrNull(currentPage)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        QuestionNumber(questionNumber = currentPage + 1)
        Timer(millis = timerMillis)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AnimatedVisibility(visible = questionDTO != null) {
                HorizontalPager(
                    state = pagerState,
                    pageCount = listOfQuestions.size,
                    userScrollEnabled = false
                ) {
                    var selectedAnswer: AnswerDTO? by remember {
                        mutableStateOf(null)
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.7f),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        stickyHeader {
                            QuestionDesign(question = questionDTO!!.question)
                        }
                        questionAnswers(
                            selectedAnswer = selectedAnswer,
                            listOfAnswers = questionDTO!!.answersDTO,
                            onClick = { answer ->
                                selectedAnswer = answer
                                onAnswerClick(answer)
                            }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(fraction = 0.8f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(visible = !questionDTO?.tip.isNullOrEmpty()) {
                    TipButton(onTip = { onTip(questionDTO?.tip!!) })
                }
            }
        }
        QuestionButton(last = currentPage + 1 == listOfQuestions.size, onNext = {
            onNextQuestion(currentPage)
        }, onSubmit = onSubmit)
    }
}

@Composable
private fun QuestionNumber(questionNumber: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp), horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Question ",
            style = MaterialTheme.typography.headlineSmall,
        )
        CounterAnimation(count = questionNumber) {
            Text(
                text = it,
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    }
}

@Composable
private fun QuestionDesign(question: String?) {
    CustomCard {
        AutoSizeText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 20.dp),
            text = question ?: "",
            style = MaterialTheme.typography.headlineSmall.copy(textAlign = TextAlign.Center),
        )
    }
}


private fun LazyListScope.questionAnswers(
    selectedAnswer: AnswerDTO?,
    listOfAnswers: List<AnswerDTO>,
    onClick: (AnswerDTO) -> Unit
) {
    this@questionAnswers.item { Spacer(modifier = Modifier.size(10.dp)) }
    this@questionAnswers.items(listOfAnswers) {
        QuestionAnswer(
            modifier = Modifier.fillMaxWidth(0.7f).padding(vertical = 10.dp),
            isSelected = selectedAnswer?.answer == it.answer,
            answerDTO = it,
            onClick = onClick)
    }
    this@questionAnswers.item { Spacer(modifier = Modifier.size(10.dp)) }
}

@Composable
private fun QuestionAnswer(
    modifier: Modifier,
    isSelected: Boolean,
    answerDTO: AnswerDTO,
    onClick: (AnswerDTO) -> Unit
) {
    val backgroundColor by animateColorAsState(targetValue = if (isSelected) Color.Green else MaterialTheme.colorScheme.primary)
    val textColor by animateColorAsState(targetValue = if (isSelected) Color.Black else MaterialTheme.colorScheme.onPrimary)
    CustomCard(
        modifier = modifier,
        onClick = {
            onClick(answerDTO)
        },
        fullWidthFraction = 1f,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            text = answerDTO.answer,
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                color = textColor
            ),
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun QuestionButton(last: Boolean = false, onNext: () -> Unit, onSubmit: () -> Unit) {
    val hapticFeedback = LocalHapticFeedback.current
    Button(onClick = {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        if (last)
            onSubmit()
        else
            onNext()
    }, contentPadding = PaddingValues(horizontal = 35.dp, vertical = 12.dp)) {
        AnimatedContent(targetState = last) {
            Text(
                text = if (last) "Submit" else "Next",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TipButton(onTip: () -> Unit) =
    Card(onClick = onTip) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Info, contentDescription = "Tip")
            Text(text = "Tip")
        }
    }

@Composable
private fun Timer(millis: Long) {
    var po0 by remember {
        mutableStateOf(0)
    }
    var po1 by remember {
        mutableStateOf(0)
    }
    var po2 by remember {
        mutableStateOf(0)
    }
    var po3 by remember {
        mutableStateOf(0)
    }

    val spinAnimation by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = TimeUnit.MINUTES.toMillis(
                    2L
                ).toInt(), easing = LinearEasing
            ), repeatMode = RepeatMode.Restart
        )
    )

    val progress by remember(millis) {
        derivedStateOf {
            (TimeUnit.MINUTES.toMillis(2L) - millis) / TimeUnit.MINUTES.toMillis(2L).toFloat()
        }
    }

    val scaleAnimation by rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = when {
                    progress > 0.9f -> 300
                    progress > 0.8f -> 400
                    progress > 0.6f -> 600
                    else -> 1000
                }, easing = FastOutSlowInEasing
            ), repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(key1 = millis) {
        TimerUtils.convertMillisToIndividualDigits(millis) { p0, p1, p2, p3 ->
            po0 = p0
            po1 = p1
            po2 = p2
            po3 = p3
        }
    }

    Card(elevation = CardDefaults.cardElevation(10.dp)) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                CircularProgressIndicator(
                    progress = progress,
                    modifier = Modifier.size(33.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp, strokeCap = StrokeCap.Round
                )
                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.Center)
                        .graphicsLayer {
                            if (progress != 1.0f) {
                                scaleX = scaleAnimation
                                scaleY = scaleAnimation
                                rotationZ = spinAnimation
                            }
                        },
                    painter = painterResource(id = R.mipmap.boring_quiz_app),
                    contentDescription = "App Icon"
                )
            }
            Row {
                CounterAnimation(count = po0) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
                CounterAnimation(count = po1) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
                Text(
                    text = ":",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                )
                CounterAnimation(count = po2) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
                CounterAnimation(count = po3) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }
    }
}

