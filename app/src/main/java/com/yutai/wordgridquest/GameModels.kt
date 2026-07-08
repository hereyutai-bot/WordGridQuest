package com.yutai.wordgridquest

enum class Screen {
    HOME,
    MODE_SELECT,
    LETTER_TILE,
    HINT_WORD,
    RANKING,
    STUDY_RECORD,
    HELP
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
    val skipCount: Int,
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