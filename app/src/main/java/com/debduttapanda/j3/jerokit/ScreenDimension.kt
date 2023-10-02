/*
package com.debduttapanda.j3.jerokit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

val localDesignWidth = compositionLocalOf { 360f }
@Composable
fun ep(
    dimension: Float,
    fullWidth: Float = 360f
): Float{
    val context = LocalContext.current
    val density = LocalDensity.current.density
    val widthPx = context.resources.displayMetrics.widthPixels
    val factor = dimension / fullWidth
    return widthPx * factor / density
}

@Composable
fun dep(
    dimension: Float,
    fullWidth: Float
): Dp {
    return ep(dimension, fullWidth).dp
}

@Composable
fun sep(
    dimension: Float,
    fullWidth: Float
): TextUnit {

    return with(LocalDensity.current) {
        ep(dimension, fullWidth).dp.toSp()
    }
}

@Composable
fun Number.dep(fullWidth: Number = localDesignWidth.current): Dp{
    return dep(dimension = toFloat(), fullWidth = fullWidth.toFloat())
}

@Composable
fun Number.sep(fullWidth: Number = localDesignWidth.current): TextUnit{
    return sep(dimension = toFloat(), fullWidth = fullWidth.toFloat())
}
*/

package com.debduttapanda.j3.jerokit

import android.graphics.Color.pack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

var screenWidthDp = 0
    private set(value) {
        field = value
    }
var screenHeightDp = 0
    private set(value) {
        field = value
    }

val localDesignWidth = compositionLocalOf { 363.5f }
private var localDimensionFactor = 1f
private var savedDensity = 1f
private var savedFontScale = 1f
private var inverseSavedFontScale = 1f

fun ep(
    dimension: Float,
): Float{
    return dimension * localDimensionFactor
}

fun Dp.toMySp(): TextUnit = (value * inverseSavedFontScale).sp

val Number.dep: Dp get(){
    return ep(dimension = toFloat()).dp
}

val Number.depx: Float get() {
    return this.dep.value * savedDensity
}

val Any?.depx: Float get() {
    return if(this is Number) this.dep.value * savedDensity else 0f
}

val Any?.adep: Dp get() {
    return if(this is Number) this.dep else 0.dep
}

val Number.sep get() = ep(dimension = toFloat()).dep.toMySp()

@Composable
fun InitializeMetrics() {
    screenWidthDp = LocalConfiguration.current.screenWidthDp
    screenHeightDp = LocalConfiguration.current.screenHeightDp
    val designWidth = localDesignWidth.current
    val density = LocalDensity.current

    savedDensity = density.density
    savedFontScale = density.fontScale
    inverseSavedFontScale = 1f/ savedFontScale
    localDimensionFactor = screenWidthDp / designWidth
}

operator fun Dp.times(number: Number): Dp {
    return (this.value * number.toFloat()).dep
}
operator fun Number.times(dp: Dp): Dp {
    return (this.toFloat() * dp.value).dep
}

operator fun TextUnit.times(number: Number): TextUnit {
    return (this.value * number.toFloat()).sep
}
operator fun Number.times(sp: TextUnit): TextUnit {
    return (this.toFloat() * sp.value).sep
}

