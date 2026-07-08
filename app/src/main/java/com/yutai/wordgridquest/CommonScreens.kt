package com.yutai.wordgridquest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            .navigationBarsPadding()
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

@Composable
fun RankingScreen(
    records: List<RankingRecord>,
    onBackClick: () -> Unit
) {
    val letterTileRecords = records
        .filter { it.modeType == GameModeType.LETTER_TILE }
        .sortedByDescending { it.score }

    val hintWordRecords = records
        .filter { it.modeType == GameModeType.HINT_WORD }
        .sortedByDescending { it.score }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "排行榜",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (records.isEmpty()) {
            EmptyStateCard(
                title = "尚無排行榜資料",
                content = "之後完成遊戲後，會把高分紀錄顯示在這裡。"
            )
        } else {
            RankingModeSection(
                title = GameModeType.LETTER_TILE.displayName,
                records = letterTileRecords
            )

            Spacer(modifier = Modifier.height(20.dp))

            RankingModeSection(
                title = GameModeType.HINT_WORD.displayName,
                records = hintWordRecords
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "返回首頁")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun RankingModeSection(
    title: String,
    records: List<RankingRecord>
) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(10.dp))

    if (records.isEmpty()) {
        EmptyStateCard(
            title = "尚無資料",
            content = "此模式目前還沒有排行榜紀錄。"
        )
    } else {
        records.forEachIndexed { index, record ->
            RankingRecordCard(
                rank = index + 1,
                record = record
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun StudyRecordScreen(
    records: List<StudyRecord>,
    onBackClick: () -> Unit
) {
    val letterTileRecords = records
        .filter { it.modeType == GameModeType.LETTER_TILE }

    val hintWordRecords = records
        .filter { it.modeType == GameModeType.HINT_WORD }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "學習紀錄",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (records.isEmpty()) {
            EmptyStateCard(
                title = "尚無學習紀錄",
                content = "之後完成遊戲後，會記錄分數、答題狀況與學習成果。"
            )
        } else {
            StudyRecordModeSection(
                title = GameModeType.LETTER_TILE.displayName,
                records = letterTileRecords
            )

            Spacer(modifier = Modifier.height(20.dp))

            StudyRecordModeSection(
                title = GameModeType.HINT_WORD.displayName,
                records = hintWordRecords
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "返回首頁")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun StudyRecordModeSection(
    title: String,
    records: List<StudyRecord>
) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(10.dp))

    if (records.isEmpty()) {
        EmptyStateCard(
            title = "尚無資料",
            content = "此模式目前還沒有學習紀錄。"
        )
    } else {
        records.forEach { record ->
            StudyRecordCard(record = record)

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun EmptyStateCard(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = content,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun RankingRecordCard(
    rank: Int,
    record: RankingRecord
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "第 $rank 名",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(text = "玩家：${record.playerName}", fontSize = 16.sp)
            Text(text = "模式：${record.modeType.displayName}", fontSize = 16.sp)
            Text(text = "分數：${record.score}", fontSize = 16.sp)
            Text(text = "內容：${record.detailText}", fontSize = 16.sp)
            Text(text = "時間：${record.playedAtText}", fontSize = 16.sp)
        }
    }
}

@Composable
private fun StudyRecordCard(
    record: StudyRecord
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = record.modeType.displayName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(text = "分數：${record.score}", fontSize = 16.sp)
            Text(text = "答對：${record.correctCount}", fontSize = 16.sp)
            Text(text = "答錯：${record.wrongCount}", fontSize = 16.sp)
            Text(text = "跳過：${record.skipCount}", fontSize = 16.sp)
            Text(text = "時間：${record.playedAtText}", fontSize = 16.sp)

            if (record.note.isNotBlank()) {
                Text(text = "備註：${record.note}", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun GameHelpScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "遊戲說明",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        HelpSectionCard(
            title = "模式一：字母牌陣",
            content = """
                ・點選沒有被壓住的字母牌。
                ・組成有效英文單字即可得分。
                ・灰色牌代表被上層牌壓住，暫時不能選。
                ・使用提示會指定一個單字任務。
                ・完成提示任務不計分。
                ・剩餘字母太少或無法組字時會進入結算。
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(14.dp))

        HelpSectionCard(
            title = "模式一計分",
            content = """
                ・3 字母：30 分
                ・4 字母：50 分
                ・5 字母：100 分
                ・6 字母以上：150 分
                ・提示任務：0 分
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(14.dp))

        HelpSectionCard(
            title = "模式二：提示拼字",
            content = """
                ・根據中文意思與缺字提示拼出英文單字。
                ・使用 A-Z 按鈕作答。
                ・答對 +10 分。
                ・答錯、跳過、時間到皆 0 分。
                ・每場隨機 10 題。
                ・答題後會顯示正確答案。
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(14.dp))

        HelpSectionCard(
            title = "模式二難度",
            content = """
                ・練習模式：不倒數
                ・挑戰模式：每題 15 秒
                ・快速模式：每題 10 秒
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "返回首頁")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun HelpSectionCard(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = content,
                fontSize = 16.sp
            )
        }
    }
}