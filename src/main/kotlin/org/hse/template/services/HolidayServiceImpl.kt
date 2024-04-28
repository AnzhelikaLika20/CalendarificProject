package org.hse.template.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.hse.template.client.rest.api.HolidaysClient
import org.hse.template.client.rest.model.CachedResponse
import org.hse.template.client.rest.model.Holiday
import org.hse.template.client.rest.model.Response
import org.hse.template.repositories.CacheRepository
import org.hse.template.services.interfaces.HolidayService
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

@Service
@Transactional
class HolidayServiceImpl(
    private val holidaysClient: HolidaysClient,
    private val cachedRepository: CacheRepository
) : HolidayService {
    val api_key: String = "fDgPXc7m75MrnVIAIm8EpkZ0ttUUC7DN"

    @Cacheable(value = ["responseCache"], key = "'/holidays/' + #country + '/' + #year")
    override fun getHolidaysByCountryAndYear(@RequestParam country: String, @RequestParam year: Int): Response {
        val pathId = "/holidays/${country}/${year}"
        val cachedResponse = getCachedResponse(pathId)
        if (cachedResponse != null) {
            return cachedResponse
        }
        val response = holidaysClient.getHolidays(api_key, country, year).response
        saveResponseToDatabase(pathId, response.toJson())
        return response
    }

    override fun getUpcomingHolidays(@RequestParam country: String): Response {
        val pathId = "/upcoming/${country}"
        val cachedResponse = getCachedResponse(pathId)
        if (cachedResponse != null) {
            return cachedResponse
        }
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
        val response = Response(holidaysInRange)
        saveResponseToDatabase(pathId, response.toJson())
        return response
    }

    override fun getHolidaysByDate(
        @RequestParam day: Long,
        @RequestParam month: Long,
        @RequestParam year: Long
    ): Response {
        val pathId = "/byDate/${day}/${month}/${year}"
        val cachedResponse = getCachedResponse(pathId)
        if (cachedResponse != null) {
            return cachedResponse
        }
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
        val response = Response(holidays)
        saveResponseToDatabase(pathId, response.toJson())
        return response
    }

    override fun getHolidaysByType(@RequestParam type: String, @RequestParam year: Long): Response {
        val pathId = "/byType/${year}"
        val cachedResponse = getCachedResponse(pathId)
        if (cachedResponse != null) {
            return cachedResponse
        }
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
        val response = Response(holidays)
        saveResponseToDatabase(pathId, response.toJson())
        return response
    }

    override fun getHolidaysByDateAndCountry(day: Long, month: Long, year: Long, country: String): Response {
        val pathId = "/byDateAndCountry/${day}/${month}/${year}"
        val cachedResponse = getCachedResponse(pathId)
        if (cachedResponse != null) {
            return cachedResponse
        }
        val holidays = holidaysClient.getHolidays(
            api_key,
            country,
            year.toInt()
        ).response.holidays.filter { holiday ->
            holiday.date.datetime.day == day && holiday.date.datetime.month == month
        }
        val response = Response(holidays)
        saveResponseToDatabase(pathId, response.toJson())
        return response
    }

    override fun getHolidaysByTypeAndCountry(type: String, year: Long, country: String): Response {
        val pathId = "/byTypeAndCountry/${year}"
        val cachedResponse = getCachedResponse(pathId)
        if (cachedResponse != null) {
            return cachedResponse
        }
        val holidays = holidaysClient.getHolidays(
            api_key,
            country,
            year.toInt()
        ).response.holidays.filter { holiday ->
            holiday.type.contains(type)
        }
        val response = Response(holidays)
        saveResponseToDatabase(pathId, response.toJson())
        return response
    }

    fun saveResponseToDatabase(path: String, response: String) {
        val cachedResponse = CachedResponse(path = path, response = response)
        cachedRepository.save(cachedResponse)
    }

    fun getCachedResponse(pathId: String): Response? {
        val cachedResponse = cachedRepository.getByPath(pathId)
        if (cachedResponse != null && cachedResponse.createdTs.isAfter(LocalDateTime.now().minusMinutes(30)))
            cachedResponse.let {
                val objectMapper = jacksonObjectMapper()
                return objectMapper.readValue<Response>(it.response)
            }
        return null
    }
}