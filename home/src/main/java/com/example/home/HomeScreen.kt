package com.example.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.n9ne.common.R
import org.n9ne.common.model.Activity


@Preview(showSystemUi = true)
@Composable
fun HomeScreen() {

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        TargetCard()
        Progress()
        val activities: List<Activity> =
            mutableListOf(
                Activity(null, null, "12", "2024/11/20", ""),
                Activity(null, null, "4", "2024/11/22", "")

            )

        Activities(activities)
    }
}


@Composable
fun TargetCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.linearBlueStart),
                        colorResource(id = R.color.linearBlueEnd),

                        )
                )
            )
    ) {
        TextTitle(
            text = "Today Target",
            modifier = Modifier
                .padding(16.dp)
        )
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            HorizontalProgressbar(progress = 50)
        }
    }
}

@Composable
fun Progress() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
    ) {
        TextTitle(
            text = "Activity Progress",
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .shadow(elevation = 5.dp, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(16.dp)

        ) {
            val days = listOf(
                "Sat", "Sun", "Mon", "Tre", "Wed", "Thu", "Fri"
            )

            for (i in 0..6)
                VerticalProgressBar(progress = 20 * i, caption = days[i])


        }

    }
}

@Composable
fun Activities(activities: List<Activity>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
    ) {
        TextTitle(text = "Latest Activities")

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(activities) {
                Box {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .shadow(elevation = 5.dp, shape = RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_menu),
                            contentDescription = "menu",
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.End)
                        )

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp)
                        ) {
                            Text(
                                text = it.amount,
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                            )
                            Text(
                                text = it.date,
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TextTitle(text: String, modifier: Modifier = Modifier) {

    Text(
        fontWeight = FontWeight.Bold,
        text = text,
        fontSize = 18.sp,
        modifier = modifier
    )
}

@Composable
fun Int.pxToDp(): Dp {
    return with(LocalDensity.current) { this@pxToDp.toDp() }
}

@Composable
fun VerticalProgressBar(
    total: Int = 100, progress: Int = 0, caption: String
) {
    var height by remember {
        mutableIntStateOf(progress)
    }
    val text by remember {
        mutableStateOf(caption)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .height(120.dp)
                .wrapContentWidth()
                .clip(CircleShape)
                .background(Color.LightGray)
                .onGloballyPositioned {
                    height = ((progress * it.size.height) / total)
                }
        ) {
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .align(Alignment.BottomCenter)
                    .height(height.pxToDp())
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                colorResource(id = R.color.linearBlueStart),
                                colorResource(id = R.color.linearBlueEnd)
                            )
                        )
                    )
            )
        }
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = text,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp,
            color = Color.DarkGray
        )
    }
}

@Composable
fun HorizontalProgressbar(
    total: Int = 100, progress: Int = 0
) {
    var width by remember {
        mutableIntStateOf(progress)
    }

    Column(
        Modifier
            .fillMaxWidth()

    ) {

        Box(
            Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .background(Color.White)
                .onGloballyPositioned {
                    width = ((progress * it.size.width) / total)
                }
        ) {
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .width(width.pxToDp())
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                colorResource(id = R.color.linearPurpleStart),
                                colorResource(id = R.color.linearPurpleEnd)
                            )
                        )
                    )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "0L",
                fontWeight = FontWeight.Black
            )
            Text(
                text = "3L",
                fontWeight = FontWeight.Black
            )
        }
    }
}
