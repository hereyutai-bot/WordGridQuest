package com.yutai.wordgridquest

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object RecordStorage {
    private const val PREF_NAME = "word_grid_quest_records"
    private const val KEY_RANKING_RECORDS = "ranking_records"
    private const val KEY_STUDY_RECORDS = "study_records"

    fun saveRankingRecords(
        context: Context,
        records: List<RankingRecord>
    ) {
        val jsonArray = JSONArray()

        records.forEach { record ->
            jsonArray.put(
                JSONObject().apply {
                    put("playerName", record.playerName)
                    put("modeType", record.modeType.name)
                    put("score", record.score)
                    put("detailText", record.detailText)
                    put("playedAtText", record.playedAtText)
                }
            )
        }

        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_RANKING_RECORDS, jsonArray.toString())
            .apply()
    }

    fun loadRankingRecords(
        context: Context
    ): List<RankingRecord> {
        val jsonText = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_RANKING_RECORDS, null)
            ?: return emptyList()

        return runCatching {
            val jsonArray = JSONArray(jsonText)
            val records = mutableListOf<RankingRecord>()

            for (index in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(index)

                val modeType = runCatching {
                    GameModeType.valueOf(item.getString("modeType"))
                }.getOrDefault(GameModeType.LETTER_TILE)

                records.add(
                    RankingRecord(
                        playerName = item.optString("playerName", "玩家"),
                        modeType = modeType,
                        score = item.optInt("score", 0),
                        detailText = item.optString("detailText", ""),
                        playedAtText = item.optString("playedAtText", "")
                    )
                )
            }

            records
        }.getOrDefault(emptyList())
    }

    fun saveStudyRecords(
        context: Context,
        records: List<StudyRecord>
    ) {
        val jsonArray = JSONArray()

        records.forEach { record ->
            jsonArray.put(
                JSONObject().apply {
                    put("modeType", record.modeType.name)
                    put("score", record.score)
                    put("correctCount", record.correctCount)
                    put("wrongCount", record.wrongCount)
                    put("skipCount", record.skipCount)
                    put("playedAtText", record.playedAtText)
                    put("note", record.note)
                }
            )
        }

        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_STUDY_RECORDS, jsonArray.toString())
            .apply()
    }

    fun loadStudyRecords(
        context: Context
    ): List<StudyRecord> {
        val jsonText = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_STUDY_RECORDS, null)
            ?: return emptyList()

        return runCatching {
            val jsonArray = JSONArray(jsonText)
            val records = mutableListOf<StudyRecord>()

            for (index in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(index)

                val modeType = runCatching {
                    GameModeType.valueOf(item.getString("modeType"))
                }.getOrDefault(GameModeType.LETTER_TILE)

                records.add(
                    StudyRecord(
                        modeType = modeType,
                        score = item.optInt("score", 0),
                        correctCount = item.optInt("correctCount", 0),
                        wrongCount = item.optInt("wrongCount", 0),
                        skipCount = item.optInt("skipCount", 0),
                        playedAtText = item.optString("playedAtText", ""),
                        note = item.optString("note", "")
                    )
                )
            }

            records
        }.getOrDefault(emptyList())
    }

    fun currentPlayedAtText(): String {
        val formatter = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
        return formatter.format(Date())
    }
}