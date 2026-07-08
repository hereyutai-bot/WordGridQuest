package com.yutai.wordgridquest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HintWordResultScreen(
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