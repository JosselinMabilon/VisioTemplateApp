package com.example.visioglobe.util


/**
 * This class contains usefull functions to convert Date
 */
interface DateFormatter {

    /**
     * Convert a [Timestamp] in ms as Date
     * @param [Long]
     * @return the format date as [String].
     */
    fun mapToDateString(date: Long): String
}
