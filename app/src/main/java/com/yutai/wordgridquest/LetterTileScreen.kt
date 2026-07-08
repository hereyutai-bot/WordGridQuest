package com.yutai.wordgridquest

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.runtime.LaunchedEffect

@Composable
fun LetterTileScreen(
    onBackClick: () -> Unit,
    onGameFinished: (LetterTileGameResult) -> Unit
) {
    var tiles by remember { mutableStateOf(createNewLetterTiles()) }
    var selectedWord by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf("") }
    var score by remember { mutableIntStateOf(0) }
    var hintCount by remember { mutableIntStateOf(3) }

    var activeHintWord by remember { mutableStateOf<String?>(null) }
    var activeHintMeaning by remember { mutableStateOf<String?>(null) }
    var activeHintTileIds by remember { mutableStateOf<List<Int>>(emptyList()) }
    var showHintDialog by remember { mutableStateOf(false) }

    var successWordCount by remember { mutableIntStateOf(0) }
    var showLetterResult by remember { mutableStateOf(false) }
    var hasSavedResult by remember { mutableStateOf(false) }

    val remainingTileCount = tiles.count { !it.isRemoved }
    val removedTileCount = tiles.count { it.isRemoved }
    val hintUsedCount = 3 - hintCount

    if (showLetterResult) {
        val finalResult = LetterTileGameResult(
            score = score,
            successWordCount = successWordCount,
            removedTileCount = removedTileCount,
            remainingTileCount = remainingTileCount,
            hintUsedCount = hintUsedCount
        )

        LaunchedEffect(showLetterResult) {
            if (!hasSavedResult) {
                onGameFinished(finalResult)
                hasSavedResult = true
            }
        }

        LetterTileResultScreen(
            result = finalResult,
            onPlayAgainClick = {
                tiles = createNewLetterTiles()
                selectedWord = ""
                resultMessage = ""
                score = 0
                hintCount = 3
                activeHintWord = null
                activeHintMeaning = null
                activeHintTileIds = emptyList()
                showHintDialog = false
                successWordCount = 0
                showLetterResult = false
                hasSavedResult = false
            },
            onBackClick = onBackClick
        )
        return
    }

    if (showHintDialog && activeHintWord != null && activeHintMeaning != null) {
        AlertDialog(
            onDismissRequest = {
                // 強制提示任務，不允許點外面關閉
            },
            title = {
                Text(text = "提示任務")
            },
            text = {
                Text(
                    text = "提示單字：$activeHintWord = $activeHintMeaning\n\n" +
                            "請依序點選：${activeHintWord!!.toList().joinToString(" → ")}\n\n" +
                            "完成此提示單字不計分。"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showHintDialog = false
                    }
                ) {
                    Text(text = "知道了")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .clickable(
                enabled = resultMessage.isNotEmpty()
            ) {
                resultMessage = ""
            }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "字母牌陣",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "返回模式選擇")
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "灰色牌被上層壓住，不能點選",
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "3字30｜4字50｜5字100｜6字以上150",
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = "提示任務完成不計分",
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "分數：$score　提示：$hintCount / 3　剩餘：$remainingTileCount",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LetterTileBoard(
            tiles = tiles,
            hintedTileIds = activeHintTileIds.toSet(),
            onTileClick = { tile ->
                val hintWord = activeHintWord

                if (hintWord != null) {
                    val expectedTileId = activeHintTileIds.getOrNull(selectedWord.length)

                    if (tile.id != expectedTileId) {
                        resultMessage = "請依提示順序點選：$hintWord"
                        return@LetterTileBoard
                    }
                }

                selectedWord += tile.letter

                tiles = tiles.map {
                    if (it.id == tile.id) {
                        it.copy(isSelected = true)
                    } else {
                        it
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "目前選取",
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (selectedWord.isEmpty()) {
                        "尚未選取字母"
                    } else {
                        selectedWord
                    },
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp,
                    textAlign = TextAlign.Center
                )

                if (resultMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "本次結果",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = resultMessage,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "點一下畫面可關閉提示",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.85f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (selectedWord.isEmpty()) {
                            resultMessage = "請先選擇字母"
                            return@Button
                        }

                        val meaning = validWords[selectedWord]

                        if (meaning != null) {
                            val isHintTaskWord = activeHintWord != null &&
                                    selectedWord == activeHintWord

                            val earnedScore = if (isHintTaskWord) {
                                0
                            } else {
                                calculateWordScore(selectedWord)
                            }

                            score += earnedScore
                            successWordCount += 1

                            resultMessage = if (isHintTaskWord) {
                                "成功！$selectedWord = $meaning\n本次使用提示任務，不計分"
                            } else {
                                "成功！$selectedWord = $meaning\n獲得 $earnedScore 分"
                            }

                            var updatedTiles = tiles.map {
                                if (it.isSelected) {
                                    it.copy(
                                        isRemoved = true,
                                        isSelected = false
                                    )
                                } else {
                                    it
                                }
                            }

                            selectedWord = ""
                            activeHintWord = null
                            activeHintMeaning = null
                            activeHintTileIds = emptyList()
                            showHintDialog = false

                            val newRemainingCount = updatedTiles.count { !it.isRemoved }

                            if (newRemainingCount <= 2) {
                                tiles = updatedTiles
                                showLetterResult = true
                                return@Button
                            }

                            val canStillPlayNow = canFormAnyValidWordFromAvailableTiles(updatedTiles)

                            if (!canStillPlayNow) {
                                updatedTiles = reshuffleUntilPlayable(updatedTiles)

                                val canPlayAfterShuffle =
                                    canFormAnyValidWordFromAvailableTiles(updatedTiles)

                                if (canPlayAfterShuffle) {
                                    resultMessage += "\n目前可用牌湊不出單字，已自動洗牌"
                                } else {
                                    resultMessage += "\n剩餘字母已難以組成有效單字，進入結算"
                                    tiles = updatedTiles
                                    showLetterResult = true
                                    return@Button
                                }
                            }

                            tiles = updatedTiles
                        } else {
                            resultMessage = "不是有效單字：$selectedWord"
                            selectedWord = ""

                            activeHintWord = null
                            activeHintMeaning = null
                            activeHintTileIds = emptyList()
                            showHintDialog = false

                            tiles = tiles.map {
                                it.copy(isSelected = false)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "檢查單字")
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedButton(
                    onClick = {
                        selectedWord = ""

                        resultMessage = if (activeHintWord != null) {
                            "提示任務進行中，請依序點選：$activeHintWord"
                        } else {
                            ""
                        }

                        tiles = tiles.map {
                            it.copy(isSelected = false)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "清除選取")
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedButton(
                    onClick = {
                        if (hintCount <= 0) {
                            resultMessage = "本局提示次數已用完"
                            return@OutlinedButton
                        }

                        var cleanTiles = tiles.map {
                            it.copy(isSelected = false)
                        }

                        selectedWord = ""
                        activeHintWord = null
                        activeHintMeaning = null
                        activeHintTileIds = emptyList()
                        showHintDialog = false

                        val remainingCountNow = cleanTiles.count { !it.isRemoved }

                        if (remainingCountNow <= 2) {
                            tiles = cleanTiles
                            showLetterResult = true
                            return@OutlinedButton
                        }

                        var hintResult = findHintResult(cleanTiles)

                        if (hintResult == null) {
                            cleanTiles = reshuffleUntilPlayable(cleanTiles)
                            hintResult = findHintResult(cleanTiles)
                        }

                        if (hintResult == null) {
                            tiles = cleanTiles
                            resultMessage = "目前已無可提示單字，進入結算"
                            showLetterResult = true
                        } else {
                            tiles = cleanTiles
                            activeHintWord = hintResult.word
                            activeHintMeaning = hintResult.meaning
                            activeHintTileIds = hintResult.tileIds
                            hintCount -= 1
                            showHintDialog = true
                            resultMessage = "提示任務已啟動，請完成：${hintResult.word}"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "提示")
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedButton(
                    onClick = {
                        tiles = createNewLetterTiles()
                        selectedWord = ""
                        resultMessage = ""
                        score = 0
                        hintCount = 3
                        activeHintWord = null
                        activeHintMeaning = null
                        activeHintTileIds = emptyList()
                        showHintDialog = false
                        successWordCount = 0
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "重開一局")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun LetterTileBoard(
    tiles: List<LetterTile>,
    hintedTileIds: Set<Int>,
    onTileClick: (LetterTile) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(315.dp)
            .background(
                color = Color(0xFF3E2723),
                shape = RoundedCornerShape(18.dp)
            )
            .border(
                width = 2.dp,
                color = Color(0xFF1B0F0A),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        tiles
            .filter { !it.isRemoved }
            .sortedWith(
                compareBy<LetterTile> { it.layer }
                    .thenBy { it.row }
                    .thenBy { it.col }
            )
            .forEach { tile ->
                val available = isTileAvailable(tile, tiles)
                val isHinted = tile.id in hintedTileIds

                LetterTileButton(
                    tile = tile,
                    isAvailable = available,
                    isHinted = isHinted,
                    onClick = {
                        if (available && !tile.isSelected) {
                            onTileClick(tile)
                        }
                    },
                    modifier = Modifier
                        .offset(
                            x = tileX(tile).dp,
                            y = tileY(tile).dp
                        )
                        .zIndex(tile.layer.toFloat())
                )
            }
    }
}

@Composable
fun LetterTileButton(
    tile: LetterTile,
    isAvailable: Boolean,
    isHinted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tileColor = getLetterColor(tile.letter)

    Box(
        modifier = modifier
            .size(width = 46.dp, height = 54.dp)
            .alpha(if (isAvailable) 1.0f else 0.38f)
            .background(
                color = when {
                    tile.isSelected -> MaterialTheme.colorScheme.primaryContainer
                    isHinted -> Color(0xFFFFF176)
                    isAvailable -> tileColor
                    else -> Color(0xFFE0E0E0)
                },
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = when {
                    tile.isSelected -> 3.dp
                    isHinted -> 4.dp
                    else -> 1.dp
                },
                color = when {
                    tile.isSelected -> MaterialTheme.colorScheme.primary
                    isHinted -> Color(0xFFFF9800)
                    isAvailable -> Color(0xFF2A2A2A)
                    else -> Color.LightGray
                },
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                if (isAvailable && !tile.isSelected) {
                    onClick()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tile.letter,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = if (isAvailable) Color.Black else Color.Gray
        )
    }
}

@Composable
fun LetterTileResultScreen(
    result: LetterTileGameResult,
    onPlayAgainClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val resultText = buildLetterTileResultText(result)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "字母牌陣結果",
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
                ResultTextRow(label = "本局分數", value = result.score.toString())
                ResultTextRow(label = "成功單字", value = "${result.successWordCount} 個")
                ResultTextRow(label = "已消除牌數", value = "${result.removedTileCount} 張")
                ResultTextRow(label = "剩餘牌數", value = "${result.remainingTileCount} 張")
                ResultTextRow(label = "使用提示", value = "${result.hintUsedCount} / 3 次")

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "計分規則",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "3字母：30分\n" +
                            "4字母：50分\n" +
                            "5字母：100分\n" +
                            "6字母以上：150分\n" +
                            "提示任務完成：0分",
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                clipboardManager.setText(AnnotatedString(resultText))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "複製成果")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, resultText)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, "分享成果")
                context.startActivity(shareIntent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "分享成果")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
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

        Spacer(modifier = Modifier.height(80.dp))
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
            fontWeight = if (label == "分數" || label == "本局分數") {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            }
        )
    }
}

fun buildLetterTileResultText(
    result: LetterTileGameResult
): String {
    return """
        Word Grid Quest 模式一：字母牌陣
        分數：${result.score}
        成功單字：${result.successWordCount} 個
        已消除牌數：${result.removedTileCount} 張
        剩餘牌數：${result.remainingTileCount} 張
        使用提示：${result.hintUsedCount} / 3 次
    """.trimIndent()
}