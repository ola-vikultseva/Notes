package com.example.notes.notes.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomRoundedCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 36.dp,
    width: Dp = 400.dp,
    height: Dp = cornerRadius * 6,
    color: Color = Color.LightGray
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = modifier.size(width, height)) {
            val r = cornerRadius.toPx()
            val w = size.width
            val h = size.height
            val path = Path().apply {
                fillType = PathFillType.EvenOdd
                moveTo(r, 0f)
                lineTo(w - r, 0f)
                arcTo(
                    rect = Rect(left = w - r * 2, top = 0f, right = w, bottom = r * 2),
                    startAngleDegrees = -90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                lineTo(w, r * 3)
                arcTo(
                    rect = Rect(left = w - 2 * r, top = r * 2, right = w, bottom = 4 * r),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                arcTo(
                    rect = Rect(left = w - 2 * r, top = r * 4, right = w, bottom = h),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                arcTo(
                    rect = Rect(left = w - 4 * r, top = h - 2 * r, right = w - 2 * r, bottom = h),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                lineTo(r, h)
                arcTo(
                    rect = Rect(left = 0f, top = h - r * 2, right = r * 2, bottom = h),
                    startAngleDegrees = 90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                lineTo(0f, r)
                arcTo(
                    rect = Rect(left = 0f, top = 0f, right = r * 2, bottom = r * 2),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                close()
            }
            drawPath(path, color)
        }
        Row {
            Column(modifier = Modifier.weight(1f)) {
            }
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                SmallFloatingActionButton(
                    onClick = {},
                    shape = CircleShape,
                    containerColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = ""
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                SmallFloatingActionButton(
                    onClick = {},
                    shape = CircleShape,
                    containerColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = ""
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                FloatingActionButton(
                    onClick = {},
                    shape = CircleShape,
                    containerColor = Color.LightGray,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        tint = Color.White,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}