package io.github.sgpublic.core.code

class PICBuilder: CodeBuilder() {
    override fun onBuild(char: Char, code: String): String {
        return "    RETLW    0X${code}    ;$char\n"
    }
}