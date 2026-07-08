package com.yutai.wordgridquest

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class HintWordGameMode(
    val name: String,
    val description: String,
    val timeLimitSeconds: Int?
)

val hintWordGameModes = listOf(
    HintWordGameMode(
        name = "練習模式",
        description = "不倒數，適合慢慢學習單字。",
        timeLimitSeconds = null
    ),
    HintWordGameMode(
        name = "挑戰模式",
        description = "每題 15 秒，適合一般挑戰。",
        timeLimitSeconds = 15
    ),
    HintWordGameMode(
        name = "快速模式",
        description = "每題 10 秒，節奏較快。",
        timeLimitSeconds = 10
    )
)

@Composable
fun HintWordScreen(
    onBackClick: () -> Unit
) {
    var selectedMode by remember { mutableStateOf<HintWordGameMode?>(null) }

    if (selectedMode == null) {
        HintWordModeSelectScreen(
            onModeSelected = {
                selectedMode = it
            },
            onBackClick = onBackClick
        )
        return
    }

    HintWordGameScreen(
        gameMode = selectedMode!!,
        onBackClick = {
            selectedMode = null
        },
        onExitToModeSelect = onBackClick
    )
}

@Composable
fun HintWordModeSelectScreen(
    onModeSelected: (HintWordGameMode) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "提示拼字",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "選擇遊戲模式",
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        hintWordGameModes.forEach { mode ->
            Card(
                onClick = {
                    onModeSelected(mode)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = mode.name,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = mode.description,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "返回模式選擇")
        }
    }
}

@Composable
fun HintWordGameScreen(
    gameMode: HintWordGameMode,
    onBackClick: () -> Unit,
    onExitToModeSelect: () -> Unit
) {
    var questionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf("") }
    var score by remember { mutableIntStateOf(0) }
    var correctCount by remember { mutableIntStateOf(0) }
    var wrongCount by remember { mutableIntStateOf(0) }
    var skipCount by remember { mutableIntStateOf(0) }
    var hasAnswered by remember { mutableStateOf(false) }
    var showResult by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var remainingSeconds by remember {
        mutableIntStateOf(gameMode.timeLimitSeconds ?: 0)
    }

    val question = hintWordQuestions[questionIndex]
    val totalQuestions = hintWordQuestions.size
    val hasTimer = gameMode.timeLimitSeconds != null

    fun markCurrentQuestionAsSkippedByTimeout() {
        selectedAnswer = "-"
        resultMessage =
            "時間到！正確答案是 ${question.missingAnswer}，完整單字是 ${question.word}"
        skipCount += 1
        hasAnswered = true
        isPaused = false
    }

    LaunchedEffect(questionIndex, gameMode) {
        remainingSeconds = gameMode.timeLimitSeconds ?: 0
    }

    LaunchedEffect(questionIndex, hasAnswered, showResult, gameMode, isPaused) {
        if (hasTimer && !showResult && !hasAnswered && !isPaused) {
            while (
                remainingSeconds > 0 &&
                !hasAnswered &&
                !showResult &&
                !isPaused
            ) {
                delay(1000)
                remainingSeconds -= 1
            }

            if (
                remainingSeconds <= 0 &&
                !hasAnswered &&
                !showResult &&
                !isPaused
            ) {
                markCurrentQuestionAsSkippedByTimeout()
            }
        }
    }

    if (showResult) {
        GameResultScreen(
            result = GameResult(
                score = score,
                correctCount = correctCount,
                wrongCount = wrongCount,
                skipCount = skipCount,
                totalQuestions = totalQuestions
            ),
            modeName = gameMode.name,
            onPlayAgainClick = {
                questionIndex = 0
                selectedAnswer = ""
                resultMessage = ""
                score = 0
                correctCount = 0
                wrongCount = 0
                skipCount = 0
                hasAnswered = false
                showResult = false
                isPaused = false
                remainingSeconds = gameMode.timeLimitSeconds ?: 0
            },
            onBackClick = onBackClick,
            onExitToModeSelect = onExitToModeSelect
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "提示拼字",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = gameMode.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "第 ${questionIndex + 1} / $totalQuestions 題",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = when {
                isPaused -> "遊戲已暫停"
                hasTimer -> "剩餘時間：$remainingSeconds 秒"
                else -> "練習模式：不倒數"
            },
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (!isPaused && !hasAnswered) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        isPaused = true
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "暫停")
                }

                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "離開遊戲")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isPaused) {
                    Text(
                        text = "遊戲已暫停",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "題目已隱藏，請按「繼續遊戲」後作答。",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            isPaused = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "繼續遊戲")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onBackClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "離開遊戲")
                    }
                } else {
                    Text(
                        text = "提示：${question.meaning}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = question.clue,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 4.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "請點選缺少的字母",
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (selectedAnswer.isEmpty()) {
                            "尚未選擇"
                        } else {
                            "目前選擇：$selectedAnswer"
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AlphabetButtonGrid(
                        selectedAnswer = selectedAnswer,
                        hasAnswered = hasAnswered,
                        onLetterClick = { letter ->
                            if (!hasAnswered) {
                                selectedAnswer = letter

                                if (letter == question.missingAnswer) {
                                    resultMessage =
                                        "答對了！${question.word} = ${question.meaning}"
                                    score += 10
                                    correctCount += 1
                                } else {
                                    resultMessage =
                                        "答錯了，正確答案是 ${question.missingAnswer}，完整單字是 ${question.word}"
                                    wrongCount += 1
                                }

                                hasAnswered = true
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = {
                            selectedAnswer = ""
                            resultMessage = ""
                        },
                        enabled = !hasAnswered,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "重新作答")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            if (!hasAnswered) {
                                selectedAnswer = "-"
                                resultMessage =
                                    "已跳過此題，正確答案是 ${question.missingAnswer}，完整單字是 ${question.word}"
                                skipCount += 1
                                hasAnswered = true
                            }
                        },
                        enabled = !hasAnswered,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "跳過此題")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (questionIndex < totalQuestions - 1) {
                                questionIndex += 1
                                selectedAnswer = ""
                                resultMessage = ""
                                hasAnswered = false
                                isPaused = false
                                remainingSeconds = gameMode.timeLimitSeconds ?: 0
                            } else {
                                showResult = true
                            }
                        },
                        enabled = hasAnswered,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (questionIndex < totalQuestions - 1) {
                                "下一題"
                            } else {
                                "結束遊戲"
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "分數：$score",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "答對：$correctCount　答錯：$wrongCount　跳過：$skipCount",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (resultMessage.isNotEmpty() && !isPaused) {
            Text(
                text = resultMessage,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AlphabetButtonGrid(
    selectedAnswer: String,
    hasAnswered: Boolean,
    onLetterClick: (String) -> Unit
) {
    val alphabetLetters = ('A'..'Z').map { it.toString() }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        alphabetLetters.forEach { letter ->
            Button(
                onClick = {
                    onLetterClick(letter)
                },
                enabled = !hasAnswered,
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(42.dp)
            ) {
                Text(
                    text = letter,
                    fontSize = 16.sp,
                    fontWeight = if (selectedAnswer == letter) {
                        FontWeight.Bold
                    } else {
                        FontWeight.SemiBold
                    },
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun GameResultScreen(
    result: GameResult,
    modeName: String,
    onPlayAgainClick: () -> Unit,
    onBackClick: () -> Unit,
    onExitToModeSelect: () -> Unit
) {
    val context = LocalContext.current
    val accuracy = if (result.totalQuestions > 0) {
        result.correctCount * 100 / result.totalQuestions
    } else {
        0
    }

    val shareText = buildShareText(
        modeName = modeName,
        score = result.score,
        correctCount = result.correctCount,
        wrongCount = result.wrongCount,
        skipCount = result.skipCount,
        totalQuestions = result.totalQuestions,
        accuracy = accuracy
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "遊戲結果",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                ResultTextRow(label = "模式", value = "提示拼字 - $modeName")
                ResultTextRow(label = "分數", value = result.score.toString())
                ResultTextRow(label = "總題數", value = "${result.totalQuestions} 題")
                ResultTextRow(label = "答對", value = "${result.correctCount} 題")
                ResultTextRow(label = "答錯", value = "${result.wrongCount} 題")
                ResultTextRow(label = "跳過", value = "${result.skipCount} 題")
                ResultTextRow(label = "正確率", value = "$accuracy%")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                copyTextToClipboard(context, shareText)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "複製成果")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                shareResult(context, shareText)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "分享成果")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onPlayAgainClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "同模式再玩一次")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "返回難度選擇")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onExitToModeSelect,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "返回模式選擇")
        }
    }
}

fun buildShareText(
    modeName: String,
    score: Int,
    correctCount: Int,
    wrongCount: Int,
    skipCount: Int,
    totalQuestions: Int,
    accuracy: Int
): String {
    return """
        我剛完成 Word Grid Quest 英文單字挑戰！

        模式：提示拼字 - $modeName
        分數：$score
        總題數：$totalQuestions 題
        答對：$correctCount 題
        答錯：$wrongCount 題
        跳過：$skipCount 題
        正確率：$accuracy%

        一起來挑戰英文單字吧！
        #WordGridQuest #英文學習 #單字遊戲
    """.trimIndent()
}

fun copyTextToClipboard(
    context: Context,
    text: String
) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("WordGridQuestResult", text)
    clipboard.setPrimaryClip(clip)

    Toast.makeText(
        context,
        "成果已複製",
        Toast.LENGTH_SHORT
    ).show()
}

fun shareResult(
    context: Context,
    text: String
) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(
        sendIntent,
        "分享成果"
    )

    context.startActivity(shareIntent)
}