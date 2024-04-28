package org.hse.template.client.rest.api

import org.hse.template.client.rest.model.CountryRoot
import org.hse.template.client.rest.model.Root
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "holidays")
interface HolidaysClient {
    @GetMapping("holidays")
    fun getHolidays(@RequestParam api_key: String, @RequestParam country: String, @RequestParam year: Int): Root

    @GetMapping("countries")
    fun getCountries(@RequestParam api_key: String): CountryRoot
}
