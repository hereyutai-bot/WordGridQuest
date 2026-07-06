package com.yutai.wordgridquest

import android.os.Bundle
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

        Screen.HINT_WORD -> SimplePageScreen(
            title = "提示拼字",
            content = "根據中文提示與部分字母線索，補齊正確英文單字。\n\n範例：\n提示：蘋果\nA _ P L E\n答案：APPLE",
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