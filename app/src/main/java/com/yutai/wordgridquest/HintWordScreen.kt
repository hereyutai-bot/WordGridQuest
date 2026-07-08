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

private enum class HintGameDifficulty(
    val title: String,
    val description: String,
    val secondsPerQuestion: Int?
) {
    Practice(
        title = "練習模式",
        description = "不倒數，適合熟悉單字",
        secondsPerQuestion = null
    ),
    Challenge(
        title = "挑戰模式",
        description = "每題 15 秒",
        secondsPerQuestion = 15
    ),
    Fast(
        title = "快速模式",
        description = "每題 10 秒",
        secondsPerQuestion = 10
    )
}

@Composable
fun HintWordScreen(
    onBackToModeSelect: () -> Unit
) {
    var selectedDifficulty by remember { mutableStateOf<HintGameDifficulty?>(null) }

    if (selectedDifficulty == null) {
        HintDifficultySelectScreen(
            onSelectDifficulty = { difficulty ->
                selectedDifficulty = difficulty
            },
            onBackToModeSelect = onBackToModeSelect
        )
    } else {
        HintWordGameScreen(
            difficulty = selectedDifficulty!!,
            onBackToDifficultySelect = {
                selectedDifficulty = null
            }
        )
    }
}

@Composable
private fun HintDifficultySelectScreen(
    onSelectDifficulty: (HintGameDifficulty) -> Unit,
    onBackToModeSelect: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "模式二：提示拼字",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "請選擇遊戲難度",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        HintDifficultyButton(
            title = HintGameDifficulty.Practice.title,
            description = HintGameDifficulty.Practice.description,
            onClick = { onSelectDifficulty(HintGameDifficulty.Practice) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        HintDifficultyButton(
            title = HintGameDifficulty.Challenge.title,
            description = HintGameDifficulty.Challenge.description,
            onClick = { onSelectDifficulty(HintGameDifficulty.Challenge) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        HintDifficultyButton(
            title = HintGameDifficulty.Fast.title,
            description = HintGameDifficulty.Fast.description,
            onClick = { onSelectDifficulty(HintGameDifficulty.Fast) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = onBackToModeSelect,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "返回模式選擇")
        }
    }
}

@Composable
private fun HintDifficultyButton(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun HintWordGameScreen(
    difficulty: HintGameDifficulty,
    onBackToDifficultySelect: () -> Unit
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

    var answerFeedback by remember { mutableStateOf("") }

    var timeLeft by remember {
        mutableIntStateOf(difficulty.secondsPerQuestion ?: 0)
    }

    val questionResults = remember {
        mutableStateListOf<String>()
    }

    val totalQuestions = gameQuestions.size
    val currentQuestion = gameQuestions[currentIndex]
    val correctAnswer = currentQuestion.missingAnswer.uppercase()

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
                answerFeedback = "時間到！正確答案是：$correctAnswer"

                questionResults.add(
                    "第 ${currentIndex + 1} 題：時間到，答案：$correctAnswer"
                )
            }
        }
    }

    if (isGameFinished) {
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
            .padding(18.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "模式二：提示拼字",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = difficulty.title,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

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
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "剩餘時間：$timeLeft 秒",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

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

        Spacer(modifier = Modifier.height(20.dp))

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
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "提示",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentQuestion.clue,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = selectedAnswer.ifEmpty { "請用 A-Z 按鈕拼出答案" },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

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

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val userAnswer = selectedAnswer.uppercase()

                    if (userAnswer == correctAnswer) {
                        score += 10
                        correctCount += 1
                        answerFeedback = "答對了！+10 分"
                        questionResults.add(
                            "第 ${currentIndex + 1} 題：答對，答案：$correctAnswer"
                        )
                    } else {
                        wrongCount += 1
                        answerFeedback = "答錯了！正確答案是：$correctAnswer"
                        questionResults.add(
                            "第 ${currentIndex + 1} 題：答錯，答案：$correctAnswer"
                        )
                    }

                    isAnswered = true
                },
                enabled = selectedAnswer.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "送出答案")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    skipCount += 1
                    isAnswered = true
                    answerFeedback = "已跳過，正確答案是：$correctAnswer"

                    questionResults.add(
                        "第 ${currentIndex + 1} 題：跳過，答案：$correctAnswer"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "跳過此題")
            }

            Spacer(modifier = Modifier.height(16.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ('A'..'Z').forEach { letter ->
                    Button(
                        onClick = {
                            selectedAnswer += letter
                        },
                        modifier = Modifier.padding(3.dp)
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

            Spacer(modifier = Modifier.height(20.dp))

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
    }
}

@Composable
private fun HintWordResultScreen(
    difficulty: HintGameDifficulty,
    score: Int,
    totalQuestions: Int,
    correctCount: Int,
    wrongCount: Int,
    skipCount: Int,
    questionResults: List<String>,
    onPlayAgain: () -> Unit,
    onBackToDifficultySelect: () -> Unit,
    onCopyResult: () -> Unit,
    onShareResult: () -> Unit
) {
    val accuracy = if (totalQuestions == 0) {
        0
    } else {
        correctCount * 100 / totalQuestions
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "遊戲結果",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "模式：${difficulty.title}")
                Text(text = "分數：$score")
                Text(text = "總題數：$totalQuestions")
                Text(text = "答對：$correctCount")
                Text(text = "答錯：$wrongCount")
                Text(text = "跳過：$skipCount")
                Text(text = "正確率：$accuracy%")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (questionResults.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "本場題目回顧",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    questionResults.forEach { result ->
                        Text(
                            text = result,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        Button(
            onClick = onCopyResult,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "複製成果")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onShareResult,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "分享成果")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onPlayAgain,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "同模式再玩一次")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBackToDifficultySelect,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "返回難度選擇")
        }
    }
}

private fun buildHintResultText(
    difficulty: HintGameDifficulty,
    score: Int,
    totalQuestions: Int,
    correctCount: Int,
    wrongCount: Int,
    skipCount: Int
): String {
    val accuracy = if (totalQuestions == 0) {
        0
    } else {
        correctCount * 100 / totalQuestions
    }

    return """
        Word Grid Quest 模式二：提示拼字
        模式：${difficulty.title}
        分數：$score
        總題數：$totalQuestions
        答對：$correctCount
        答錯：$wrongCount
        跳過：$skipCount
        正確率：$accuracy%
    """.trimIndent()
}