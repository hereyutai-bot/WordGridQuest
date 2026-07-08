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

val dictionaryWords = listOf(
    DictionaryWord("APPLE", "蘋果"),
    DictionaryWord("BOOK", "書"),
    DictionaryWord("CAT", "貓"),
    DictionaryWord("DOG", "狗"),
    DictionaryWord("TREE", "樹"),
    DictionaryWord("WATER", "水"),
    DictionaryWord("SCHOOL", "學校"),
    DictionaryWord("HOUSE", "房子"),
    DictionaryWord("CHAIR", "椅子"),
    DictionaryWord("TABLE", "桌子"),
    DictionaryWord("PHONE", "電話"),
    DictionaryWord("MUSIC", "音樂"),
    DictionaryWord("HAPPY", "快樂的"),
    DictionaryWord("SMILE", "微笑"),
    DictionaryWord("LIGHT", "光"),
    DictionaryWord("NIGHT", "夜晚"),
    DictionaryWord("MONEY", "錢"),
    DictionaryWord("TRAIN", "火車"),
    DictionaryWord("PLANE", "飛機"),
    DictionaryWord("RIVER", "河流")
)

fun buildValidWordsFromDictionary(): Map<String, String> {
    return dictionaryWords.associate { dictionaryWord ->
        dictionaryWord.word.uppercase() to dictionaryWord.meaning
    }
}

fun buildHintQuestionsFromDictionary(): List<HintWordQuestion> {
    return dictionaryWords
        .filter { it.word.length in 3..8 }
        .map { dictionaryWord ->
            HintWordQuestion(
                word = dictionaryWord.word.uppercase(),
                meaning = dictionaryWord.meaning,
                clue = buildWordClue(dictionaryWord.word.uppercase()),
                missingAnswer = dictionaryWord.word.uppercase()
            )
        }
}

fun buildWordClue(word: String): String {
    if (word.length <= 2) {
        return word
    }

    return word.mapIndexed { index, char ->
        if (index == 0 || index == word.lastIndex) {
            char.toString()
        } else {
            "_"
        }
    }.joinToString(" ")
}