package com.example.composegallery

import androidx.compose.runtime.snapshots.SnapshotStateList


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