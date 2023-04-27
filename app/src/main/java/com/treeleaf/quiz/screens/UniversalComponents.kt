package com.treeleaf.quiz.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.work.WorkInfo
import java.lang.Float.max


@Composable
fun GlobalDialog(
    state: WorkInfo.State? = WorkInfo.State.FAILED,
    message: String? = "Found an error when trying to fetch questions."
) {
    var displayDialog by remember(state) {
        mutableStateOf(state == WorkInfo.State.FAILED || state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED)
    }
    if (displayDialog)
        when (state) {
            WorkInfo.State.FAILED -> Dialog(onDismissRequest = {
                displayDialog = false
            }) {
                Card {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Error, contentDescription = "Error")
                        Text(
                            "Oops! There's an error",
                            style = TextStyle(fontWeight = FontWeight.SemiBold)
                        )
                        Text(text = message ?: "", textAlign = TextAlign.Center)
                        Button(modifier = Modifier.padding(top = 10.dp), onClick = {
                            displayDialog = false
                        }) {
                            Text(text = "Use in offline mode")
                        }
                    }
                }
            }
            else -> Dialog(
                onDismissRequest = { },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
}

@Composable
fun EmptyDialog(showDialog: Boolean, onDismiss: () -> Unit) {
    if (showDialog)
        Dialog(onDismissRequest = onDismiss, properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)) {
            Card {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Error, contentDescription = "Error")
                    Text(
                        "Oops!",
                        style = TextStyle(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "There seems to be no more questions left.",
                        textAlign = TextAlign.Center
                    )
                    Button(modifier = Modifier.padding(top = 10.dp), onClick = onDismiss) {
                        Text(text = "Close app")
                    }
                }
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        cursorColor = Color.White
    ),
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = Color.White,
        fontWeight = FontWeight.W500
    ),
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        maxLines = maxLines,
        label = label,
        colors = colors,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    fullWidthFraction: Float = 0.8f,
    colors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) =
    if (onClick == null)
        Card(
            modifier = modifier.fillMaxWidth(fullWidthFraction),
            colors = colors,
            elevation = elevation,
            shape = shape
        ) {
            content(this)
        } else
        Card(
            modifier = modifier.fillMaxWidth(fullWidthFraction),
            onClick = onClick,
            colors = colors,
            elevation = elevation,
            shape = shape
        ) {
            content(this)
        }

@Composable
fun AppName() = Text(
    text = "Boring Quiz App",
    textAlign = TextAlign.Center,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
    lineHeight = 40.sp
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CounterAnimation(
    count: Int,
    content: @Composable AnimatedVisibilityScope.(targetCount: String) -> Unit
) = AnimatedContent(
    targetState = count,
    transitionSpec = {
        // Compare the incoming number with the previous number.
        if (targetState > initialState) {
            // If the target number is larger, it slides up and fades in
            // while the initial (smaller) number slides up and fades out.
            slideInVertically { height -> height } + fadeIn() with
                    slideOutVertically { height -> -height } + fadeOut()
        } else {
            // If the target number is smaller, it slides down and fades in
            // while the initial number slides down and fades out.
            slideInVertically { height -> -height } + fadeIn() with
                    slideOutVertically { height -> height } + fadeOut()
        }.using(
            // Disable clipping since the faded slide-in/out should
            // be displayed out of bounds.
            SizeTransform(clip = false)
        )
    }
) { targetCount ->
    content(targetCount.toString())
}

@Composable
fun AutoSizeText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    lineCount: Int = 4
) {
    var resizableTextSize by remember(style) {
        mutableStateOf(style)
    }
    val defaultFontSize = remember {
        16.sp
    }
    val defaultLineHeight = remember {
        10.sp
    }
    var shouldDraw by remember {
        mutableStateOf(false)
    }


    Text(modifier = modifier.drawWithContent {
        if (shouldDraw)
            drawContent()
    }, text = text, style = resizableTextSize, onTextLayout = {
        if (it.lineCount > lineCount) {
            resizableTextSize =
                resizableTextSize.copy(
                    fontSize = (if (resizableTextSize.fontSize.isUnspecified) defaultFontSize else max(
                        (resizableTextSize.fontSize.value) * 0.95f,
                        12f
                    ).sp),
                    lineHeight = (if (resizableTextSize.lineHeight.isUnspecified) defaultLineHeight else max(
                        (resizableTextSize.lineHeight.value) * 0.95f,
                        12f
                    ).sp)
                )
        } else
            shouldDraw = true
    }, softWrap = true)
}