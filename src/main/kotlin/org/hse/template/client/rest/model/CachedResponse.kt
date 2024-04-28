package org.hse.template.client.rest.model

import jakarta.persistence.Entity
import jakarta.persistence.Lob
import org.springframework.data.annotation.Id
import java.time.LocalDateTime


@Entity
data class CachedResponse(
    @jakarta.persistence.Id @Id
    val path: String,
    @Lob
    val response: String,
    val createdTs: LocalDateTime = LocalDateTime.now()
)