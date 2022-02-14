package de.dhbw.wikigame.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WikimediaDateUtil {

    public fun getGETRequestFormattedDatePattern(): String {

        var currentDateMinusOneDay = LocalDateTime.now().minusDays(2)
        var dateFormatPattern = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        return currentDateMinusOneDay.format(dateFormatPattern)

    }

}