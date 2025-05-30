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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

@Composable
fun MyPieChart(
    modifier: Modifier = Modifier,
    values: List<Any>,
    title: String = "Pie Chart",
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    pieColors: List<Int> = ColorTemplate.MATERIAL_COLORS.toList(),
    valueFormat: (Float) -> String = { it.toString() }
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

            if (values.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No data available")
                }
            } else {
                AndroidView(
                    factory = { context ->
                        PieChart(context).apply {
                            description.isEnabled = false
                            setTouchEnabled(true)
                            setBackgroundColor(android.graphics.Color.TRANSPARENT)
                            legend.apply {
                                isEnabled = true
                                textSize = 14f
                                setTextColor(textColor.toArgb())
                                form = Legend.LegendForm.CIRCLE
                                orientation=Legend.LegendOrientation.VERTICAL
                            }
                            isRotationEnabled = true
                            rotationAngle = 270f
                            contentDescription = "Pie chart $title"
                            animateY(1000, Easing.EaseInOutQuad)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { chart ->

                    val chartEntries = values.groupingBy { it }.eachCount().map { (value, count) ->
                        PieEntry(count.toFloat(), value.toString())
                    }

                    val dataSet = PieDataSet(chartEntries, "").apply {
                        colors = pieColors
                        valueTextColor = textColor.toArgb()
                        valueTextSize = 12f
                        setDrawValues(true)
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return valueFormat(value)
                            }
                        }
                        sliceSpace = 3f
                        selectionShift = 8f
                    }

                    //chart.data = PieData(dataSet)
                    chart.data = PieData(dataSet).apply {
                        setValueTextSize(14f)
                        setValueTextColor(textColor.toArgb())
                    }
                    chart.invalidate()
                }
            }
        }
    }
}