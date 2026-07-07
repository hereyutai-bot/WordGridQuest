package com.yutai.wordgridquest

import androidx.compose.ui.graphics.Color

fun createNewLetterTiles(): List<LetterTile> {
    val topSlots = tileSlots.filter { it.layer == 2 }
    val otherSlots = tileSlots.filter { it.layer != 2 }

    val shuffledTopLetters = topLayerStartingLetters.shuffled()
    val shuffledOtherLetters = remainingStartingLetters.shuffled()

    val topTiles = topSlots.mapIndexed { index, slot ->
        LetterTile(
            id = slot.id,
            letter = shuffledTopLetters[index],
            row = slot.row,
            col = slot.col,
            layer = slot.layer
        )
    }

    val otherTiles = otherSlots.mapIndexed { index, slot ->
        LetterTile(
            id = slot.id,
            letter = shuffledOtherLetters[index],
            row = slot.row,
            col = slot.col,
            layer = slot.layer
        )
    }

    return (otherTiles + topTiles).sortedBy { it.id }
}

fun tileX(tile: LetterTile): Int {
    return (tile.col - 3) * 42 + tile.layer * 14
}

fun tileY(tile: LetterTile): Int {
    return tile.row * 54 + 72 - tile.layer * 20
}

fun isOverlapping(
    lowerTile: LetterTile,
    upperTile: LetterTile
): Boolean {
    val lowerLeft = tileX(lowerTile)
    val lowerRight = lowerLeft + 52
    val lowerTop = tileY(lowerTile)
    val lowerBottom = lowerTop + 62

    val upperLeft = tileX(upperTile)
    val upperRight = upperLeft + 52
    val upperTop = tileY(upperTile)
    val upperBottom = upperTop + 62

    return lowerLeft < upperRight &&
            lowerRight > upperLeft &&
            lowerTop < upperBottom &&
            lowerBottom > upperTop
}

fun isTileAvailable(
    tile: LetterTile,
    tiles: List<LetterTile>
): Boolean {
    if (tile.isRemoved) {
        return false
    }

    val hasOverlappingTileAbove = tiles.any {
        !it.isRemoved &&
                it.layer > tile.layer &&
                isOverlapping(
                    lowerTile = tile,
                    upperTile = it
                )
    }

    return !hasOverlappingTileAbove
}

fun canFormWordFromLetters(
    word: String,
    letters: List<String>
): Boolean {
    val letterCounts = letters.groupingBy { it }.eachCount().toMutableMap()

    word.forEach { char ->
        val key = char.toString()
        val count = letterCounts[key] ?: 0

        if (count <= 0) {
            return false
        }

        letterCounts[key] = count - 1
    }

    return true
}

fun canFormAnyValidWordFromAvailableTiles(
    tiles: List<LetterTile>
): Boolean {
    val availableLetters = tiles
        .filter { isTileAvailable(it, tiles) }
        .filter { !it.isSelected }
        .map { it.letter }

    return validWords.keys.any { word ->
        canFormWordFromLetters(
            word = word,
            letters = availableLetters
        )
    }
}

fun findHintResult(
    tiles: List<LetterTile>
): HintResult? {
    val availableTiles = tiles
        .filter { isTileAvailable(it, tiles) }
        .filter { !it.isSelected }
        .sortedWith(
            compareBy<LetterTile> { it.layer }
                .thenBy { it.row }
                .thenBy { it.col }
        )

    validWords.entries.shuffled().forEach { entry ->
        val word = entry.key
        val meaning = entry.value
        val usedTileIds = mutableListOf<Int>()

        word.forEach { char ->
            val letter = char.toString()

            val tile = availableTiles.firstOrNull {
                it.letter == letter && it.id !in usedTileIds
            }

            if (tile != null) {
                usedTileIds.add(tile.id)
            }
        }

        if (usedTileIds.size == word.length) {
            return HintResult(
                word = word,
                meaning = meaning,
                tileIds = usedTileIds
            )
        }
    }

    return null
}

fun reshuffleRemainingTiles(
    tiles: List<LetterTile>
): List<LetterTile> {
    val remainingTiles = tiles.filter { !it.isRemoved }
    val removedTiles = tiles.filter { it.isRemoved }

    val shuffledLetters = remainingTiles
        .map { it.letter }
        .shuffled()

    val shuffledRemainingTiles = remainingTiles.mapIndexed { index, tile ->
        tile.copy(
            letter = shuffledLetters[index],
            isSelected = false
        )
    }

    return (removedTiles + shuffledRemainingTiles).sortedBy { it.id }
}

fun reshuffleUntilPlayable(
    tiles: List<LetterTile>
): List<LetterTile> {
    var newTiles = tiles.map {
        it.copy(isSelected = false)
    }

    val remainingCount = newTiles.count { !it.isRemoved }

    if (remainingCount <= 2) {
        return newTiles
    }

    repeat(300) {
        newTiles = reshuffleRemainingTiles(newTiles)

        if (canFormAnyValidWordFromAvailableTiles(newTiles)) {
            return newTiles
        }
    }

    return newTiles
}

fun calculateWordScore(word: String): Int {
    return when (word.length) {
        3 -> 30
        4 -> 50
        5 -> 100
        else -> 150
    }
}

fun getLetterColor(letter: String): Color {
    return when (letter.uppercase()) {
        "A" -> Color(0xFFFFCDD2)
        "B" -> Color(0xFFF8BBD0)
        "C" -> Color(0xFFE1BEE7)
        "D" -> Color(0xFFD1C4E9)
        "E" -> Color(0xFFC5CAE9)
        "F" -> Color(0xFFBBDEFB)
        "G" -> Color(0xFFB3E5FC)
        "H" -> Color(0xFFB2EBF2)
        "I" -> Color(0xFFB2DFDB)
        "J" -> Color(0xFFC8E6C9)
        "K" -> Color(0xFFDCEDC8)
        "L" -> Color(0xFFF0F4C3)
        "M" -> Color(0xFFFFF9C4)
        "N" -> Color(0xFFFFECB3)
        "O" -> Color(0xFFFFE0B2)
        "P" -> Color(0xFFFFCCBC)
        "Q" -> Color(0xFFD7CCC8)
        "R" -> Color(0xFFCFD8DC)
        "S" -> Color(0xFFFFF59D)
        "T" -> Color(0xFFA5D6A7)
        "U" -> Color(0xFF80CBC4)
        "V" -> Color(0xFF81D4FA)
        "W" -> Color(0xFF90CAF9)
        "X" -> Color(0xFF9FA8DA)
        "Y" -> Color(0xFFCE93D8)
        "Z" -> Color(0xFFEF9A9A)
        else -> Color.White
    }
}