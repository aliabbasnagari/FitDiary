import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun MyLineChart(
    modifier: Modifier = Modifier,
    values: List<Float>,
    labels: List<String>,
    title: String = "Line Chart",
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    circleColor: Color = MaterialTheme.colorScheme.primary,
    yAxisFormatter: (Float) -> String = { it.toString() },
    maxVisibleEntries: Int = 7
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            if (values.isEmpty() || labels.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No data available")
                }
            } else {
                AndroidView(
                    factory = { context ->
                        LineChart(context).apply {
                            description.isEnabled = false
                            setTouchEnabled(true)
                            isDragEnabled = true
                            setScaleEnabled(true)
                            setPinchZoom(true)
                            setBackgroundColor(android.graphics.Color.TRANSPARENT)

                            xAxis.apply {
                                setTextColor(textColor.toArgb())
                                position = XAxis.XAxisPosition.BOTTOM
                                setDrawGridLines(false)
                                granularity = 1f
                                labelCount = minOf(values.size, maxVisibleEntries)
                                valueFormatter = IndexAxisValueFormatter(labels)
                            }

                            axisLeft.apply {
                                setTextColor(textColor.toArgb())
                                setDrawGridLines(true)
                                valueFormatter = object : ValueFormatter() {
                                    override fun getFormattedValue(value: Float): String {
                                        return yAxisFormatter(value)
                                    }
                                }
                            }
                            animateY(1000, Easing.EaseInOutCubic)
                            axisRight.isEnabled = false
                            legend.isEnabled = false
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { chart ->
                    val chartEntries = values.mapIndexed { index, value ->
                        Entry(index.toFloat(), value)
                    }

                    val dataSet = LineDataSet(chartEntries, title).apply {
                        color = lineColor.toArgb()
                        valueTextColor = textColor.toArgb()
                        setCircleColor(circleColor.toArgb())
                        lineWidth = 2f
                        circleRadius = 4f
                        setDrawCircleHole(false)
                        valueTextSize = 10f
                        setDrawValues(true)
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return yAxisFormatter(value)
                            }
                        }
                        mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                    }

                    chart.data = LineData(dataSet)
                    chart.invalidate()
                }
            }
        }
    }
}