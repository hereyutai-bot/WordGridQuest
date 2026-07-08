package com.yutai.wordgridquest

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun HintWordScreen(
    onBackToModeSelect: () -> Unit,
    onGameFinished: (GameResult) -> Unit
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
            },
            onGameFinished = onGameFinished
        )
    }
}