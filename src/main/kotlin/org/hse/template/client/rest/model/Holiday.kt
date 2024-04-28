package org.hse.template.client.rest.model

data class Root(
    val meta: Meta,
    val response: Response,
)

data class Meta(
    val code: Long,
)

data class Response(
    val holidays: List<Holiday>,
)

data class Holiday(
    val name: String,
    val description: String,
    val date: Date,
    val type: List<String>,
)

data class Date(
    val iso: String,
    val datetime: Datetime,
)

data class Datetime(
    val year: Long,
    val month: Long,
    val day: Long,
)
