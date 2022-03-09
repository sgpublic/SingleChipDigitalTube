package io.github.sgpublic.core.code

import java.util.*

abstract class CodeBuilder {
    /**
     * 提交编译请求
     * @param char 字符
     * @param common 共阴/共阳，true 为共阳，false 为共阴
     * @param segment 段和位之间的对应关系
     * @see CodeBuilder.segment
     */
    fun postBuild(char: Char, common: Boolean, segment: Map<Char, Int> = CodeBuilder.segment): String {
        val code = getCode(char, common)
        var result = 0
        for ((c, int) in segment) {
            val tmp = 1 shl (c - 'a')
            if (code and tmp == 0) {
                result = result or (1 shl int)
            }
        }
        val str = result.toString(16)
            .padStart(2, '0')
            .uppercase(Locale.getDefault())
        return onBuild(char, str)
    }

    /**
     * 输出单个代码
     * @param char 待编译的字符
     * @param code 编译得到的 16 进制
     */
    protected abstract fun onBuild(char: Char, code: String): String

    companion object {
        /**
         * 默认配置下共阳代码
         */
        private val codeSet = mapOf(
            '0' to 0xC0, '1' to 0xF9, '2' to 0xA4, '3' to 0xB0, '4' to 0x99,
            '5' to 0x92, '6' to 0x82, '7' to 0xF8, '8' to 0x80, '9' to 0x90,
            'A' to 0x88, 'b' to 0x83, 'C' to 0xC6, 'd' to 0xA1, 'E' to 0x86,
            'F' to 0x89, 'H' to 0x89, 'G' to 0xC2, 'h' to 0x8B, 'c' to 0xA7,
            'J' to 0xF1, 'L' to 0xC7, 'n' to 0xAB, 'N' to 0xC8, 'o' to 0xA3,
            'P' to 0x8C, 'q' to 0x98, 'r' to 0xAF, 't' to 0x87, 'U' to 0xC1,
            '-' to 0xBF, '_' to 0xF7, ' ' to 0xFF,
        )

        /**
         * 默认段与位的对应关系配置
         */
        val segment = mapOf(
            'a' to 0, 'b' to 1, 'c' to 2, 'd' to 3,
            'e' to 4, 'f' to 5, 'g' to 6, 'h' to 7,
        )

        /**
         * 获取默认配置下代码
         * @param char 字符
         * @param common 共阴/共阳，true 为共阳，false 为共阴
         */
        fun getCode(char: Char, common: Boolean = false): Int {
            val result = codeSet[char] ?: 0xFF
            return if (common) 0xFF xor result else result
        }
    }
}