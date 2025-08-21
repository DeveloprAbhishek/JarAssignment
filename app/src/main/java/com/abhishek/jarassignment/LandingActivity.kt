package com.abhishek.jarassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.abhishek.jarassignment.presentation.ui.theme.JarAssignmentTheme

class LandingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JarAssignmentTheme {}
        }
    }
}

