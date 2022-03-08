package io.github.sgpublic.compose

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation

/**
 * 在已有 path 路径上新建 path，并在其基础上偏移
 * @param path 已有 path
 * @param offset 偏移量
 * @see Path
 */
fun Path(path: Path, offset: Offset = Offset.Zero): Path {
    return Path().apply {
        addPath(path, offset)
    }
}