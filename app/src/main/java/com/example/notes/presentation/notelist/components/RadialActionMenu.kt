package com.example.notes.presentation.notelist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.notes.R
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RadialActionMenu(
    position: Offset,
    onPinClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val density = LocalDensity.current
    val menuSizePx = with(density) { 180.dp.toPx() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onDismiss
            )
    ) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = (position.x - menuSizePx / 2).toInt(),
                        y = (position.y - menuSizePx / 2).toInt()
                    )
                }
                .size(180.dp),
            contentAlignment = Alignment.Center
        ) {
            ActionButton(
                painterResId = R.drawable.ic_pin_angle,
                angle = 250f,
                onClick = onPinClick
            )
            ActionButton(
                painterResId = R.drawable.ic_edit,
                angle = 295f,
                onClick = onEditClick
            )
            ActionButton(
                painterResId = R.drawable.ic_delete,
                angle = 340f,
                onClick = onDeleteClick
            )
        }
    }
}

@Composable
fun ActionButton(
    painterResId: Int,
    angle: Float,
    onClick: () -> Unit
) {
    val radius = 80.dp
    val offsetX = radius * cos(Math.toRadians(angle.toDouble())).toFloat()
    val offsetY = radius * sin(Math.toRadians(angle.toDouble())).toFloat()
    Box(
        modifier = Modifier.offset(x = offsetX, y = offsetY),
        contentAlignment = Alignment.Center
    ) {
        FloatingActionButton(
            onClick = onClick,
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                focusedElevation = 0.dp,
                hoveredElevation = 0.dp
            )
        ) {
            Icon(
                painter = painterResource(painterResId),
                contentDescription = null
            )
        }
    }
}