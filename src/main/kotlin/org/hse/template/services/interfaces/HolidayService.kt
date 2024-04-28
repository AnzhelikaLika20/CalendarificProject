package org.hse.template.services.interfaces

import org.hse.template.client.rest.model.Response
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam

@Service
interface HolidayService {
    fun getHolidaysByCountryAndYear(@RequestParam country: String, @RequestParam year: Int): Response
    fun getUpcomingHolidays(@RequestParam country: String): Response
    fun getHolidaysByDate(
        @RequestParam day: Long,
        @RequestParam month: Long,
        @RequestParam year: Long
    ): Response

    fun getHolidaysByType(@RequestParam type: String, @RequestParam year: Long): Response
}