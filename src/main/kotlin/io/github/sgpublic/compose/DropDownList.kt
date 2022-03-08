package io.github.sgpublic.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 附带标题的，仅包含数字选项 0 ~ 7 的下拉菜单
 * @param title 标题
 * @param selected 当前选中的数字
 * @param expanded 是否展开
 * @param request 回调菜单展开状态
 * @param onSelect 菜单选中事件
 */
@Composable
fun DropDownList(
    title: String,
    selected: Int,
    expanded: Boolean = false,
    request: (Boolean) -> Unit,
    onSelect: (Int) -> Unit = { }
) {
    Row(
        modifier = Modifier.wrapContentHeight()
            .width(120.dp).padding(0.dp, 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.width(24.dp)
        )
        Box(
            modifier = Modifier.wrapContentSize()
        ) {
            Column {
                Card(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(80.dp, 28.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp, 0.dp)
                    ) {
                        Text(
                            text = "$selected",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { request(false) },
                    modifier = Modifier.width(140.dp)
                ) {
                    for (i in 0 until 8) {
                        DropdownMenuItem(
                            onClick = {
                                request(false)
                                onSelect(i)
                            }
                        ) {
                            Text("$i")
                        }
                    }
                }
            }
            Spacer(
                modifier = Modifier.matchParentSize()
                    .background(Color.Transparent)
                    .clickable { request(true) }
            )
        }
    }
}

/**
 * 附带标题的，自定义文本选项下拉菜单
 * @param title 标题
 * @param selected 当前选中的文本
 * @param expanded 是否展开
 * @param request 回调菜单展开状态
 * @param list 选项
 * @param modifier 布局选项
 * @param onSelect 菜单选中事件
 */
@Composable
fun DropDownList(
    title: String,
    selected: String,
    expanded: Boolean = false,
    request: (Boolean) -> Unit,
    list: List<String> = listOf(),
    modifier: Modifier = Modifier,
    onSelect: (String) -> Unit = { }
) {
    Row(
        modifier = modifier.wrapContentSize()
            .padding(0.dp, 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.width(100.dp)
        )
        Box(
            modifier = Modifier.wrapContentSize()
        ) {
            Column {
                Card(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(80.dp, 28.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp, 0.dp)
                    ) {
                        Text(
                            text = selected,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { request(false) }
                ) {
                    for (i in list) {
                        DropdownMenuItem(
                            onClick = {
                                request(false)
                                onSelect(i)
                            }
                        ) {
                            Text(i)
                        }
                    }
                }
            }
            Spacer(
                modifier = Modifier.matchParentSize()
                    .background(Color.Transparent)
                    .clickable { request(true) }
            )
        }
    }
}