package com.evaniewares.classease.tests

import androidx.compose.ui.graphics.Color
import com.evaniewares.classease.domain.model.PieChartItem
import com.evaniewares.classease.ui.theme.AppColor
import com.evaniewares.classease.ui.theme.Purple40

val piList = listOf(
    PieChartItem(
        Color.Red,
        19,
        "Haha",
        false
    ),
    PieChartItem(
        Purple40,
        45,
        "Haha",
        false
    ),
    PieChartItem(
        AppColor,
        38,
        "Haha",
        false
    )

)