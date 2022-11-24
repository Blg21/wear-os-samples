@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.dino.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import kotlinx.coroutines.launch

@ExperimentalLifecycleComposeApi
@Composable
fun GameWorld(
    viewModel: GameWorldViewModel = viewModel()
) {
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Box(contentAlignment = Alignment.Center) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged {
                    viewModel.onCanvasResize(it.width, it.height)
                }
                .pointerInput("screen_taps") {
                    detectTapGestures {
                        viewModel.onReceiveJumpInput()
                    }
                }
                .onRotaryScrollEvent {
                    coroutineScope.launch {
                        viewModel.onReceiveJumpInput()
                    }
                    true
                }
                .focusRequester(focusRequester)
                .focusable(true)
        ) {
            val uiStateValue = uiState.value

            if (uiStateValue != null) {
                for (obstacle in uiStateValue.obstacles) {
                    translate(left = obstacle.left, top = obstacle.top) {
                        drawCactus()
                    }
                }

                translate(left = uiStateValue.dino.left, top = uiStateValue.dino.top) {
                    drawDino(uiStateValue.dino.avatarState, uiStateValue.gameWorldTicks)
                }
            }
        }
        if (uiState.value?.isPlaying == false) {
            Chip(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = "play game"
                    )
                },
                modifier = Modifier.padding(16.dp),
                label = { Text("Play") },
                secondaryLabel = uiState.value?.score?.let { score ->
                    { Text("score: ${score}") }
                },
                onClick = { viewModel.onStartPressed() }
            )
        }
    }
}