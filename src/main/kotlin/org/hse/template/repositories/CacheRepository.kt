package org.hse.template.repositories

import org.hse.template.client.rest.model.CachedResponse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface CacheRepository : JpaRepository<CachedResponse, Long> {
    fun getByPath(path: String): CachedResponse?
}