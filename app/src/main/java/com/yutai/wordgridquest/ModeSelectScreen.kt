package com.yutai.wordgridquest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            description = "從彩色堆疊字母牌中找出可組成英文單字的組合，成功拼字後即可消除。",
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