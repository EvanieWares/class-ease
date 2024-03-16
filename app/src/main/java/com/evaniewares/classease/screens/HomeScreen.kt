package com.evaniewares.classease.screens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.evaniewares.classease.R
import com.evaniewares.classease.domain.model.ArcData
import com.evaniewares.classease.domain.model.PieData
import com.evaniewares.classease.domain.model.StudentEntity
import com.evaniewares.classease.domain.model.SubjectPieData
import com.evaniewares.classease.navigation.HomeScreenRoutes
import com.evaniewares.classease.tests.studentListTest
import com.evaniewares.classease.ui.theme.Blue300
import com.evaniewares.classease.ui.theme.ClassEaseTheme
import com.evaniewares.classease.ui.theme.Cyan100
import com.evaniewares.classease.ui.theme.Cyan200
import com.evaniewares.classease.ui.theme.Cyan300
import com.evaniewares.classease.ui.theme.Cyan400
import com.evaniewares.classease.ui.theme.Cyan500
import com.evaniewares.classease.ui.theme.Green100
import com.evaniewares.classease.ui.theme.Green200
import com.evaniewares.classease.ui.theme.Green300
import com.evaniewares.classease.ui.theme.Green400
import com.evaniewares.classease.ui.theme.Green500
import com.evaniewares.classease.ui.theme.Orange100
import com.evaniewares.classease.ui.theme.Orange200
import com.evaniewares.classease.ui.theme.Orange300
import com.evaniewares.classease.ui.theme.Orange400
import com.evaniewares.classease.ui.theme.Orange500
import com.evaniewares.classease.ui.theme.Purple100
import com.evaniewares.classease.ui.theme.Purple200
import com.evaniewares.classease.ui.theme.Purple300
import com.evaniewares.classease.ui.theme.Purple400
import com.evaniewares.classease.ui.theme.Purple500
import com.evaniewares.classease.ui.theme.Red100
import com.evaniewares.classease.ui.theme.Red200
import com.evaniewares.classease.ui.theme.Red300
import com.evaniewares.classease.ui.theme.Red400
import com.evaniewares.classease.ui.theme.Red500
import com.evaniewares.classease.utils.SubjectType

/**
 * App's home screen.
 *
 * @param navController the [NavHostController] that will help to go
 * back to the previous page.
 * @param studentList the list of all students
 */
@Composable
fun HomeScreen(
    navController: NavHostController,
    studentList: List<StudentEntity>
) {
    val scrollState = rememberScrollState()

    val subjectPieDataList = remember {
        mutableListOf(
            SubjectPieData("English", getOrangeColorFamily(), SubjectType.ENGLISH),
            SubjectPieData("Chichewa", getGreenColorFamily(), SubjectType.CHICHEWA),
            SubjectPieData("Mathematics", getRedColorFamily(), SubjectType.MATHEMATICS),
            SubjectPieData("Primary Science", getCyanColorFamily(), SubjectType.SCIENCE),
            SubjectPieData("Social & BK/RE", getPurpleColorFamily(), SubjectType.SOCIAL),
            SubjectPieData("Arts & Life Skills", getGreenColorFamily(), SubjectType.ARTS)
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
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
            OverallPerformance(
                studentList = studentList
            )
            subjectPieDataList.forEach { subjectPieData ->
                Performance(
                    studentList = studentList,
                    title = subjectPieData.title,
                    colorFamily = subjectPieData.colorFamily,
                    subjectType = subjectPieData.subjectType
                )
            }
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

    val pieData = listOf(
        PieData(Green300, chichewaPassed, "Chichewa"),
        PieData(Blue300, artsPassed, "Arts"),
        PieData(Orange300, englishPassed, "English"),
        PieData(Cyan300, sciencePassed, "Science"),
        PieData(Purple300, socialPassed, "Social"),
        PieData(Red200, mathsPassed, "Maths")

    )

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.overall_performance),
                style = MaterialTheme.typography.titleMedium
            )
            HorizontalDivider(modifier = Modifier.padding(bottom = 20.dp))
            OverallPieChart(pieDataList = pieData)
            HorizontalDivider()
        }
    }
}

@Composable
private fun Performance(
    studentList: List<StudentEntity>,
    title: String,
    colorFamily: List<Color>,
    subjectType: SubjectType
) {
    val pieDataList = getFiltered(colorFamily, studentList, subjectType)

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(5.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                PieChart(
                    pieDataList = pieDataList,
                    size = 180.dp
                )
                Column {
                    pieDataList.map {
                        LegendRow(pieData = it)
                    }
                }
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            HorizontalDivider()
        }
    }
}

/**
 * Displays a PieChart.
 *
 * @param pieDataList a list of [PieData] items to display on the pie chart
 * @param size the size of the pie chart
 * @param modifier [Modifier]
 */
@Composable
private fun PieChart(
    pieDataList: List<PieData>,
    size: Dp,
    modifier: Modifier = Modifier
) {
    val localModifier = modifier.size(size)
    val total = pieDataList.fold(0f) { acc, pieData ->
        acc + pieData.value
    }.div(360)
    var currentSum = 0
    val arcs = pieDataList.map {
        currentSum += it.value
        ArcData(
            targetSweepAngle = currentSum / total,
            color = it.color
        )
    }

    Canvas(modifier = localModifier) {
        arcs.reversed().map {
            drawArc(
                startAngle = -90f,
                sweepAngle = it.targetSweepAngle,
                color = it.color,
                useCenter = true
            )
        }
    }
}

@Composable
private fun OverallPieChart(
    pieDataList: List<PieData>,
    radiusOuter: Dp = 90.dp,
    chartBarWidth: Dp = 20.dp,
    animDuration: Int = 1000
) {
    val totalSum = pieDataList.sumOf { it.value }

    val arcs = pieDataList.map {
        ArcData(
            targetSweepAngle = 360 * it.value.toFloat() / totalSum.toFloat(),
            color = it.color
        )
    }

    var animationPlayed by remember {
        mutableStateOf(false)
    }

    var lastValue = 0f

    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ),
        label = "Animate size"
    )

    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90 * 11f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ),
        label = "Animate rotation"
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (totalSum != 0) {
            Box(
                modifier = Modifier.size(animateSize.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .size(radiusOuter * 2f)
                        .rotate(animateRotation)
                ) {
                    arcs.map {
                        drawArc(
                            color = it.color,
                            startAngle = lastValue,
                            sweepAngle = it.targetSweepAngle,
                            useCenter = false,
                            style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                        )
                        lastValue += it.targetSweepAngle
                    }
                }
            }
        }
        Box(modifier = Modifier.padding(10.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                pieDataList.map {
                    LegendRow(
                        pieData = it,
                        width = (radiusOuter * 2),
                        boxSize = 24.dp,
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

/**
 * Shows a pie chart legend.
 *
 * @param pieData the [PieData] to display on the legend
 * @param boxSize size of the colored box that display color for specific entry
 * @param width the width of the legend
 * @param textStyle the style of the text
 */
@Composable
private fun LegendRow(
    pieData: PieData,
    boxSize: Dp = 14.dp,
    width: Dp = 110.dp,
    textStyle: TextStyle = MaterialTheme.typography.labelSmall
) {
    Row(
        modifier = Modifier.width(width),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(boxSize)
                        .background(pieData.color)
                )
                Text(
                    text = pieData.description,
                    style = textStyle
                )
            }
        }
        Text(
            text = pieData.value.toString(),
            style = textStyle
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

private fun getFiltered(
    colorFamily: List<Color>,
    studentList: List<StudentEntity>,
    subjectType: SubjectType
): List<PieData> {
    val excellent: Int
    val vGood: Int
    val good: Int
    val pass: Int
    val fail: Int

    when (subjectType) {
        SubjectType.ARTS -> {
            excellent = studentList.filter { it.arts > 79 }.size
            vGood = studentList.filter { it.arts in 66..79 }.size
            good = studentList.filter { it.arts in 56..65 }.size
            pass = studentList.filter { it.arts in 40..55 }.size
            fail = studentList.filter { it.arts < 40 }.size
        }

        SubjectType.CHICHEWA -> {
            excellent = studentList.filter { it.chichewa > 79 }.size
            vGood = studentList.filter { it.chichewa in 66..79 }.size
            good = studentList.filter { it.chichewa in 56..65 }.size
            pass = studentList.filter { it.chichewa in 40..55 }.size
            fail = studentList.filter { it.chichewa < 40 }.size
        }

        SubjectType.ENGLISH -> {
            excellent = studentList.filter { it.english > 79 }.size
            vGood = studentList.filter { it.english in 66..79 }.size
            good = studentList.filter { it.english in 56..65 }.size
            pass = studentList.filter { it.english in 40..55 }.size
            fail = studentList.filter { it.english < 40 }.size
        }

        SubjectType.MATHEMATICS -> {
            excellent = studentList.filter { it.maths > 79 }.size
            vGood = studentList.filter { it.maths in 66..79 }.size
            good = studentList.filter { it.maths in 56..65 }.size
            pass = studentList.filter { it.maths in 40..55 }.size
            fail = studentList.filter { it.maths < 40 }.size
        }

        SubjectType.SCIENCE -> {
            excellent = studentList.filter { it.science > 79 }.size
            vGood = studentList.filter { it.science in 66..79 }.size
            good = studentList.filter { it.science in 56..65 }.size
            pass = studentList.filter { it.science in 40..55 }.size
            fail = studentList.filter { it.science < 40 }.size
        }

        SubjectType.SOCIAL -> {
            excellent = studentList.filter { it.social > 79 }.size
            vGood = studentList.filter { it.social in 66..79 }.size
            good = studentList.filter { it.social in 56..65 }.size
            pass = studentList.filter { it.social in 40..55 }.size
            fail = studentList.filter { it.social < 40 }.size
        }
    }

    return listOf(
        PieData(
            color = colorFamily[4],
            value = excellent,
            description = "Excellent"
        ),
        PieData(
            color = colorFamily[3],
            value = vGood,
            description = "Very good"
        ),
        PieData(
            color = colorFamily[2],
            value = good,
            description = "Good"
        ),
        PieData(
            color = colorFamily[1],
            value = pass,
            description = "Pass"
        ),
        PieData(
            color = colorFamily[0],
            value = fail,
            description = "Fail"
        )
    )
}

private fun getGreenColorFamily(): List<Color> {
    return listOf(
        Green100, Green200, Green300, Green400, Green500
    )
}

private fun getRedColorFamily(): List<Color> {
    return listOf(
        Red100, Red200, Red300, Red400, Red500
    )
}

private fun getCyanColorFamily(): List<Color> {
    return listOf(
        Cyan100, Cyan200, Cyan300, Cyan400, Cyan500
    )
}

private fun getPurpleColorFamily(): List<Color> {
    return listOf(
        Purple100, Purple200, Purple300, Purple400, Purple500
    )
}

private fun getOrangeColorFamily(): List<Color> {
    return listOf(
        Orange100, Orange200, Orange300, Orange400, Orange500
    )
}

@Preview
@Composable
fun HomePreview() {
    ClassEaseTheme(darkTheme = false) {
        Surface {
            HomeScreen(
                navController = rememberNavController(),
                studentList = studentListTest
            )
        }
    }
}
