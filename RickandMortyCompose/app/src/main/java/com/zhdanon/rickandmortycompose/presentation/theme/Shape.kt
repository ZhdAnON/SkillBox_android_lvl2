package com.zhdanon.rickandmortycompose.presentation.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

val RaMShapes = Shapes(
    small = CutCornerShape(topStart = 8.dp),
    medium = CutCornerShape(24.dp),
    large = RoundedCornerShape(8.dp)
)