package org.hse.template.controller

import org.hse.template.api.HolidaysApi
import org.hse.template.client.rest.api.HolidaysClient
import org.hse.template.client.rest.model.Holiday
import org.hse.template.client.rest.model.Response
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

@RestController
@RequestMapping("/api/v1")
class HolidaysController(
    private val holidaysClient: HolidaysClient
) : HolidaysApi {
    val api_key: String = "fDgPXc7m75MrnVIAIm8EpkZ0ttUUC7DN"

    @GetMapping("/holidays")
    override fun getHolidaysByCountryAndYear(@RequestParam country: String, @RequestParam year: Int): Response {
        return holidaysClient.getHolidays(api_key, country, year).response
    }

    @GetMapping("/upcoming")
    override fun getUpcomingHolidays(@RequestParam country: String): Response {
        val currentDate = LocalDate.now()
        val holidays = holidaysClient.getHolidays(api_key, country, currentDate.year)
        val endOfInterval = currentDate.plusDays(7)

        val formatters = listOf(
            DateTimeFormatter.ISO_OFFSET_DATE_TIME,
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        )

        val holidaysInRange = holidays.response.holidays.filter { holiday ->
            var holidayDate: LocalDate? = null
            var cnt = 0
            for (formatter in formatters) {
                try {
                    cnt++
                    holidayDate = LocalDate.parse(holiday.date.iso, formatter)
                    break
                } catch (e: DateTimeParseException) {
                    if (cnt == 4)
                        println(holiday.date.iso)
                    continue
                }
            }
            holidayDate != null && holidayDate.isAfter(currentDate) && holidayDate.isBefore(endOfInterval)
        }
        return Response(holidaysInRange)
    }

    @GetMapping("/byDate")
    override fun getHolidaysByDate(
        @RequestParam day: Long,
        @RequestParam month: Long,
        @RequestParam year: Long
    ): Response {
        val countries = holidaysClient.getCountries(api_key).response.countries
        val holidays: MutableList<Holiday> = mutableListOf()
        for (country in countries) {
            holidays += holidaysClient.getHolidays(
                api_key,
                country.iso3166.lowercase(Locale.getDefault()),
                year.toInt()
            ).response.holidays.filter { holiday ->
                holiday.date.datetime.day == day && holiday.date.datetime.month == month
            }
        }
        return Response(holidays)
    }

    @GetMapping("/byType")
    override fun getHolidaysByType(@RequestParam type: String, @RequestParam year: Long): Response {
        val countries = holidaysClient.getCountries(api_key).response.countries
        val holidays: MutableList<Holiday> = mutableListOf()
        for (country in countries) {
            holidays += holidaysClient.getHolidays(
                api_key,
                country.iso3166.lowercase(Locale.getDefault()),
                year.toInt()
            ).response.holidays.filter { holiday ->
                holiday.type.contains(type)
            }
        }
        return Response(holidays)
    }

}