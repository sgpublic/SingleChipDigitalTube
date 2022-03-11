// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package io.github.sgpublic

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.sgpublic.window.App

/**
 * 程序入口
 */
fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "LED 数码管生成器 by 忆丶距 V1.1.0",
            resizable = false,
            state = rememberWindowState(
                size = DpSize(860.dp, 470.dp),
                // 设置初始位置为屏幕正中
                position = WindowPosition.Aligned(Alignment.Center)
            )
        ) {
            MaterialTheme {
                App.Compose()
            }
        }
    }
}
