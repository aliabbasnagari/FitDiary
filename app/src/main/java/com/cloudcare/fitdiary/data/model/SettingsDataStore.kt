package com.cloudcare.fitdiary.data.model

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {
    companion object {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val CHART_MODE = stringPreferencesKey("chart_mode")
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        Log.d("SET", mode.name)
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE] = mode.name
        }
    }

    val getThemeMode: Flow<ThemeMode> = context.dataStore.data
        .map { preferences ->
            when (preferences[THEME_MODE]) {
                ThemeMode.LIGHT.name -> ThemeMode.LIGHT
                ThemeMode.DARK.name -> ThemeMode.DARK
                else -> ThemeMode.SYSTEM_DEFAULT
            }
        }


    suspend fun setChartMode(mode: ChartMode) {
        context.dataStore.edit { preferences ->
            preferences[CHART_MODE] = mode.name
        }
    }

    val getChartMode: Flow<ChartMode> = context.dataStore.data
        .map { preferences ->
            when (preferences[CHART_MODE]) {
                ChartMode.HORIZONTAL.name -> ChartMode.HORIZONTAL
                ChartMode.VERTICAL.name -> ChartMode.VERTICAL
                else -> ChartMode.HORIZONTAL
            }
        }
}