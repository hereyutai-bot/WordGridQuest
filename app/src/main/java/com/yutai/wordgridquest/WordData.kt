package com.yutai.wordgridquest
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
    ),
    HintWordQuestion(
        word = "SUN",
        meaning = "太陽",
        clue = "S _ N",
        missingAnswer = "U"
    ),
    HintWordQuestion(
        word = "MOON",
        meaning = "月亮",
        clue = "M _ O N",
        missingAnswer = "O"
    ),
    HintWordQuestion(
        word = "STAR",
        meaning = "星星",
        clue = "S T _ R",
        missingAnswer = "A"
    ),
    HintWordQuestion(
        word = "TREE",
        meaning = "樹",
        clue = "T R _ E",
        missingAnswer = "E"
    ),
    HintWordQuestion(
        word = "FISH",
        meaning = "魚",
        clue = "F _ S H",
        missingAnswer = "I"
    ),
    HintWordQuestion(
        word = "BIRD",
        meaning = "鳥",
        clue = "B I _ D",
        missingAnswer = "R"
    ),
    HintWordQuestion(
        word = "MILK",
        meaning = "牛奶",
        clue = "M I _ K",
        missingAnswer = "L"
    ),
    HintWordQuestion(
        word = "CAKE",
        meaning = "蛋糕",
        clue = "C A _ E",
        missingAnswer = "K"
    ),
    HintWordQuestion(
        word = "HAND",
        meaning = "手",
        clue = "H A _ D",
        missingAnswer = "N"
    ),
    HintWordQuestion(
        word = "FOOD",
        meaning = "食物",
        clue = "F O _ D",
        missingAnswer = "O"
    ),
    HintWordQuestion(
        word = "RAIN",
        meaning = "雨",
        clue = "R A _ N",
        missingAnswer = "I"
    ),
    HintWordQuestion(
        word = "HOUSE",
        meaning = "房子",
        clue = "H O _ S E",
        missingAnswer = "U"
    ),
    HintWordQuestion(
        word = "MOUSE",
        meaning = "老鼠",
        clue = "M O _ S E",
        missingAnswer = "U"
    ),
    HintWordQuestion(
        word = "TABLE",
        meaning = "桌子",
        clue = "T A _ L E",
        missingAnswer = "B"
    ),
    HintWordQuestion(
        word = "CHAIR",
        meaning = "椅子",
        clue = "C H _ I R",
        missingAnswer = "A"
    ),
    HintWordQuestion(
        word = "PEN",
        meaning = "筆",
        clue = "P _ N",
        missingAnswer = "E"
    ),
    HintWordQuestion(
        word = "BED",
        meaning = "床",
        clue = "B _ D",
        missingAnswer = "E"
    ),
    HintWordQuestion(
        word = "CAR",
        meaning = "車",
        clue = "C _ R",
        missingAnswer = "A"
    ),
    HintWordQuestion(
        word = "BUS",
        meaning = "公車",
        clue = "B _ S",
        missingAnswer = "U"
    ),
    HintWordQuestion(
        word = "CUP",
        meaning = "杯子",
        clue = "C _ P",
        missingAnswer = "U"
    ),
    HintWordQuestion(
        word = "BOX",
        meaning = "盒子",
        clue = "B _ X",
        missingAnswer = "O"
    ),
    HintWordQuestion(
        word = "EGG",
        meaning = "蛋",
        clue = "E _ G",
        missingAnswer = "G"
    ),
    HintWordQuestion(
        word = "MAP",
        meaning = "地圖",
        clue = "M _ P",
        missingAnswer = "A"
    ),
    HintWordQuestion(
        word = "RED",
        meaning = "紅色",
        clue = "R _ D",
        missingAnswer = "E"
    ),
    HintWordQuestion(
        word = "BLUE",
        meaning = "藍色",
        clue = "B L _ E",
        missingAnswer = "U"
    )
)
val validWords = mapOf(
    // 3 個字母
    "CAT" to "貓",
    "DOG" to "狗",
    "SUN" to "太陽",
    "SET" to "設置",
    "RUN" to "跑",
    "RED" to "紅色",
    "BED" to "床",
    "CAR" to "車",
    "BUS" to "公車",
    "PEN" to "筆",
    "EGG" to "蛋",
    "MAP" to "地圖",
    "BAT" to "蝙蝠／球棒",
    "BOX" to "盒子",
    "CUP" to "杯子",

    // 4 個字母
    "BOOK" to "書",
    "GAME" to "遊戲",
    "FISH" to "魚",
    "BIRD" to "鳥",
    "MOON" to "月亮",
    "STAR" to "星星",
    "TREE" to "樹",
    "MILK" to "牛奶",
    "CAKE" to "蛋糕",
    "HAND" to "手",
    "FOOD" to "食物",
    "RAIN" to "雨",

    // 5 個字母
    "APPLE" to "蘋果",
    "HOUSE" to "房子",
    "MOUSE" to "老鼠",
    "TABLE" to "桌子",
    "CHAIR" to "椅子"
)

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