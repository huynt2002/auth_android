package me.auth_kit_aos.auth_kit.sessionService

import java.time.Instant

class SessionService {
    private var lastAuthTime: Long? = null

    companion object {
        private const val MINUTE = 10 // Expired time
        const val EXPIRED_TIME = MINUTE * 60 * 1000
    }

    fun updateTime() {
        lastAuthTime = Instant.now().toEpochMilli()
    }

    fun isExpired(): Boolean {
        return lastAuthTime?.let { Instant.now().toEpochMilli() - lastAuthTime!! > EXPIRED_TIME }
            ?: true
    }
}
