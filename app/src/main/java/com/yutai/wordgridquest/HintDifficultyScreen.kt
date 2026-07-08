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