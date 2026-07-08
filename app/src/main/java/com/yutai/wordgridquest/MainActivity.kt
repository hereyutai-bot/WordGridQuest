package com.yutai.wordgridquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.yutai.wordgridquest.ui.theme.WordGridQuestTheme
import androidx.compose.runtime.LaunchedEffect
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

@Composable
fun WordGridQuestApp() {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }

    var rankingRecords by remember {
        mutableStateOf<List<RankingRecord>>(emptyList())
    }

    var studyRecords by remember {
        mutableStateOf<List<StudyRecord>>(emptyList())
    }

    fun addLetterTileRecord(result: LetterTileGameResult) {
        val rankingRecord = RankingRecord(
            playerName = "玩家",
            modeType = GameModeType.LETTER_TILE,
            score = result.score,
            detailText = "成功單字 ${result.successWordCount} 個，使用提示 ${result.hintUsedCount} / 3 次",
            playedAtText = "本次遊玩"
        )

        val studyRecord = StudyRecord(
            modeType = GameModeType.LETTER_TILE,
            score = result.score,
            correctCount = result.successWordCount,
            wrongCount = 0,
            skipCount = 0,
            playedAtText = "本次遊玩",
            note = "已消除 ${result.removedTileCount} 張，剩餘 ${result.remainingTileCount} 張"
        )

        rankingRecords = rankingRecords + rankingRecord
        studyRecords = studyRecords + studyRecord
    }

    fun addHintWordRecord(result: GameResult) {
        val rankingRecord = RankingRecord(
            playerName = "玩家",
            modeType = GameModeType.HINT_WORD,
            score = result.score,
            detailText = "答對 ${result.correctCount} 題，答錯 ${result.wrongCount} 題，跳過 ${result.skipCount} 題",
            playedAtText = "本次遊玩"
        )

        val studyRecord = StudyRecord(
            modeType = GameModeType.HINT_WORD,
            score = result.score,
            correctCount = result.correctCount,
            wrongCount = result.wrongCount,
            skipCount = result.skipCount,
            playedAtText = "本次遊玩",
            note = "總題數 ${result.totalQuestions} 題"
        )

        rankingRecords = rankingRecords + rankingRecord
        studyRecords = studyRecords + studyRecord
    }

    when (currentScreen) {
        Screen.HOME -> HomeScreen(
            onStartClick = { currentScreen = Screen.MODE_SELECT },
            onRankingClick = { currentScreen = Screen.RANKING },
            onStudyRecordClick = { currentScreen = Screen.STUDY_RECORD },
            onHelpClick = { currentScreen = Screen.HELP }
        )

        Screen.MODE_SELECT -> ModeSelectScreen(
            onLetterTileClick = { currentScreen = Screen.LETTER_TILE },
            onHintWordClick = { currentScreen = Screen.HINT_WORD },
            onBackClick = { currentScreen = Screen.HOME }
        )

        Screen.LETTER_TILE -> LetterTileScreen(
            onBackClick = { currentScreen = Screen.MODE_SELECT },
            onGameFinished = { result ->
                addLetterTileRecord(result)
            }
        )

        Screen.HINT_WORD -> HintWordScreen(
            onBackToModeSelect = { currentScreen = Screen.MODE_SELECT },
            onGameFinished = { result ->
                addHintWordRecord(result)
            }
        )

        Screen.RANKING -> RankingScreen(
            records = rankingRecords,
            onBackClick = { currentScreen = Screen.HOME }
        )

        Screen.STUDY_RECORD -> StudyRecordScreen(
            records = studyRecords,
            onBackClick = { currentScreen = Screen.HOME }
        )

        Screen.HELP -> GameHelpScreen(
            onBackClick = { currentScreen = Screen.HOME }
        )
    }
}