package live.codeland.petsguidesbackend.config.rateLimiting;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class RateLimiter {
    private static final int MAX_REQUESTS_PER_MINUTE = 80;
    private final Map<String, Queue<Long>> requestTimestamps = new ConcurrentHashMap<>();

    public boolean isAllowed(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();

        // Ensure the map entry exists for the IP
        requestTimestamps.putIfAbsent(ipAddress, new LinkedBlockingQueue<>());

        Queue<Long> timestamps = requestTimestamps.get(ipAddress);

        // Remove timestamps older than 1 minute
        while (!timestamps.isEmpty() && timestamps.peek() < currentTime - TimeUnit.MINUTES.toMillis(1)) {
            timestamps.poll();
        }

        // Check if the number of requests is within the limit for the current minute
        if (timestamps.size() < MAX_REQUESTS_PER_MINUTE) {
            timestamps.offer(currentTime);
            return true;
        } else {
            // If exceeded the limit, reject the request
            return false;
        }
    }

}
