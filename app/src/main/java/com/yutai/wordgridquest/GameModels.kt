package com.yutai.wordgridquest

enum class Screen {
    HOME,
    MODE_SELECT,
    LETTER_TILE,
    HINT_WORD,
    RANKING,
    STUDY_RECORD,
    HELP,
    DICTIONARY_CHECK
}

enum class GameModeType(
    val displayName: String
) {
    LETTER_TILE(
        displayName = "模式一：字母牌陣"
    ),
    HINT_WORD(
        displayName = "模式二：提示拼字"
    )
}

data class RankingRecord(
    val playerName: String,
    val modeType: GameModeType,
    val score: Int,
    val detailText: String,
    val playedAtText: String
)

data class StudyRecord(
    val modeType: GameModeType,
    val score: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val skipCount: Int,
    val playedAtText: String,
    val note: String
)

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