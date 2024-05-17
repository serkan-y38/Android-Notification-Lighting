package com.yilmaz.notificationlistener

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreUtils(private val context: Context) {

    fun getScreenCornerRadiusSize(): Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[CORNER_RADIUS_SIZE_KEY] ?: 32
        }

    suspend fun setScreenCornerRadiusSize(theme: Int) {
        context.dataStore.edit { preferences ->
            preferences[CORNER_RADIUS_SIZE_KEY] = theme
        }
    }

    fun getAnimationFrequency(): Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[ANIMATION_FREQUENCY] ?: 1
        }

    suspend fun setAnimationFrequency(repetitionNum: Int) {
        context.dataStore.edit { preferences ->
            preferences[ANIMATION_FREQUENCY] = repetitionNum
        }
    }

    fun getBorderSize(): Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[BORDER_SIZE_KEY] ?: 4
        }

    suspend fun setBorderSize(size: Int) {
        context.dataStore.edit { preferences ->
            preferences[BORDER_SIZE_KEY] = size
        }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        private val CORNER_RADIUS_SIZE_KEY = intPreferencesKey("screen_corner_radius_size")
        private val BORDER_SIZE_KEY = intPreferencesKey("screen_border_size")
        private val ANIMATION_FREQUENCY = intPreferencesKey("animation_frequency")
    }

}