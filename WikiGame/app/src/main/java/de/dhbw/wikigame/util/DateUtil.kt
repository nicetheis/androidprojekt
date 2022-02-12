package de.dhbw.wikigame.util

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateUtil {

    public fun getRequestFormattedDatePattern(): String {

        var currentDateMinusOneDay = LocalDateTime.now().minusDays(1)
        var dateFormatPattern = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        return currentDateMinusOneDay.format(dateFormatPattern)

    }

}