package com.yutai.wordgridquest

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.yutai.wordgridquest.ui.theme.WordGridQuestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WordGridQuestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WordGridQuestApp()
                }
            }
        }
    }
}

enum class Screen {
    HOME,
    MODE_SELECT,
    LETTER_TILE,
    HINT_WORD,
    RANKING,
    STUDY_RECORD
}

data class HintWordQuestion(
    val word: String,
    val meaning: String,
    val clue: String,
    val missingAnswer: String
)

data class GameResult(
    val score: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val totalQuestions: Int
)

val hintWordQuestions = listOf(
    HintWordQuestion(
        word = "APPLE",
        meaning = "蘋果",
        clue = "A _ P L E",
        missingAnswer = "P"
    ),
    HintWordQuestion(
        word = "BOOK",
        meaning = "書",
        clue = "B _ O K",
        missingAnswer = "O"
    ),
    HintWordQuestion(
        word = "CAT",
        meaning = "貓",
        clue = "C _ T",
        missingAnswer = "A"
    ),
    HintWordQuestion(
        word = "DOG",
        meaning = "狗",
        clue = "D _ G",
        missingAnswer = "O"
    ),
    HintWordQuestion(
        word = "GAME",
        meaning = "遊戲",
        clue = "G A _ E",
        missingAnswer = "M"
    )
)

@Composable
fun WordGridQuestApp() {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }

    when (currentScreen) {
        Screen.HOME -> HomeScreen(
            onStartClick = { currentScreen = Screen.MODE_SELECT },
            onRankingClick = { currentScreen = Screen.RANKING },
            onStudyRecordClick = { currentScreen = Screen.STUDY_RECORD }
        )

        Screen.MODE_SELECT -> ModeSelectScreen(
            onLetterTileClick = { currentScreen = Screen.LETTER_TILE },
            onHintWordClick = { currentScreen = Screen.HINT_WORD },
            onBackClick = { currentScreen = Screen.HOME }
        )

        Screen.LETTER_TILE -> SimplePageScreen(
            title = "字母牌陣",
            content = "從字母牌中找出可以組成英文單字的組合。\n\n成功拼出有效單字後，字母牌會消除並獲得分數。\n\n此模式下一階段實作。",
            onBackClick = { currentScreen = Screen.MODE_SELECT }
        )

        Screen.HINT_WORD -> HintWordScreen(
            onBackClick = { currentScreen = Screen.MODE_SELECT }
        )

        Screen.RANKING -> SimplePageScreen(
            title = "排行榜",
            content = "排行榜將記錄最高分、完成單字數、正確率與遊玩時間。\n\n目前尚未儲存資料。",
            onBackClick = { currentScreen = Screen.HOME }
        )

        Screen.STUDY_RECORD -> SimplePageScreen(
            title = "學習紀錄",
            content = "學習紀錄將保存遊玩次數、錯誤單字、完成關卡與學習進度。\n\n目前尚未儲存資料。",
            onBackClick = { currentScreen = Screen.HOME }
        )
    }
}

@Composable
fun HomeScreen(
    onStartClick: () -> Unit,
    onRankingClick: () -> Unit,
    onStudyRecordClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Word Grid Quest",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "英文單字牌陣",
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = onStartClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "開始遊戲")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onRankingClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "排行榜")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onStudyRecordClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "學習紀錄")
        }
    }
}

@Composable
fun ModeSelectScreen(
    onLetterTileClick: () -> Unit,
    onHintWordClick: () -> Unit,
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
            text = "選擇遊戲模式",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(28.dp))

        GameModeCard(
            title = "模式一：字母牌陣",
            description = "從字母牌中找出可組成英文單字的組合，成功拼字後即可消除。",
            onClick = onLetterTileClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        GameModeCard(
            title = "模式二：提示拼字",
            description = "根據中文提示與部分字母線索，補齊正確英文單字。",
            onClick = onHintWordClick
        )

        Spacer(modifier = Modifier.height(28.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "返回首頁")
        }
    }
}

@Composable
fun HintWordScreen(
    onBackClick: () -> Unit
) {
    var questionIndex by remember { mutableIntStateOf(0) }
    var userInput by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf("") }
    var score by remember { mutableIntStateOf(0) }
    var correctCount by remember { mutableIntStateOf(0) }
    var wrongCount by remember { mutableIntStateOf(0) }
    var hasAnswered by remember { mutableStateOf(false) }
    var showResult by remember { mutableStateOf(false) }

    val question = hintWordQuestions[questionIndex]
    val totalQuestions = hintWordQuestions.size

    if (showResult) {
        GameResultScreen(
            result = GameResult(
                score = score,
                correctCount = correctCount,
                wrongCount = wrongCount,
                totalQuestions = totalQuestions
            ),
            onPlayAgainClick = {
                questionIndex = 0
                userInput = ""
                resultMessage = ""
                score = 0
                correctCount = 0
                wrongCount = 0
                hasAnswered = false
                showResult = false
            },
            onBackClick = onBackClick
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

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "第 ${questionIndex + 1} / $totalQuestions 題",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                    text = "請輸入缺少的字母",
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = userInput,
                    onValueChange = {
                        userInput = it.uppercase().take(1)
                    },
                    label = {
                        Text(text = "答案")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (!hasAnswered) {
                            if (userInput == question.missingAnswer) {
                                resultMessage = "答對了！${question.word} = ${question.meaning}"
                                score += 10
                                correctCount += 1
                            } else {
                                resultMessage = "答錯了，正確答案是 ${question.missingAnswer}，完整單字是 ${question.word}"
                                wrongCount += 1
                            }
                            hasAnswered = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "檢查答案")
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        userInput = ""
                        resultMessage = ""
                        hasAnswered = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "重新作答")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (questionIndex < totalQuestions - 1) {
                            questionIndex += 1
                            userInput = ""
                            resultMessage = ""
                            hasAnswered = false
                        } else {
                            showResult = true
                        }
                    },
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

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "分數：$score",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "答對：$correctCount　答錯：$wrongCount",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (resultMessage.isNotEmpty()) {
            Text(
                text = resultMessage,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "返回模式選擇")
        }
    }
}

@Composable
fun GameResultScreen(
    result: GameResult,
    onPlayAgainClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val accuracy = if (result.totalQuestions > 0) {
        result.correctCount * 100 / result.totalQuestions
    } else {
        0
    }

    val shareText = buildShareText(
        score = result.score,
        correctCount = result.correctCount,
        wrongCount = result.wrongCount,
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
                ResultTextRow(label = "模式", value = "提示拼字")
                ResultTextRow(label = "分數", value = result.score.toString())
                ResultTextRow(label = "總題數", value = "${result.totalQuestions} 題")
                ResultTextRow(label = "答對", value = "${result.correctCount} 題")
                ResultTextRow(label = "答錯", value = "${result.wrongCount} 題")
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
            Text(text = "再玩一次")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "返回模式選擇")
        }
    }
}

@Composable
fun ResultTextRow(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label：$value",
            fontSize = 17.sp,
            fontWeight = if (label == "分數") FontWeight.Bold else FontWeight.Normal
        )
    }
}

fun buildShareText(
    score: Int,
    correctCount: Int,
    wrongCount: Int,
    totalQuestions: Int,
    accuracy: Int
): String {
    return """
        我剛完成 Word Grid Quest 英文單字挑戰！

        模式：提示拼字
        分數：$score
        總題數：$totalQuestions 題
        答對：$correctCount 題
        答錯：$wrongCount 題
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

@Composable
fun GameModeCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun SimplePageScreen(
    title: String,
    content: String,
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
            text = title,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = content,
            fontSize = 17.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "返回")
        }
    }
}