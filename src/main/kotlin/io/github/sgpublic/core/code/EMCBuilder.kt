package io.github.sgpublic.core.code

class EMCBuilder: CodeBuilder() {
    override fun onBuild(char: Char, code: String): String {
        return "    RETL    0X${code}    ;$char\n"
    }
}