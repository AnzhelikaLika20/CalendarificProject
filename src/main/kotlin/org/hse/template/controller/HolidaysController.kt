package org.hse.template.controller

import org.hse.template.api.HolidaysApi
import org.hse.template.client.rest.model.Response
import org.hse.template.services.HolidayServiceImpl
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class HolidaysController(
    private val holidaysService: HolidayServiceImpl
) : HolidaysApi {

    @GetMapping("/holidays")
    override fun getHolidaysByCountryAndYear(@RequestParam country: String, @RequestParam year: Int): Response {
        return holidaysService.getHolidaysByCountryAndYear(country, year)
    }

    @GetMapping("/upcoming")
    override fun getUpcomingHolidays(@RequestParam country: String): Response {
        return holidaysService.getUpcomingHolidays(country)
    }

    @GetMapping("/byDate")
    override fun getHolidaysByDate(
        @RequestParam day: Long,
        @RequestParam month: Long,
        @RequestParam year: Long
    ): Response {
        return holidaysService.getHolidaysByDate(day, month, year)
    }

    @GetMapping("/byType")
    override fun getHolidaysByType(@RequestParam type: String, @RequestParam year: Long): Response {
        return holidaysService.getHolidaysByType(type, year)
    }

}