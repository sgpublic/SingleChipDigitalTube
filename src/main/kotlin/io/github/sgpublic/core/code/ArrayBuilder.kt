package io.github.sgpublic.core.code

class ArrayBuilder: CodeBuilder() {
    override fun onBuild(char: Char, code: String): String {
        return "    0x${code}, //$char\n"
    }
}