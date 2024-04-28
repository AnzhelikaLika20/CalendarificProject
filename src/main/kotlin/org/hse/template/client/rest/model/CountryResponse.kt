package org.hse.template.client.rest.model

import com.fasterxml.jackson.annotation.JsonProperty

data class CountryRoot(
    val meta: CountryMeta,
    val response: CountryResponse,
)

data class CountryMeta(
    val code: Long,
)

data class CountryResponse(
    val url: String,
    val countries: List<Country>,
)

data class Country(
    @JsonProperty("country_name")
    val countryName: String,
    @JsonProperty("iso-3166")
    val iso3166: String,
    @JsonProperty("total_holidays")
    val totalHolidays: Long,
    @JsonProperty("supported_languages")
    val supportedLanguages: Long,
    val uuid: String,
    @JsonProperty("flag_unicode")
    val flagUnicode: String,
)

