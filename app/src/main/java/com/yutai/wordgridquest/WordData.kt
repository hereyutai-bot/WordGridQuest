package com.yutai.wordgridquest
val hintWordQuestions: List<HintWordQuestion> = buildHintQuestionsFromDictionary()
val validWords: Map<String, String> = buildValidWordsFromDictionary()

val topLayerStartingLetters = listOf(
    "C", "A", "T",
    "D", "O", "G"
)

val remainingStartingLetters = listOf(
    "B", "O", "O", "K",
    "A", "P", "P", "L", "E",
    "G", "A", "M", "E",
    "F", "I", "S", "H",
    "B", "I", "R", "D",
    "S", "U", "N",
    "M", "O", "O", "N",
    "S", "T", "A", "R",
    "T", "R", "E", "E",
    "H", "O", "U", "S", "E"
)