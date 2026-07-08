package com.yutai.wordgridquest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class HintGameDifficulty(
    val title: String,
    val description: String,
    val secondsPerQuestion: Int?,
    val clueMode: ClueMode
) {
    PRACTICE(
        title = "練習模式",
        description = "少挖空，適合初學",
        secondsPerQuestion = null,
        clueMode = ClueMode.EASY
    ),

    CHALLENGE(
        title = "挑戰模式",
        description = "中等挖空，難度適中",
        secondsPerQuestion = null,
        clueMode = ClueMode.NORMAL
    ),

    FAST(
        title = "快速模式",
        description = "多挖空，較有挑戰",
        secondsPerQuestion = null,
        clueMode = ClueMode.HARD
    ),

    HELL(
        title = "地獄模式",
        description = "完全不提示英文，只看中文意思",
        secondsPerQuestion = null,
        clueMode = ClueMode.HELL
    )
}

enum class ClueMode {
    EASY,
    NORMAL,
    HARD,
    HELL
}

@Composable
fun HintDifficultySelectScreen(
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
            title = HintGameDifficulty.PRACTICE.title,
            description = HintGameDifficulty.PRACTICE.description,
            onClick = { onSelectDifficulty(HintGameDifficulty.PRACTICE) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        HintDifficultyButton(
            title = HintGameDifficulty.CHALLENGE.title,
            description = HintGameDifficulty.CHALLENGE.description,
            onClick = { onSelectDifficulty(HintGameDifficulty.CHALLENGE) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        HintDifficultyButton(
            title = HintGameDifficulty.FAST.title,
            description = HintGameDifficulty.FAST.description,
            onClick = { onSelectDifficulty(HintGameDifficulty.FAST) }
        )
        Spacer(modifier = Modifier.height(14.dp))

        HintDifficultyButton(
            title = "地獄模式",
            description = "不提示英文，只看中文意思作答",
            onClick = {
                onSelectDifficulty(HintGameDifficulty.HELL)
            }
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