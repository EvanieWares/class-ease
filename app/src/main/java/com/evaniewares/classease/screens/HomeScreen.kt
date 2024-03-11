package com.evaniewares.classease.screens

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.evaniewares.classease.R
import com.evaniewares.classease.domain.model.PieChartItem
import com.evaniewares.classease.domain.model.StudentEntity
import com.evaniewares.classease.navigation.HomeScreenRoutes
import com.evaniewares.classease.tests.studentListTest
import com.evaniewares.classease.ui.theme.AppColor
import com.evaniewares.classease.ui.theme.Blue
import com.evaniewares.classease.ui.theme.ClassEaseTheme
import com.evaniewares.classease.ui.theme.DangerColor
import com.evaniewares.classease.ui.theme.Grey
import com.evaniewares.classease.ui.theme.Pink40
import com.evaniewares.classease.ui.theme.Purple40
import com.evaniewares.classease.ui.theme.White
import com.evaniewares.classease.ui.theme.Yellow
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt

/**
 * App's home screen.
 *
 * @param navController the [NavHostController] that will help to go
 * back to the previous page.
 */
@Composable
fun HomeScreen(
    navController: NavHostController,
    studentList: List<StudentEntity>
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(scrollState)
        ) {
            ScrollableTopBar(
                onSettingsClick = {
                    navController.navigate(HomeScreenRoutes.Settings.route)
                },
                onAccountClick = {
                    navController.navigate(HomeScreenRoutes.Account.route)
                },
                onNotificationClick = {
                    navController.navigate(HomeScreenRoutes.Notifications.route)
                }
            )
            Enrollment(studentList = studentList)
            Spacer(modifier = Modifier.padding(10.dp))
            OverallPerformance(studentList = studentList)
        }
    }
}

@Composable
private fun OverallPerformance(
    studentList: List<StudentEntity>
) {
    val artsPassed = studentList.filter { it.arts > 39 }.size
    val chichewaPassed = studentList.filter { it.chichewa > 39 }.size
    val englishPassed = studentList.filter { it.english > 39 }.size
    val mathsPassed = studentList.filter { it.maths > 39 }.size
    val sciencePassed = studentList.filter { it.science > 39 }.size
    val socialPassed = studentList.filter { it.social > 39 }.size

    val pieChartItems = listOf(
        PieChartItem(AppColor, chichewaPassed, "Chichewa", false),
        PieChartItem(Purple40, artsPassed, "Arts", false),
        PieChartItem(DangerColor, englishPassed, "English", false),
        PieChartItem(Yellow, sciencePassed, "Science", false),
        PieChartItem(Blue, socialPassed, "Social", false),
        PieChartItem(Pink40, mathsPassed, "Maths", false)

    )


    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.overall_performance),
                style = MaterialTheme.typography.titleMedium
            )
            HorizontalDivider()
            PieChart(
                input = pieChartItems,
                modifier = Modifier.size(350.dp),
                centerText = stringResource(R.string.overall_performance),
                radius = 350f,
                innerRadius = 175f
            )
        }
    }
}

/**
 * Displays a PieChart
 */
@Composable
private fun PieChart(
    input: List<PieChartItem>,
    modifier: Modifier = Modifier,
    radius: Float = 500f,
    innerRadius: Float = 250f,
    transparentWidth: Float = 70f,
    centerText: String = ""
) {
    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }
    var inputList by remember {
        mutableStateOf(input)
    }
    var isCenterTapped by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(key1 = true) {
                    detectTapGestures(
                        onTap = { offset ->
                            val tapAngleInDegrees = (-atan2(
                                x = circleCenter.y - offset.y,
                                y = circleCenter.x - offset.x
                            ) * (180f / PI).toFloat() - 90f).mod(360f)
                            val centerClicked = if (tapAngleInDegrees < 90) {
                                offset.x < circleCenter.x + innerRadius && offset.y < circleCenter.y + innerRadius
                            } else if (tapAngleInDegrees < 180) {
                                offset.x > circleCenter.x - innerRadius && offset.y < circleCenter.y + innerRadius
                            } else if (tapAngleInDegrees < 270) {
                                offset.x > circleCenter.x - innerRadius && offset.y > circleCenter.y - innerRadius
                            } else {
                                offset.x < circleCenter.x + innerRadius && offset.y > circleCenter.y - innerRadius
                            }

                            if (centerClicked) {
                                inputList = inputList.map {
                                    it.copy(isTapped = !isCenterTapped)
                                }
                                isCenterTapped = !isCenterTapped
                            } else {
                                val anglePerValue = 360 / input.sumOf {
                                    it.value
                                }
                                var currAngle = 0f
                                inputList.forEach { pieChartItem ->
                                    currAngle += pieChartItem.value * anglePerValue
                                    if (tapAngleInDegrees < currAngle) {
                                        val description = pieChartItem.description
                                        inputList = inputList.map {
                                            if (description == it.description) {
                                                it.copy(isTapped = !it.isTapped)
                                            } else {
                                                it.copy(isTapped = false)
                                            }
                                        }
                                        return@detectTapGestures
                                    }
                                }
                            }
                        }
                    )
                },
        ) {
            val width = size.width
            val height = size.height
            val totalValue = input.sumOf { it.value }
            val anglePerValue = 360f / totalValue
            var currentStartAngle = 0f
            circleCenter = Offset(x = width / 2f, y = height / 2f)

            inputList.forEach { pieChartItem ->
                val scale = if (pieChartItem.isTapped) 1.1f else 1.0f
                val angleToDraw = pieChartItem.value * anglePerValue
                scale(scale) {
                    drawArc(
                        color = pieChartItem.color,
                        startAngle = currentStartAngle,
                        sweepAngle = angleToDraw,
                        useCenter = true,
                        size = Size(
                            width = radius * 2f,
                            height = radius * 2f
                        ),
                        topLeft = Offset(
                            (width - radius * 2f) / 2f,
                            (height - radius * 2f) / 2f
                        )
                    )
                    currentStartAngle += angleToDraw
                }
                var rotateAngle = currentStartAngle - angleToDraw / 2f - 90f
                var factor = 1f
                if (rotateAngle > 90f) {
                    rotateAngle = (rotateAngle + 180).mod(360f)
                    factor = -0.92f
                }

                val percentage = (pieChartItem.value / totalValue.toFloat() * 100).roundToInt()

                drawContext.canvas.nativeCanvas.apply {
                    if (percentage > 3) {
                        rotate(rotateAngle) {
                            drawText(
                                "$percentage%",
                                circleCenter.x,
                                circleCenter.y + (radius - (radius - innerRadius) / 2f) * factor,
                                Paint().apply {
                                    textSize = 12.sp.toPx()
                                    textAlign = Paint.Align.CENTER
                                    color = White.toArgb()
                                }
                            )
                        }
                    }
                }
                if (pieChartItem.isTapped) {
                    val tabRotation = currentStartAngle - angleToDraw - 90f
                    rotate(tabRotation) {
                        drawRoundRect(
                            topLeft = circleCenter,
                            size = Size(width = 12f, height = radius * 1.2f),
                            color = Grey,
                            cornerRadius = CornerRadius(15f, 15f)
                        )
                    }
                    rotate(tabRotation + angleToDraw) {
                        drawRoundRect(
                            topLeft = circleCenter,
                            size = Size(width = 12f, height = radius * 1.2f),
                            color = Grey,
                            cornerRadius = CornerRadius(15f, 15f)
                        )
                    }
                    rotate(rotateAngle) {
                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                "${pieChartItem.description}: ${pieChartItem.value}",
                                circleCenter.x,
                                circleCenter.y + radius * 1.3f * factor,
                                Paint().apply {
                                    textSize = 16.sp.toPx()
                                    textAlign = Paint.Align.CENTER
                                    color = pieChartItem.color.toArgb()
                                    isFakeBoldText = true
                                }
                            )
                        }
                    }
                }
            }

            if (inputList.first().isTapped) {
                rotate(-90f) {
                    drawRoundRect(
                        topLeft = circleCenter,
                        size = Size(width = 12f, height = radius * 1.2f),
                        color = Grey,
                        cornerRadius = CornerRadius(15f, 15f)
                    )
                }
            }

            drawContext.canvas.nativeCanvas.apply {
                drawCircle(
                    circleCenter.x,
                    circleCenter.y,
                    innerRadius,
                    Paint().apply {
                        color = White.copy(alpha = 0.6f).toArgb()
                        setShadowLayer(10f, 0f, 0f, Grey.toArgb())
                    }
                )
            }

            drawCircle(
                color = White.copy(0.2f),
                radius = innerRadius + transparentWidth / 2f
            )
        }
        Text(
            text = centerText,
            modifier = Modifier
                .width(Dp(innerRadius / 1.5f))
                .padding(5.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Displays the class enrollment
 *
 * @param studentList a list of all students
 */
@Composable
fun Enrollment(
    studentList: List<StudentEntity>
) {
    val boysList = studentList.filter { it.gender == "M" }
    val girlsList = studentList.filter { it.gender == "F" }
    val otherList = studentList.filter { it.gender == "O" }
    val textStyle = MaterialTheme.typography.bodyMedium

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enrollment",
                style = MaterialTheme.typography.titleMedium
            )
            HorizontalDivider()
            Row(
                modifier = Modifier.padding(5.dp),
                horizontalArrangement = Arrangement.spacedBy(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Boys: ${boysList.size}",
                    style = textStyle
                )
                Text(
                    text = "Girls: ${girlsList.size}",
                    style = textStyle
                )
                Text(
                    text = "Other: ${otherList.size}",
                    style = textStyle
                )
            }
        }
    }
}

/**
 * Simple top app bar with icons that navigates to [NotificationScreen], [SettingScreen]
 * and [AccountScreen] respectively
 *
 * @param onSettingsClick handles user click actions on the [Settings] icon
 * @param onAccountClick handles user click actions on the [AccountCircle] icon
 * @param onNotificationClick handles user click actions on the [Notifications] icon
 */
@Composable
private fun ScrollableTopBar(
    onSettingsClick: () -> Unit,
    onAccountClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    val iconSize = 24.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier.size(iconSize)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications"
                )
            }
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier.size(iconSize)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings"
                )
            }
            IconButton(
                onClick = onAccountClick,
                modifier = Modifier.size(iconSize)
            ) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "Account"
                )
            }
        }
    }
}

@PreviewFontScale
@PreviewScreenSizes
@Preview(
    showSystemUi = true
)
@Composable
fun HomePreview() {
    ClassEaseTheme(darkTheme = false) {
        HomeScreen(rememberNavController(), studentListTest)
    }
}
