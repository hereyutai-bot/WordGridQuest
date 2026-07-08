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
            onBackClick = { currentScreen = Screen.MODE_SELECT }
        )

        Screen.HINT_WORD -> HintWordScreen(
            onBackToModeSelect = { currentScreen = Screen.MODE_SELECT }
        )

        Screen.RANKING -> RankingScreen(
            records = emptyList(),
            onBackClick = { currentScreen = Screen.HOME }
        )

        Screen.STUDY_RECORD -> StudyRecordScreen(
            records = emptyList(),
            onBackClick = { currentScreen = Screen.HOME }
        )

        Screen.HELP -> GameHelpScreen(
            onBackClick = { currentScreen = Screen.HOME }
        )
    }
}