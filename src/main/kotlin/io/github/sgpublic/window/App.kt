package io.github.sgpublic.window

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.sgpublic.compose.DropDownList
import io.github.sgpublic.compose.Path
import io.github.sgpublic.compose.openInBrowser
import io.github.sgpublic.core.ClipboardUtil
import io.github.sgpublic.core.code.*

/**
 * 主界面
 */
class App {
    /**
     * 八段数码管控制
     */
    private val lighting = mutableStateListOf(
        false, false, false, false, false, false, false, false,
    )

    /**
     * 对应关系设置下拉菜单状态
     */
    private val dropdown = mutableStateMapOf(
        'a' to false, 'b' to false, 'c' to false, 'd' to false,
        'e' to false, 'f' to false, 'g' to false, 'h' to false,
    )

    /**
     * 共阴/共阳
     */
    private val commonList = listOf("共阳", "共阴")

    /**
     * 共阳/共阳设置状态，true 为共阳，false 为共阴
     */
    private val common = mutableStateOf(true)

    /**
     * 共阳/共阳设置下拉菜单状态
     */
    private val commonOpen = mutableStateOf(false)

    /**
     * 获取共阴/共阳标题
     */
    private fun getCommon(): String {
        return if (common.value) commonList[0] else commonList[1]
    }

    /**
     * 根据共阴/共阳标题设置状态，并刷新代码框
     */
    private fun setCommon(str: String) {
        common.value = str == commonList[0]
        buildCode()
    }

    /**
     * 代码构建格式
     */
    private val singleChipList = mapOf(
        "A51" to A51Builder(),
        "C51" to C51Builder(),
        "PIC" to PICBuilder(),
        "EMC" to EMCBuilder(),
        "数组" to ArrayBuilder()
    )

    /**
     * 代码构建格式设置
     */
    private val singleChip = mutableStateOf(singleChipList.keys.toList()[0])

    /**
     * 代码构建格式下拉菜单状态
     */
    private val singleChipOpen = mutableStateOf(false)

    /**
     * 设置代码构建格式
     */
    private fun setSingleChip(str: String) {
        singleChip.value = str
        buildCode()
    }

    /**
     * 非数字字符支持
     */
    private val dataList = listOf(
        'A', 'b', 'C', 'd', 'E', 'F', 'H',
        'G', 'g', 'c', 'J', 'n', 'N', 'o',
        'P', 'q', 'r', 't', 'U', '-', '_'
    )

    /**
     * 代码框文本
     */
    private val out = mutableStateOf("")

    /**
     * 添加到编译列表的字符
     */
    private val outList = mutableStateListOf<Char>()

    /**
     * 交换编译列表中字符的位置
     * @param from 待移动的字符索引
     * @param to 目标位置索引
     */
    private fun move(from: Int, to: Int) {
        val tmp = outList[from]
        outList[from] = outList[to]
        outList[to] = tmp
        buildCode()
    }

    /**
     * 将目标字符与其上一个位置的字符交换
     * @param index 待移动的字符索引
     */
    private fun moveUp(index: Int) {
        if (index > 0) move(index, index - 1)
    }

    /**
     * 将目标字符与其下一个位置的字符交换
     * @param index 待移动的字符索引
     */
    private fun moveDown(index: Int) {
        if (index < outList.size - 1) move(index, index + 1)
    }

    /**
     * 添加菜单状态
     */
    private val addPanel = mutableStateOf(false)

    /**
     * 将字符添加到编译列表
     * @param char 待添加的字符
     */
    private fun addToList(char: Char) {
        outList.add(char)
        light(char)
        buildCode()
    }

    /**
     * 控制八段数码管
     */
    private fun light(char: Char) {
        val code = CodeBuilder.getCode(char)
        var tmp = 1
        for (i in 0 until 8) {
            lighting[i] = tmp and code == 0
            tmp = tmp shl 1
        }
    }

    /**
     * 输出编译代码
     */
    private fun buildCode() {
        val out = StringBuilder()
        val builder = singleChipList[singleChip.value] ?: return
        for (i in outList) {
            out.append(builder.postBuild(i, common.value, segment))
        }
        this.out.value = out.toString()
    }

    /**
     * 获取单独一段的亮灭状态
     */
    private fun getColor(char: Char): Color {
        return if (lighting[char - 'a']) Color.Red else Color.White
    }

    /**
     * 段和位的对应关系设置
     */
    private val segment = mutableStateMapOf(
        'a' to 0, 'b' to 1, 'c' to 2, 'd' to 3,
        'e' to 4, 'f' to 5, 'g' to 6, 'h' to 7,
    )

    /**
     * 修改段和位的对应关系
     * @param char 需设置的段
     * @param int 位
     */
    private fun setSegment(char: Char, int: Int) {
        val key = segment.entries.find {
            return@find it.value == int
        }?.key ?: return
        val tmp = segment[char] ?: return
        segment[char] = int
        segment[key] = tmp
        buildCode()
    }

    companion object {
        /**
         * 数码管中横画
         */
        private val horizontal = Path().apply {
            lineTo(14.dp.value, 0.dp.value)
            lineTo(0.dp.value, 14.dp.value)
            lineTo(12.dp.value, 28.dp.value)
            lineTo(114.dp.value, 28.dp.value)
            lineTo(128.dp.value, 14.dp.value)
            lineTo(116.dp.value, 0.dp.value)
            lineTo(14.dp.value, 0.dp.value)
        }

        /**
         * 数码管中竖画
         */
        private val vertical = Path().apply {
            lineTo(44.dp.value, 14.dp.value)
            lineTo(32.dp.value, 0.dp.value)
            lineTo(18.dp.value, 14.dp.value)
            lineTo(0.dp.value, 100.dp.value)
            lineTo(12.dp.value, 114.dp.value)
            lineTo(26.dp.value, 100.dp.value)
            lineTo(44.dp.value, 14.dp.value)
        }

        /**
         * App::class.java 构建界面
         */
        @OptIn(ExperimentalMaterialApi::class)
        @Composable
        @Preview
        @Suppress("LocalVariableName", "FunctionName")
        fun Compose() {
            val ViewModel by remember { mutableStateOf(App()) }
            MaterialTheme {
                Row(
                    modifier = Modifier.padding(30.dp, 15.dp)
                        .fillMaxSize()
                ) {
                    Column {
                        Row(
                            modifier = Modifier.height(328.dp)
                        ) {
                            // 数码管
                            Card(
                                modifier = Modifier.width(236.dp)
                                    .fillMaxHeight(),
                                backgroundColor = Color.Gray,
                                elevation = 4.dp,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Canvas(
                                    modifier = Modifier.fillMaxSize()
                                        .padding(0.dp, 20.dp)
                                ) {
                                    // a
                                    drawPath(
                                        path = Path(horizontal, Offset(74.dp.value, 10.dp.value)),
                                        color = ViewModel.getColor('a'),
                                    )
                                    // c
                                    drawPath(
                                        path = Path(vertical, Offset(172.dp.value, 26.dp.value)),
                                        color = ViewModel.getColor('b'),
                                    )
                                    // b
                                    drawPath(
                                        path = Path(vertical, Offset(151.dp.value, 142.dp.value)),
                                        color = ViewModel.getColor('c'),
                                    )
                                    // d
                                    drawPath(
                                        path = Path(horizontal, Offset(33.dp.value, 245.dp.value)),
                                        color = ViewModel.getColor('d'),
                                    )
                                    // e
                                    drawPath(
                                        path = Path(vertical, Offset(19.dp.value, 142.dp.value)),
                                        color = ViewModel.getColor('e'),
                                    )
                                    // f
                                    drawPath(
                                        path = Path(vertical, Offset(40.dp.value, 26.dp.value)),
                                        color = ViewModel.getColor('f'),
                                    )
                                    // g
                                    drawPath(
                                        path = Path(horizontal, Offset(54.dp.value, 127.dp.value)),
                                        color = ViewModel.getColor('g'),
                                    )
                                    // dp
                                    drawCircle(
                                        radius = 12.dp.value,
                                        center = Offset(200.dp.value, 260.dp.value),
                                        color = ViewModel.getColor('h'),
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier.padding(start = 30.dp)
                                    .wrapContentWidth()
                                    .fillMaxHeight()
                            ) {
                                Text(
                                    text = "对应关系",
                                    color = Color.Blue,
                                    modifier = Modifier.padding(0.dp, 10.dp)
                                )
                                // 对应关系设置
                                for (i in 'a' .. 'h') {
                                    DropDownList(
                                        title = i.toString(),
                                        selected = ViewModel.segment[i]!!,
                                        request = { ViewModel.dropdown[i] = it },
                                        expanded = ViewModel.dropdown[i] == true
                                    ) {
                                        ViewModel.setSegment(i, it)
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.padding(top = 26.dp, bottom = 5.dp)
                        ) {
                            // 设置共阴/共阳
                            DropDownList(
                                title = "共阴/共阳：",
                                selected = ViewModel.getCommon(),
                                expanded = ViewModel.commonOpen.value,
                                request = { ViewModel.commonOpen.value = it },
                                list = ViewModel.commonList,
                            ) {
                                ViewModel.setCommon(it)
                            }
                            // 设置输出类型
                            DropDownList(
                                title = "输出类型：",
                                selected = ViewModel.singleChip.value,
                                expanded = ViewModel.singleChipOpen.value,
                                request = { ViewModel.singleChipOpen.value = it },
                                list = ViewModel.singleChipList.keys.toList(),
                                modifier = Modifier.padding(start = 10.dp)
                            ) {
                                ViewModel.setSingleChip(it)
                            }
                        }
                    }
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Card(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.size(180.dp, 340.dp)
                            ) {
                                // 编译列表
                                LazyColumn {
                                    for (i in 0 until ViewModel.outList.size) {
                                        item {
                                            Row(
                                                modifier = Modifier.clickable {
                                                    ViewModel.light(ViewModel.outList[i])
                                                }.fillParentMaxWidth()
                                                    .padding(horizontal = 20.dp)
                                                    .height(44.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                                            ) {
                                                val char = ViewModel.outList[i]
                                                Text(
                                                    text = if (char == ' ') "空" else char.toString(),
                                                    modifier = Modifier.weight(1f, true)
                                                )
                                                Image(
                                                    painter = painterResource("ic_up.png"),
                                                    contentDescription = "上移",
                                                    alpha = if (i > 0) 1f else 0.3f,
                                                    modifier = Modifier.size(22.dp).let {
                                                        if (i > 0) {
                                                            return@let it.clickable {
                                                                ViewModel.moveUp(i)
                                                            }
                                                        } else return@let it
                                                    }
                                                )
                                                Image(
                                                    painter = painterResource("ic_down.png"),
                                                    contentDescription = "下移",
                                                    alpha = if (i < ViewModel.outList.size - 1)
                                                        1f else 0.3f,
                                                    modifier = Modifier.size(22.dp).let {
                                                        if (i < ViewModel.outList.size - 1) {
                                                            return@let it.clickable {
                                                                ViewModel.moveDown(i)
                                                            }
                                                        } else return@let it
                                                    }
                                                )
                                                Image(
                                                    painter = painterResource("ic_delete.png"),
                                                    contentDescription = "删除",
                                                    modifier = Modifier.size(22.dp).clickable {
                                                        ViewModel.outList.removeAt(i)
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            // 输出代码
                            OutlinedTextField(
                                value = ViewModel.out.value,
                                onValueChange = { },
                                readOnly = true,
                                modifier = Modifier.height(340.dp)
                            )
                        }
                        Row(
                            modifier = Modifier.padding(vertical = 14.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // 清空编译列表
                            Button(
                                onClick = { ViewModel.outList.clear() },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.width(90.dp)
                            ) {
                                Text("清空")
                            }
                            Column {
                                // 添加字符到编译列表
                                Button(
                                    onClick = { ViewModel.addPanel.value = true },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.width(90.dp)
                                ) {
                                    Text("添加")
                                }
                                DropdownMenu(
                                    expanded = ViewModel.addPanel.value,
                                    onDismissRequest = { ViewModel.addPanel.value = false }
                                ) {
                                    // 一键添加所有数字
                                    DropdownMenuItem(
                                        onClick = {
                                            ViewModel.addPanel.value = false
                                            for (i in 0 .. 9) ViewModel.addToList('0' + i)
                                        }
                                    ) {
                                        Text("数字")
                                    }
                                    // 单独添加数字
                                    for (i in '0' .. '9') {
                                        DropdownMenuItem(
                                            onClick = {
                                                ViewModel.addPanel.value = false
                                                ViewModel.addToList(i)
                                            }
                                        ) {
                                            Text(i.toString())
                                        }
                                    }
                                    // 一键添加所有非数字字符
                                    DropdownMenuItem(
                                        onClick = {
                                            ViewModel.addPanel.value = false
                                            for (i in ViewModel.dataList) ViewModel.addToList(i)
                                        }
                                    ) {
                                        Text("字母")
                                    }
                                    // 单独添加非数字字符
                                    for (i in ViewModel.dataList) {
                                        DropdownMenuItem(
                                            onClick = {
                                                ViewModel.addPanel.value = false
                                                ViewModel.addToList(i)
                                            }
                                        ) {
                                            Text(i.toString())
                                        }
                                    }
                                    // 添加空字符
                                    DropdownMenuItem(
                                        onClick = {
                                            ViewModel.addPanel.value = false
                                            ViewModel.addToList(' ')
                                        }
                                    ) {
                                        Text("空")
                                    }
                                }
                            }
                            // 复制代码
                            Button(
                                onClick = { ClipboardUtil.set(ViewModel.out.value) },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.width(90.dp)
                            ) {
                                Text("复制")
                            }
                            // 跳转关于界面
                            Button(
                                onClick = { openInBrowser("https://github.com/sgpublic/SingleChipDigitalTube_JetpackCompose") },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.width(90.dp)
                            ) {
                                Text("关于")
                            }
                        }
                    }
                }
            }
        }
    }
}
