package io.github.sgpublic.core.code

class A51Builder: CodeBuilder() {
    override fun onBuild(char: Char, code: String): String {
        return "    DB    0${code}H    ;$char\n"
    }
}