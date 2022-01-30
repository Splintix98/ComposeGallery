package com.example.composegallery

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.unit.Dp
import kotlin.math.roundToInt


// ============================================================
// ==================== SnapshotStateLists ====================
// ============================================================

fun <T> SnapshotStateList<T>.swapList(newList: List<T>) {
    clear()
    addAll(newList)
}


fun listToString(list: SnapshotStateList<String>): String {
    var string = ""
    list.forEach {
        string += it
        string += "\n"
    }
    return string
}


fun addIfNotExists(list: SnapshotStateList<String>, string: String) {
    if (string !in list) list.add(string)
}


fun removeFromList(list: SnapshotStateList<String>, string: String) {
    list.remove(string)
}


fun snapshotStateListToList(snapshotStateList: SnapshotStateList<String>): List<String> {
    val list = mutableListOf<String>()
    snapshotStateList.forEach {
        list += it
    }
    return list
}


// =============================================
// ==================== API ====================
// =============================================

inline fun <T> sdk29AndUp(onSdk29: () -> T): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        onSdk29()
    } else null
}


// =============================================
// ================== Screen ===================
// =============================================

fun getScreenHeightInDp(context: Context): Float {
    val displayMetrics = context.resources.displayMetrics
    return displayMetrics.heightPixels / displayMetrics.density
}

fun getScreenWidthInDp(context: Context): Float {
    val displayMetrics = context.resources.displayMetrics
    return displayMetrics.heightPixels / displayMetrics.density
}

fun getScreenDimensionsInDp(context: Context): Point {
    val point = Point()
    val displayMetrics = context.resources.displayMetrics
    displayMetrics.apply {
        point.x = (widthPixels / density).roundToInt()
        point.y = (heightPixels / density).roundToInt()
    }

    return point
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()