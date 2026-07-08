package com.yutai.wordgridquest

data class DictionaryWord(
    val word: String,
    val meaning: String,
    val level: WordLevel = WordLevel.BASIC
)

enum class WordLevel {
    BASIC,
    COMMON,
    ADVANCED
}

val dictionaryWords: List<DictionaryWord> =
    basicDictionaryWords +
            commonDictionaryWords +
            advancedDictionaryWords

fun buildValidWordsFromDictionary(): Map<String, String> {
    return dictionaryWords.associate { dictionaryWord ->
        dictionaryWord.word.uppercase() to dictionaryWord.meaning
    }
}

fun buildHintQuestionsFromDictionary(): List<HintWordQuestion> {
    return dictionaryWords
        .filter { it.word.length in 3..8 }
        .map { dictionaryWord ->
            val upperWord = dictionaryWord.word.uppercase()

            HintWordQuestion(
                word = upperWord,
                meaning = dictionaryWord.meaning,
                clue = buildWordClue(
                    word = upperWord,
                    clueMode = ClueMode.NORMAL
                ),
                missingAnswer = buildMissingAnswer(
                    word = upperWord,
                    clueMode = ClueMode.NORMAL
                )
            )
        }
}

fun buildHintQuestionsForDifficulty(
    difficulty: HintGameDifficulty
): List<HintWordQuestion> {
    val wordLengthRange = when (difficulty) {
        HintGameDifficulty.PRACTICE -> 3..5
        HintGameDifficulty.CHALLENGE -> 4..6
        HintGameDifficulty.FAST -> 5..8
        HintGameDifficulty.HELL -> 5..10
    }

    return dictionaryWords
        .filter { dictionaryWord ->
            dictionaryWord.word.length in wordLengthRange
        }
        .map { dictionaryWord ->
            val upperWord = dictionaryWord.word.uppercase()

            HintWordQuestion(
                word = upperWord,
                meaning = dictionaryWord.meaning,
                clue = buildWordClue(
                    word = upperWord,
                    clueMode = difficulty.clueMode
                ),
                missingAnswer = buildMissingAnswer(
                    word = upperWord,
                    clueMode = difficulty.clueMode
                )
            )
        }
}

fun buildWordClue(
    word: String,
    clueMode: ClueMode
): String {
    val upperWord = word.uppercase()
    val hiddenIndexes = getHiddenIndexes(
        word = upperWord,
        clueMode = clueMode
    )

    return upperWord.mapIndexed { index, char ->
        if (index in hiddenIndexes) {
            "口"
        } else {
            char.toString()
        }
    }.joinToString("")
}

fun buildMissingAnswer(
    word: String,
    clueMode: ClueMode
): String {
    val upperWord = word.uppercase()
    val hiddenIndexes = getHiddenIndexes(
        word = upperWord,
        clueMode = clueMode
    )

    return upperWord.mapIndexedNotNull { index, char ->
        if (index in hiddenIndexes) {
            char
        } else {
            null
        }
    }.joinToString("")
}

fun getHiddenIndexes(
    word: String,
    clueMode: ClueMode
): Set<Int> {
    if (word.length <= 1) {
        return emptySet()
    }

    return when (clueMode) {
        ClueMode.EASY -> getEasyHiddenIndexes(word)
        ClueMode.NORMAL -> getNormalHiddenIndexes(word)
        ClueMode.HARD -> getHardHiddenIndexes(word)
        ClueMode.HELL -> word.indices.toSet()
    }
}

fun getEasyHiddenIndexes(word: String): Set<Int> {
    return when (word.length) {
        2 -> setOf(1)
        3 -> setOf(1)
        4 -> setOf(2)
        5 -> setOf(2)
        6 -> setOf(2, 4)
        7 -> setOf(2, 4)
        else -> setOf(2, 4, 6)
    }
}

fun getNormalHiddenIndexes(word: String): Set<Int> {
    return when (word.length) {
        2 -> setOf(1)
        3 -> setOf(1)
        4 -> setOf(1, 2)
        5 -> setOf(1, 3)
        6 -> setOf(1, 3)
        7 -> setOf(1, 3, 5)
        else -> setOf(1, 3, 5)
    }
}

fun getHardHiddenIndexes(word: String): Set<Int> {
    return when (word.length) {
        2 -> setOf(0, 1)
        3 -> setOf(1, 2)
        4 -> setOf(1, 2)
        5 -> setOf(1, 2, 3)
        6 -> setOf(1, 2, 3, 4)
        7 -> setOf(1, 2, 3, 4, 5)
        else -> word.indices
            .filter { index ->
                index != 0 && index != word.lastIndex
            }
            .toSet()
    }
}