package com.yutai.wordgridquest

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
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

data class HintWordQuestion(
    val word: String,
    val meaning: String,
    val clue: String,
    val missingAnswer: String
)

data class GameResult(
    val score: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val totalQuestions: Int
)

data class LetterTile(
    val id: Int,
    val letter: String,
    val row: Int,
    val col: Int,
    val layer: Int,
    val isSelected: Boolean = false,
    val isRemoved: Boolean = false
)

data class TileSlot(
    val id: Int,
    val row: Int,
    val col: Int,
    val layer: Int
)

data class LetterTileGameResult(
    val score: Int,
    val successWordCount: Int,
    val removedTileCount: Int,
    val remainingTileCount: Int,
    val hintUsedCount: Int
)

data class HintResult(
    val word: String,
    val meaning: String,
    val tileIds: List<Int>
)

val hintWordQuestions = listOf(
    HintWordQuestion(
        word = "APPLE",
        meaning = "蘋果",
        clue = "A _ P L E",
        missingAnswer = "P"
    ),
    HintWordQuestion(
        word = "BOOK",
        meaning = "書",
        clue = "B _ O K",
        missingAnswer = "O"
    ),
    HintWordQuestion(
        word = "CAT",
        meaning = "貓",
        clue = "C _ T",
        missingAnswer = "A"
    ),
    HintWordQuestion(
        word = "DOG",
        meaning = "狗",
        clue = "D _ G",
        missingAnswer = "O"
    ),
    HintWordQuestion(
        word = "GAME",
        meaning = "遊戲",
        clue = "G A _ E",
        missingAnswer = "M"
    )
)

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

        Screen.LETTER_TILE -> LetterTileScreen(
            onBackClick = { currentScreen = Screen.MODE_SELECT }
        )

        Screen.HINT_WORD -> HintWordScreen(
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

fun createNewLetterTiles(): List<LetterTile> {
    val topSlots = tileSlots.filter { it.layer == 2 }
    val otherSlots = tileSlots.filter { it.layer != 2 }

    val shuffledTopLetters = topLayerStartingLetters.shuffled()
    val shuffledOtherLetters = remainingStartingLetters.shuffled()

    val topTiles = topSlots.mapIndexed { index, slot ->
        LetterTile(
            id = slot.id,
            letter = shuffledTopLetters[index],
            row = slot.row,
            col = slot.col,
            layer = slot.layer
        )
    }

    val otherTiles = otherSlots.mapIndexed { index, slot ->
        LetterTile(
            id = slot.id,
            letter = shuffledOtherLetters[index],
            row = slot.row,
            col = slot.col,
            layer = slot.layer
        )
    }

    return (otherTiles + topTiles).sortedBy { it.id }
}

fun tileX(tile: LetterTile): Int {
    return (tile.col - 3) * 42 + tile.layer * 14
}

fun tileY(tile: LetterTile): Int {
    return tile.row * 54 + 72 - tile.layer * 20
}

fun isOverlapping(
    lowerTile: LetterTile,
    upperTile: LetterTile
): Boolean {
    val lowerLeft = tileX(lowerTile)
    val lowerRight = lowerLeft + 52
    val lowerTop = tileY(lowerTile)
    val lowerBottom = lowerTop + 62

    val upperLeft = tileX(upperTile)
    val upperRight = upperLeft + 52
    val upperTop = tileY(upperTile)
    val upperBottom = upperTop + 62

    return lowerLeft < upperRight &&
            lowerRight > upperLeft &&
            lowerTop < upperBottom &&
            lowerBottom > upperTop
}

fun isTileAvailable(
    tile: LetterTile,
    tiles: List<LetterTile>
): Boolean {
    if (tile.isRemoved) {
        return false
    }

    val hasOverlappingTileAbove = tiles.any {
        !it.isRemoved &&
                it.layer > tile.layer &&
                isOverlapping(
                    lowerTile = tile,
                    upperTile = it
                )
    }

    return !hasOverlappingTileAbove
}

fun canFormWordFromLetters(
    word: String,
    letters: List<String>
): Boolean {
    val letterCounts = letters.groupingBy { it }.eachCount().toMutableMap()

    word.forEach { char ->
        val key = char.toString()
        val count = letterCounts[key] ?: 0

        if (count <= 0) {
            return false
        }

        letterCounts[key] = count - 1
    }

    return true
}

fun canFormAnyValidWordFromAvailableTiles(
    tiles: List<LetterTile>
): Boolean {
    val availableLetters = tiles
        .filter { isTileAvailable(it, tiles) }
        .filter { !it.isSelected }
        .map { it.letter }

    return validWords.keys.any { word ->
        canFormWordFromLetters(
            word = word,
            letters = availableLetters
        )
    }
}

fun findHintResult(
    tiles: List<LetterTile>
): HintResult? {
    val availableTiles = tiles
        .filter { isTileAvailable(it, tiles) }
        .filter { !it.isSelected }
        .sortedWith(
            compareBy<LetterTile> { it.layer }
                .thenBy { it.row }
                .thenBy { it.col }
        )

    validWords.entries.shuffled().forEach { entry ->
        val word = entry.key
        val meaning = entry.value
        val usedTileIds = mutableListOf<Int>()

        word.forEach { char ->
            val letter = char.toString()

            val tile = availableTiles.firstOrNull {
                it.letter == letter && it.id !in usedTileIds
            }

            if (tile != null) {
                usedTileIds.add(tile.id)
            }
        }

        if (usedTileIds.size == word.length) {
            return HintResult(
                word = word,
                meaning = meaning,
                tileIds = usedTileIds
            )
        }
    }

    return null
}

fun reshuffleRemainingTiles(
    tiles: List<LetterTile>
): List<LetterTile> {
    val remainingTiles = tiles.filter { !it.isRemoved }
    val removedTiles = tiles.filter { it.isRemoved }

    val shuffledLetters = remainingTiles
        .map { it.letter }
        .shuffled()

    val shuffledRemainingTiles = remainingTiles.mapIndexed { index, tile ->
        tile.copy(
            letter = shuffledLetters[index],
            isSelected = false
        )
    }

    return (removedTiles + shuffledRemainingTiles).sortedBy { it.id }
}

fun reshuffleUntilPlayable(
    tiles: List<LetterTile>
): List<LetterTile> {
    var newTiles = tiles.map {
        it.copy(isSelected = false)
    }

    val remainingCount = newTiles.count { !it.isRemoved }

    if (remainingCount <= 2) {
        return newTiles
    }

    repeat(300) {
        newTiles = reshuffleRemainingTiles(newTiles)

        if (canFormAnyValidWordFromAvailableTiles(newTiles)) {
            return newTiles
        }
    }

    return newTiles
}

fun calculateWordScore(word: String): Int {
    return when (word.length) {
        3 -> 30
        4 -> 50
        5 -> 100
        else -> 150
    }
}

fun getLetterColor(letter: String): Color {
    return when (letter.uppercase()) {
        "A" -> Color(0xFFFFCDD2)
        "B" -> Color(0xFFF8BBD0)
        "C" -> Color(0xFFE1BEE7)
        "D" -> Color(0xFFD1C4E9)
        "E" -> Color(0xFFC5CAE9)
        "F" -> Color(0xFFBBDEFB)
        "G" -> Color(0xFFB3E5FC)
        "H" -> Color(0xFFB2EBF2)
        "I" -> Color(0xFFB2DFDB)
        "J" -> Color(0xFFC8E6C9)
        "K" -> Color(0xFFDCEDC8)
        "L" -> Color(0xFFF0F4C3)
        "M" -> Color(0xFFFFF9C4)
        "N" -> Color(0xFFFFECB3)
        "O" -> Color(0xFFFFE0B2)
        "P" -> Color(0xFFFFCCBC)
        "Q" -> Color(0xFFD7CCC8)
        "R" -> Color(0xFFCFD8DC)
        "S" -> Color(0xFFFFF59D)
        "T" -> Color(0xFFA5D6A7)
        "U" -> Color(0xFF80CBC4)
        "V" -> Color(0xFF81D4FA)
        "W" -> Color(0xFF90CAF9)
        "X" -> Color(0xFF9FA8DA)
        "Y" -> Color(0xFFCE93D8)
        "Z" -> Color(0xFFEF9A9A)
        else -> Color.White
    }
}

@Composable
fun LetterTileScreen(
    onBackClick: () -> Unit
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

    val remainingTileCount = tiles.count { !it.isRemoved }
    val removedTileCount = tiles.count { it.isRemoved }
    val hintUsedCount = 3 - hintCount

    if (showLetterResult) {
        LetterTileResultScreen(
            result = LetterTileGameResult(
                score = score,
                successWordCount = successWordCount,
                removedTileCount = removedTileCount,
                remainingTileCount = remainingTileCount,
                hintUsedCount = hintUsedCount
            ),
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
            .padding(14.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "字母牌陣",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "灰色牌被上層壓住，不能點選",
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "3字30｜4字50｜5字100｜6字以上150",
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )

        Text(
            text = "提示任務完成不計分",
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "分數：$score　提示：$hintCount / 3　剩餘：$remainingTileCount",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

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

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "目前選取",
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (selectedWord.isEmpty()) "尚未選取字母" else selectedWord,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

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

                Spacer(modifier = Modifier.height(10.dp))

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

                Spacer(modifier = Modifier.height(10.dp))

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

                Spacer(modifier = Modifier.height(10.dp))

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

        Spacer(modifier = Modifier.height(8.dp))

        if (resultMessage.isNotEmpty()) {
            Text(
                text = resultMessage,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "返回模式選擇")
        }
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
            .height(330.dp)
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
            .size(width = 52.dp, height = 62.dp)
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
            fontSize = 25.sp,
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
    Column(
        modifier = Modifier
            .fillMaxSize()
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
    }
}
@Composable
fun HintWordScreen(
    onBackClick: () -> Unit
) {
    var questionIndex by remember { mutableIntStateOf(0) }
    var userInput by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf("") }
    var score by remember { mutableIntStateOf(0) }
    var correctCount by remember { mutableIntStateOf(0) }
    var wrongCount by remember { mutableIntStateOf(0) }
    var hasAnswered by remember { mutableStateOf(false) }
    var showResult by remember { mutableStateOf(false) }

    val question = hintWordQuestions[questionIndex]
    val totalQuestions = hintWordQuestions.size

    if (showResult) {
        GameResultScreen(
            result = GameResult(
                score = score,
                correctCount = correctCount,
                wrongCount = wrongCount,
                totalQuestions = totalQuestions
            ),
            onPlayAgainClick = {
                questionIndex = 0
                userInput = ""
                resultMessage = ""
                score = 0
                correctCount = 0
                wrongCount = 0
                hasAnswered = false
                showResult = false
            },
            onBackClick = onBackClick
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "提示拼字",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "第 ${questionIndex + 1} / $totalQuestions 題",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "提示：${question.meaning}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = question.clue,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "請輸入缺少的字母",
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = userInput,
                    onValueChange = {
                        userInput = it.uppercase().take(1)
                    },
                    label = {
                        Text(text = "答案")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (!hasAnswered) {
                            if (userInput == question.missingAnswer) {
                                resultMessage = "答對了！${question.word} = ${question.meaning}"
                                score += 10
                                correctCount += 1
                            } else {
                                resultMessage =
                                    "答錯了，正確答案是 ${question.missingAnswer}，完整單字是 ${question.word}"
                                wrongCount += 1
                            }

                            hasAnswered = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "檢查答案")
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        userInput = ""
                        resultMessage = ""
                        hasAnswered = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "重新作答")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (questionIndex < totalQuestions - 1) {
                            questionIndex += 1
                            userInput = ""
                            resultMessage = ""
                            hasAnswered = false
                        } else {
                            showResult = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (questionIndex < totalQuestions - 1) {
                            "下一題"
                        } else {
                            "結束遊戲"
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "分數：$score",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "答對：$correctCount　答錯：$wrongCount",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (resultMessage.isNotEmpty()) {
            Text(
                text = resultMessage,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "返回模式選擇")
        }
    }
}

@Composable
fun GameResultScreen(
    result: GameResult,
    onPlayAgainClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val accuracy = if (result.totalQuestions > 0) {
        result.correctCount * 100 / result.totalQuestions
    } else {
        0
    }

    val shareText = buildShareText(
        score = result.score,
        correctCount = result.correctCount,
        wrongCount = result.wrongCount,
        totalQuestions = result.totalQuestions,
        accuracy = accuracy
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "遊戲結果",
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
                ResultTextRow(label = "模式", value = "提示拼字")
                ResultTextRow(label = "分數", value = result.score.toString())
                ResultTextRow(label = "總題數", value = "${result.totalQuestions} 題")
                ResultTextRow(label = "答對", value = "${result.correctCount} 題")
                ResultTextRow(label = "答錯", value = "${result.wrongCount} 題")
                ResultTextRow(label = "正確率", value = "$accuracy%")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                copyTextToClipboard(context, shareText)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "複製成果")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                shareResult(context, shareText)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "分享成果")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
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

fun buildShareText(
    score: Int,
    correctCount: Int,
    wrongCount: Int,
    totalQuestions: Int,
    accuracy: Int
): String {
    return """
        我剛完成 Word Grid Quest 英文單字挑戰！

        模式：提示拼字
        分數：$score
        總題數：$totalQuestions 題
        答對：$correctCount 題
        答錯：$wrongCount 題
        正確率：$accuracy%

        一起來挑戰英文單字吧！
        #WordGridQuest #英文學習 #單字遊戲
    """.trimIndent()
}

fun copyTextToClipboard(
    context: Context,
    text: String
) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("WordGridQuestResult", text)
    clipboard.setPrimaryClip(clip)

    Toast.makeText(
        context,
        "成果已複製",
        Toast.LENGTH_SHORT
    ).show()
}

fun shareResult(
    context: Context,
    text: String
) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(
        sendIntent,
        "分享成果"
    )

    context.startActivity(shareIntent)
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