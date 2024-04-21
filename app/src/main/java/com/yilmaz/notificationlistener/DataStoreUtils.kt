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
            preferences[SIZE_KEY] ?: 32
        }

    suspend fun setScreenCornerRadiusSize(theme: Int) {
        context.dataStore.edit { preferences ->
            preferences[SIZE_KEY] = theme
        }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        private val SIZE_KEY = intPreferencesKey("screen_corner_radius_size")
    }

}