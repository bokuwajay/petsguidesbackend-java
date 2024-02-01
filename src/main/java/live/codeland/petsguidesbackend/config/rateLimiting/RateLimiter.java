package live.codeland.petsguidesbackend.config.rateLimiting;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class RateLimiter {
    private static final int MAX_REQUESTS_PER_MINUTE = 60;
    private final Map<String, Long>  requestTimestamps = new ConcurrentHashMap<>();

    public boolean isAllowed(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();

        requestTimestamps.putIfAbsent(ipAddress, currentTime);

        long lastRequestTime = requestTimestamps.get(ipAddress);
        long elapsedMillis = currentTime - lastRequestTime;
        long elapsedMinutes = TimeUnit.MILLISECONDS.toMinutes(elapsedMillis);

        if (elapsedMinutes >= 1 && requestTimestamps.get(ipAddress) + TimeUnit.MINUTES.toMillis(1) < currentTime) {
            // Reset the counter if more than 1 minute has passed
            requestTimestamps.put(ipAddress, currentTime);
            return true;
        }

        if (requestTimestamps.get(ipAddress) + TimeUnit.MINUTES.toMillis(1) >= currentTime
                && requestTimestamps.get(ipAddress) + MAX_REQUESTS_PER_MINUTE >= currentTime) {
            // Check if the number of requests is within the limit for the current minute
            requestTimestamps.put(ipAddress, currentTime);
            return true;
        }

        return false;
    }
}
