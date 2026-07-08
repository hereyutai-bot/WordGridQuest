package com.yutai.wordgridquest

data class DictionaryCheckResult(
    val totalCount: Int,
    val duplicateWords: List<String>,
    val tooShortWords: List<String>,
    val emptyMeaningWords: List<String>,
    val invalidFormatWords: List<String>
)

fun checkDictionaryWords(
    words: List<DictionaryWord> = dictionaryWords
): DictionaryCheckResult {
    val duplicateWords = words
        .groupBy { it.word.uppercase() }
        .filter { it.value.size > 1 }
        .keys
        .sorted()

    val tooShortWords = words
        .filter { it.word.length < 3 }
        .map { it.word }
        .sorted()

    val emptyMeaningWords = words
        .filter { it.meaning.isBlank() }
        .map { it.word }
        .sorted()

    val invalidFormatWords = words
        .filter { dictionaryWord ->
            dictionaryWord.word != dictionaryWord.word.uppercase() ||
                    dictionaryWord.word.any { char -> char !in 'A'..'Z' }
        }
        .map { it.word }
        .sorted()

    return DictionaryCheckResult(
        totalCount = words.size,
        duplicateWords = duplicateWords,
        tooShortWords = tooShortWords,
        emptyMeaningWords = emptyMeaningWords,
        invalidFormatWords = invalidFormatWords
    )
}

fun buildDictionaryCheckReport(
    result: DictionaryCheckResult = checkDictionaryWords()
): String {
    return buildString {
        appendLine("字典檢查報告")
        appendLine("--------------------")
        appendLine("總單字數：${result.totalCount}")
        appendLine()

        appendLine("重複單字：${result.duplicateWords.size}")
        if (result.duplicateWords.isNotEmpty()) {
            result.duplicateWords.forEach { word ->
                appendLine("- $word")
            }
        }

        appendLine()
        appendLine("過短單字（少於 3 字母）：${result.tooShortWords.size}")
        if (result.tooShortWords.isNotEmpty()) {
            result.tooShortWords.forEach { word ->
                appendLine("- $word")
            }
        }

        appendLine()
        appendLine("中文意思空白：${result.emptyMeaningWords.size}")
        if (result.emptyMeaningWords.isNotEmpty()) {
            result.emptyMeaningWords.forEach { word ->
                appendLine("- $word")
            }
        }

        appendLine()
        appendLine("格式錯誤單字（非全大寫 A-Z）：${result.invalidFormatWords.size}")
        if (result.invalidFormatWords.isNotEmpty()) {
            result.invalidFormatWords.forEach { word ->
                appendLine("- $word")
            }
        }
    }
}