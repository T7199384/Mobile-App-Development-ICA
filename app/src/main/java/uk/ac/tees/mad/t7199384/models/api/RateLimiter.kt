package uk.ac.tees.mad.t7199384.models.api

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RateLimiter : Interceptor {
    private var requestCount = 0L
    private var startTimeMillis = 0L

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(this) {
            val currentTimeMillis = System.currentTimeMillis()

            // Reset request count if one second has passed
            if (currentTimeMillis - startTimeMillis >= 1000) {
                startTimeMillis = currentTimeMillis
                requestCount = 0
            }

            // Check if the request count exceeds the limit
            if (requestCount >= MAX_REQUESTS_PER_SECOND) {
                // Delay the request
                val delayMillis = startTimeMillis + 1000 - currentTimeMillis
                if (delayMillis > 0) {
                    Thread.sleep(delayMillis)
                }

                // Reset request count after the delay
                startTimeMillis = System.currentTimeMillis()
                requestCount = 0
            }

            // Increment the request count
            requestCount++

            // Proceed with the request
            return chain.proceed(chain.request())
        }
    }

    companion object {
        private const val MAX_REQUESTS_PER_SECOND = 20
    }
}