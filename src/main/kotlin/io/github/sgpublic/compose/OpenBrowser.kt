package io.github.sgpublic.compose

import java.awt.Desktop
import java.net.URI
import java.util.*

/**
 * 调用浏览器打开链接，自适应操作系统
 * @param uri 链接
 */
fun openInBrowser(uri: String) {
    val osName by lazy(LazyThreadSafetyMode.NONE) { System.getProperty("os.name").lowercase(Locale.getDefault()) }
    val desktop = Desktop.getDesktop()
    when {
        Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE) ->
            desktop.browse(URI(uri))
        "mac" in osName -> Runtime.getRuntime().exec("open $uri")
        "nix" in osName || "nux" in osName -> Runtime.getRuntime().exec("xdg-open $uri")
        else -> throw RuntimeException("cannot open $uri")
    }
}