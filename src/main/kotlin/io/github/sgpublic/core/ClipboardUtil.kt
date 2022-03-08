package io.github.sgpublic.core

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

/**
 * 剪切板工具
 */
object ClipboardUtil {
    /**
     * 使用 java.awt.Toolkit 置剪切板
     */
    fun set(string: String) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val trans = StringSelection(string)
        clipboard.setContents(trans, null)
    }
}