package com.yutai.wordgridquest

data class DictionaryCheckResult(
    val totalCount: Int,
    val duplicateWords: List<String>,
    val tooShortWords: List<String>,
    val emptyMeaningWords: List<String>,
    val invalidFormatWords: List<String>
)

data class DictionarySourceCount(
    val sourceName: String,
    val count: Int
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

fun getDictionarySourceCounts(): List<DictionarySourceCount> {
    return listOf(
        DictionarySourceCount(
            sourceName = "BasicDictionaryWords",
            count = basicDictionaryWords.size
        ),
        DictionarySourceCount(
            sourceName = "CommonDictionaryWords",
            count = commonDictionaryWords.size
        ),
        DictionarySourceCount(
            sourceName = "CommonDictionaryWordsPart2",
            count = commonDictionaryWordsPart2.size
        ),
        DictionarySourceCount(
            sourceName = "AdvancedDictionaryWords",
            count = advancedDictionaryWords.size
        )
    )
}

fun buildDictionaryCheckReport(
    result: DictionaryCheckResult = checkDictionaryWords()
): String {
    val sourceCounts = getDictionarySourceCounts()

    return buildString {
        appendLine("字典檢查報告")
        appendLine("--------------------")
        appendLine("總單字數：${result.totalCount}")
        appendLine()

        appendLine("字典來源統計")
        sourceCounts.forEach { sourceCount ->
            appendLine("- ${sourceCount.sourceName}：${sourceCount.count} 筆")
        }

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