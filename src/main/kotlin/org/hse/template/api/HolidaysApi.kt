package org.hse.template.api

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.hse.template.client.rest.model.Response
import org.springframework.web.bind.annotation.RequestParam


interface HolidaysApi {
    @Tag(name = "Получение списка праздников по стране и году")
    @ApiResponses(
        ApiResponse(
            description = "Списка праздников по стране и году",
            responseCode = "200"
        )
    )
    fun getHolidaysByCountryAndYear(
        @RequestParam country: String, @RequestParam year: Int
    ): Response

    @Tag(name = "Получение списка предстоящих в течение недели праздников по стране")
    @ApiResponses(
        ApiResponse(
            description = "Списка предстоящих в течение недели праздников по стране",
            responseCode = "200"
        )
    )
    fun getUpcomingHolidays(
        @RequestParam country: String
    ): Response

    @Tag(name = "Получение списка праздников по дате")
    @ApiResponses(
        ApiResponse(
            description = "Списка праздников по дате",
            responseCode = "200"
        )
    )
    fun getHolidaysByDate(
        @RequestParam day: Long, @RequestParam month: Long, @RequestParam year: Long
    ): Response

    @Tag(name = "Получение списка праздников по типу. Возможные значения: \"National holiday\", \"Orthodox\", \"Observance\", \"Muslim\", \"Season\", \"Weekend\"")
    @ApiResponses(
        ApiResponse(
            description = "Списка праздников по типу",
            responseCode = "200"
        )
    )
    fun getHolidaysByType(@RequestParam type: String, @RequestParam year: Long): Response

    @Tag(name = "Получение списка праздников по дате и стране")
    @ApiResponses(
        ApiResponse(
            description = "Списка праздников по дате и стране",
            responseCode = "200"
        )
    )
    fun getHolidaysByDateAndCountry(
        @RequestParam day: Long, @RequestParam month: Long, @RequestParam year: Long, @RequestParam country: String
    ): Response

    @Tag(name = "Получение списка праздников по типу и стране. Возможные значения: \"National holiday\", \"Orthodox\", \"Observance\", \"Muslim\", \"Season\", \"Weekend\"")
    @ApiResponses(
        ApiResponse(
            description = "Списка праздников по типу и стране",
            responseCode = "200"
        )
    )
    fun getHolidaysByTypeAndCountry(
        @RequestParam type: String,
        @RequestParam year: Long,
        @RequestParam country: String
    ): Response

}