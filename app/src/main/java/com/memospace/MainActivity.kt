package com.memospace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.memospace.data.local.MemoDatabase
import com.memospace.data.repository.NoteRepository
import com.memospace.ui.home.HomeViewModel
import com.memospace.ui.navigation.MemoSpaceApp
import com.memospace.ui.theme.MemoSpaceTheme

/**
 * Main activity serving as the single-activity host for Jetpack Compose navigation.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val database = MemoDatabase.getInstance(applicationContext)
        val repository = NoteRepository(database.noteDao())
        val homeViewModel = HomeViewModel(repository)

        setContent {
            val uiState by homeViewModel.uiState.collectAsState()

            MemoSpaceTheme(themeMode = uiState.themeMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MemoSpaceApp(
                        repository = repository,
                        homeViewModel = homeViewModel
                    )
                }
            }
        }
    }
}
