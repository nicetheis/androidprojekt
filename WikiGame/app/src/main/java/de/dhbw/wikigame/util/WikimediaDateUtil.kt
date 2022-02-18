package de.dhbw.wikigame.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WikimediaDateUtil {

    fun getGETRequestFormattedDatePattern(): String {

        var currentDateMinusTwoDays = LocalDateTime.now().minusDays(2)
        var dateFormatPattern = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        return currentDateMinusTwoDays.format(dateFormatPattern)

    }

}