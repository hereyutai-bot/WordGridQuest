package com.yutai.wordgridquest

fun buildHintResultText(
    difficulty: HintGameDifficulty,
    score: Int,
    totalQuestions: Int,
    correctCount: Int,
    wrongCount: Int,
    skipCount: Int
): String {
    val accuracy = if (totalQuestions == 0) {
        0
    } else {
        correctCount * 100 / totalQuestions
    }

    return """
        Word Grid Quest 模式二：提示拼字
        模式：${difficulty.title}
        分數：$score
        總題數：$totalQuestions
        答對：$correctCount
        答錯：$wrongCount
        跳過：$skipCount
        正確率：$accuracy%
    """.trimIndent()
}