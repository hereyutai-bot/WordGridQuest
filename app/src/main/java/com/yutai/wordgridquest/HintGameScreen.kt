package com.yutai.wordgridquest

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
@Composable
fun HintWordGameScreen(
    difficulty: HintGameDifficulty,
    onBackToDifficultySelect: () -> Unit,
    onGameFinished: (GameResult) -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var gameQuestions by remember {
        mutableStateOf(hintWordQuestions.shuffled().take(10))
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf("") }

    var score by remember { mutableIntStateOf(0) }
    var correctCount by remember { mutableIntStateOf(0) }
    var wrongCount by remember { mutableIntStateOf(0) }
    var skipCount by remember { mutableIntStateOf(0) }

    var isAnswered by remember { mutableStateOf(false) }
    var isGameFinished by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var hasSavedResult by remember { mutableStateOf(false) }

    var answerFeedback by remember { mutableStateOf("") }

    var timeLeft by remember {
        mutableIntStateOf(difficulty.secondsPerQuestion ?: 0)
    }

    val questionResults = remember {
        mutableStateListOf<String>()
    }

    val totalQuestions = gameQuestions.size
    val currentQuestion = gameQuestions[currentIndex]
    val currentClue = buildWordClue(
        word = currentQuestion.word,
        clueMode = difficulty.clueMode
    )

    val correctAnswer = buildMissingAnswer(
        word = currentQuestion.word,
        clueMode = difficulty.clueMode
    ).uppercase()

    LaunchedEffect(
        currentIndex,
        isPaused,
        isAnswered,
        isGameFinished,
        difficulty
    ) {
        val seconds = difficulty.secondsPerQuestion

        if (
            seconds != null &&
            !isPaused &&
            !isAnswered &&
            !isGameFinished
        ) {
            while (timeLeft > 0 && !isPaused && !isAnswered && !isGameFinished) {
                delay(1000)
                timeLeft -= 1
            }

            if (timeLeft == 0 && !isAnswered && !isGameFinished) {
                isAnswered = true
                skipCount += 1
                answerFeedback = "時間到！正確答案是：${currentQuestion.word}"

                questionResults.add(
                    "第 ${currentIndex + 1} 題：時間到，答案：${currentQuestion.word}"
                )
            }
        }
    }

    if (isGameFinished) {
        val finalResult = GameResult(
            score = score,
            correctCount = correctCount,
            wrongCount = wrongCount,
            skipCount = skipCount,
            totalQuestions = totalQuestions
        )

        LaunchedEffect(isGameFinished) {
            if (!hasSavedResult) {
                onGameFinished(finalResult)
                hasSavedResult = true
            }
        }

        HintWordResultScreen(
            difficulty = difficulty,
            score = score,
            totalQuestions = totalQuestions,
            correctCount = correctCount,
            wrongCount = wrongCount,
            skipCount = skipCount,
            questionResults = questionResults,
            onPlayAgain = {
                gameQuestions = hintWordQuestions.shuffled().take(10)
                currentIndex = 0
                selectedAnswer = ""
                score = 0
                correctCount = 0
                wrongCount = 0
                skipCount = 0
                isAnswered = false
                isGameFinished = false
                isPaused = false
                hasSavedResult = false
                answerFeedback = ""
                timeLeft = difficulty.secondsPerQuestion ?: 0
                questionResults.clear()
            },
            onBackToDifficultySelect = onBackToDifficultySelect,
            onCopyResult = {
                val resultText = buildHintResultText(
                    difficulty = difficulty,
                    score = score,
                    totalQuestions = totalQuestions,
                    correctCount = correctCount,
                    wrongCount = wrongCount,
                    skipCount = skipCount
                )

                clipboardManager.setText(AnnotatedString(resultText))
            },
            onShareResult = {
                val resultText = buildHintResultText(
                    difficulty = difficulty,
                    score = score,
                    totalQuestions = totalQuestions,
                    correctCount = correctCount,
                    wrongCount = wrongCount,
                    skipCount = skipCount
                )

                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, resultText)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, "分享成果")
                context.startActivity(shareIntent)
            }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "模式二：提示拼字",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = difficulty.title,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "第 ${currentIndex + 1} / $totalQuestions 題",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "分數：$score",
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (difficulty.secondsPerQuestion != null) {
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "剩餘時間：$timeLeft 秒",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (difficulty.secondsPerQuestion != null) {
                Button(
                    onClick = {
                        isPaused = !isPaused
                    }
                ) {
                    Text(
                        text = if (isPaused) {
                            "繼續遊戲"
                        } else {
                            "暫停"
                        }
                    )
                }
            }

            OutlinedButton(
                onClick = onBackToDifficultySelect
            ) {
                Text(text = "離開遊戲")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (isPaused) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "遊戲已暫停",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "題目、提示與 A-Z 按鈕已隱藏",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            return@Column
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "提示",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "中文：${currentQuestion.meaning}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (difficulty.clueMode == ClueMode.HELL) {
                        "拼字：不提示英文"
                    } else {
                        "拼字：$currentClue"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        HintAnswerSlots(
            clue = currentClue,
            selectedAnswer = selectedAnswer
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (!isAnswered) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        if (selectedAnswer.isNotEmpty()) {
                            selectedAnswer = selectedAnswer.dropLast(1)
                        }
                    },
                    enabled = selectedAnswer.isNotEmpty()
                ) {
                    Text(text = "刪除")
                }

                OutlinedButton(
                    onClick = {
                        selectedAnswer = ""
                    },
                    enabled = selectedAnswer.isNotEmpty()
                ) {
                    Text(text = "清空")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val userAnswer = selectedAnswer.uppercase()

                    if (userAnswer == correctAnswer) {
                        score += 10
                        correctCount += 1
                        answerFeedback = "答對了！+10 分"
                        questionResults.add(
                            "第 ${currentIndex + 1} 題：答對，答案：${currentQuestion.word}"
                        )
                    } else {
                        wrongCount += 1
                        answerFeedback = "答錯了！正確答案是：${currentQuestion.word}"
                        questionResults.add(
                            "第 ${currentIndex + 1} 題：答錯，答案：${currentQuestion.word}"
                        )
                    }

                    isAnswered = true
                },
                enabled = selectedAnswer.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "送出答案")
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedButton(
                onClick = {
                    skipCount += 1
                    isAnswered = true
                    answerFeedback = "已跳過，正確答案是：${currentQuestion.word}"

                    questionResults.add(
                        "第 ${currentIndex + 1} 題：跳過，答案：${currentQuestion.word}"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "跳過此題")
            }

            Spacer(modifier = Modifier.height(10.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ('A'..'Z').forEach { letter ->
                    Button(
                        onClick = {
                            if (selectedAnswer.length < correctAnswer.length) {
                                selectedAnswer += letter
                            }
                        },
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(text = letter.toString())
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = answerFeedback,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "本題已結束",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (currentIndex == totalQuestions - 1) {
                        isGameFinished = true
                    } else {
                        currentIndex += 1
                        selectedAnswer = ""
                        isAnswered = false
                        answerFeedback = ""
                        timeLeft = difficulty.secondsPerQuestion ?: 0
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (currentIndex == totalQuestions - 1) {
                        "查看結果"
                    } else {
                        "下一題"
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun HintAnswerSlots(
    clue: String,
    selectedAnswer: String
) {
    var answerIndex = 0

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        clue.forEach { char ->
            val isBlank = char == '口' || char == '_'

            val displayText = if (isBlank) {
                val inputChar = selectedAnswer.getOrNull(answerIndex)
                answerIndex += 1
                inputChar?.toString() ?: ""
            } else {
                char.toString()
            }

            Box(
                modifier = Modifier
                    .padding(3.dp)
                    .size(42.dp)
                    .border(
                        width = 2.dp,
                        color = if (isBlank) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Gray
                        },
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}