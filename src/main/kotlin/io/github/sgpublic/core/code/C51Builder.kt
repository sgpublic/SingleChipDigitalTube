package io.github.sgpublic.core.code

class C51Builder: CodeBuilder() {
    override fun onBuild(char: Char, code: String): String {
        return "    0x${code}, //$char\n"
    }
}