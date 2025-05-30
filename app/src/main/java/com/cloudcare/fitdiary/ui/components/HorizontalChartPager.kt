package com.cloudcare.fitdiary.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalChartPager(
    modifier: Modifier,
    pages: List<@Composable () -> Unit>
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        pageSpacing = 16.dp
    ) { page ->
        Box(modifier = Modifier.fillMaxSize()) {
            pages[page]()
        }
    }

    DotsIndicator(
        totalDots = pages.size,
        selectedIndex = pagerState.currentPage,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}

@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    selectedColor: Color = MaterialTheme.colorScheme.onTertiaryContainer,
    unselectedColor: Color = MaterialTheme.colorScheme.tertiaryContainer
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(totalDots) { index ->
            val color = if (index == selectedIndex) selectedColor else unselectedColor
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}