/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.wear.tiles

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberScalingLazyListState
import androidx.wear.tiles.TileService

@Composable
internal fun SampleTilesList(context: Context, sampleTiles: Map<Int, Class<out TileService>>) {
    TilesKotlinTheme {
        val listState = rememberScalingLazyListState()
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {
            item {
                ListHeader {
                    Text(text = context.getString(R.string.app_name))
                }
            }
            sampleTiles.forEach { (tileLabelRes, tileServiceClass) ->
                item(tileLabelRes) {
                    TileSample(context, tileLabelRes, tileServiceClass)
                }
            }
        }
    }
}

@Composable
private fun TileSample(
    context: Context,
    @StringRes tileLabelRes: Int,
    tileServiceClass: Class<out TileService>
) {
    val tileLabel = context.getString(tileLabelRes)
    Chip(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            val intent = Intent(context, TilePreviewActivity::class.java)
            intent.putExtra(TilePreviewActivity.KEY_LABEL, tileLabel)
            intent.putExtra(TilePreviewActivity.KEY_COMPONENT_NAME, tileServiceClass.name)
            context.startActivity(intent)
        },
        label = { Text(tileLabel) }
    )
}