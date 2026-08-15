package com.example.kmpnews.feature.detail.presentation.ui

internal fun formatPublishedDate(isoDate: String): String {
    if (isoDate.length < 10) return isoDate

    val datePart = isoDate.substring(0, 10)
    val parts = datePart.split("-")
    if (parts.size != 3) return isoDate

    val (year, month, day) = parts
    return "$day.$month.$year"
}
