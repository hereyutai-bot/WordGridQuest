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
    DictionaryWord("RIVER", "河流"),

    DictionaryWord("SUN", "太陽"),
    DictionaryWord("MOON", "月亮"),
    DictionaryWord("STAR", "星星"),
    DictionaryWord("SKY", "天空"),
    DictionaryWord("RAIN", "雨"),
    DictionaryWord("SNOW", "雪"),
    DictionaryWord("WIND", "風"),
    DictionaryWord("FIRE", "火"),
    DictionaryWord("EARTH", "地球"),
    DictionaryWord("SEA", "海"),
    DictionaryWord("LAKE", "湖"),
    DictionaryWord("ROAD", "道路"),
    DictionaryWord("CAR", "汽車"),
    DictionaryWord("BUS", "公車"),
    DictionaryWord("BIKE", "腳踏車"),
    DictionaryWord("BOAT", "船"),
    DictionaryWord("DOOR", "門"),
    DictionaryWord("WINDOW", "窗戶"),
    DictionaryWord("ROOM", "房間"),
    DictionaryWord("BED", "床"),

    DictionaryWord("FOOD", "食物"),
    DictionaryWord("RICE", "米飯"),
    DictionaryWord("BREAD", "麵包"),
    DictionaryWord("MILK", "牛奶"),
    DictionaryWord("EGG", "蛋"),
    DictionaryWord("FISH", "魚"),
    DictionaryWord("MEAT", "肉"),
    DictionaryWord("SOUP", "湯"),
    DictionaryWord("CAKE", "蛋糕"),
    DictionaryWord("SUGAR", "糖"),
    DictionaryWord("SALT", "鹽"),
    DictionaryWord("FRUIT", "水果"),
    DictionaryWord("BANANA", "香蕉"),
    DictionaryWord("ORANGE", "橘子"),
    DictionaryWord("GRAPE", "葡萄"),
    DictionaryWord("LEMON", "檸檬"),
    DictionaryWord("COFFEE", "咖啡"),
    DictionaryWord("TEA", "茶"),
    DictionaryWord("JUICE", "果汁"),
    DictionaryWord("CUP", "杯子"),

    DictionaryWord("MAN", "男人"),
    DictionaryWord("WOMAN", "女人"),
    DictionaryWord("BOY", "男孩"),
    DictionaryWord("GIRL", "女孩"),
    DictionaryWord("BABY", "嬰兒"),
    DictionaryWord("FATHER", "父親"),
    DictionaryWord("MOTHER", "母親"),
    DictionaryWord("BROTHER", "兄弟"),
    DictionaryWord("SISTER", "姊妹"),
    DictionaryWord("FRIEND", "朋友"),
    DictionaryWord("TEACHER", "老師"),
    DictionaryWord("STUDENT", "學生"),
    DictionaryWord("DOCTOR", "醫生"),
    DictionaryWord("NURSE", "護士"),
    DictionaryWord("POLICE", "警察"),
    DictionaryWord("DRIVER", "司機"),
    DictionaryWord("WORKER", "工人"),
    DictionaryWord("PLAYER", "玩家"),
    DictionaryWord("FAMILY", "家庭"),
    DictionaryWord("PEOPLE", "人們"),

    DictionaryWord("HAND", "手"),
    DictionaryWord("HEAD", "頭"),
    DictionaryWord("FACE", "臉"),
    DictionaryWord("EYE", "眼睛"),
    DictionaryWord("EAR", "耳朵"),
    DictionaryWord("NOSE", "鼻子"),
    DictionaryWord("MOUTH", "嘴巴"),
    DictionaryWord("HAIR", "頭髮"),
    DictionaryWord("FOOT", "腳"),
    DictionaryWord("LEG", "腿"),
    DictionaryWord("ARM", "手臂"),
    DictionaryWord("BODY", "身體"),
    DictionaryWord("HEART", "心"),
    DictionaryWord("BACK", "背部"),
    DictionaryWord("VOICE", "聲音"),

    DictionaryWord("RED", "紅色"),
    DictionaryWord("BLUE", "藍色"),
    DictionaryWord("GREEN", "綠色"),
    DictionaryWord("BLACK", "黑色"),
    DictionaryWord("WHITE", "白色"),
    DictionaryWord("YELLOW", "黃色"),
    DictionaryWord("BROWN", "棕色"),
    DictionaryWord("PINK", "粉紅色"),
    DictionaryWord("GRAY", "灰色"),
    DictionaryWord("GOLD", "金色"),

    DictionaryWord("BIG", "大的"),
    DictionaryWord("SMALL", "小的"),
    DictionaryWord("LONG", "長的"),
    DictionaryWord("SHORT", "短的"),
    DictionaryWord("FAST", "快的"),
    DictionaryWord("SLOW", "慢的"),
    DictionaryWord("HOT", "熱的"),
    DictionaryWord("COLD", "冷的"),
    DictionaryWord("NEW", "新的"),
    DictionaryWord("OLD", "舊的"),
    DictionaryWord("GOOD", "好的"),
    DictionaryWord("BAD", "壞的"),
    DictionaryWord("EASY", "簡單的"),
    DictionaryWord("HARD", "困難的"),
    DictionaryWord("CLEAN", "乾淨的"),
    DictionaryWord("DIRTY", "髒的"),
    DictionaryWord("RIGHT", "正確的"),
    DictionaryWord("WRONG", "錯誤的"),
    DictionaryWord("YOUNG", "年輕的"),
    DictionaryWord("QUIET", "安靜的")
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