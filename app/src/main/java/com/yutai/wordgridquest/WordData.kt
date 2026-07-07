package com.yutai.wordgridquest

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