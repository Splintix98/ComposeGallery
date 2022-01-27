package com.example.composegallery

import android.os.Build
import androidx.compose.runtime.snapshots.SnapshotStateList


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