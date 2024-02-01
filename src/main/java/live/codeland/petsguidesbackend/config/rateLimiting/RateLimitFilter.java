package live.codeland.petsguidesbackend.config.rateLimiting;

import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RateLimitFilter.class);

    @Autowired
    private RateLimiter rateLimiter;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (rateLimiter.isAllowed(request)) {
            filterChain.doFilter(request, response);
        } else {
            // Get the IP address of the request
            String ipAddress = request.getRemoteAddr();

            // Log the attack info
            logBlockedRequest(ipAddress, request);

            // Block the IP address using iptables
//            blockIP(ipAddress);

            response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            response.getWriter().write("Too many requests. IP blocked.");
        }
    }

    private void logBlockedRequest(String ipAddress, HttpServletRequest request) {
        logger.warn("Blocked request from IP address: {}", ipAddress);

        // Log additional information about the request if needed
        logger.info("Request URI: {}", request.getRequestURI());
        logger.info("Request method: {}", request.getMethod());
        logger.info("Query parameters: {}", request.getQueryString());
        logger.info("User-Agent: {}", request.getHeader("User-Agent"));
        logger.info("Timestamp: {}", LocalDateTime.now());

        // Log stack trace if available
        if (logger.isDebugEnabled()) {
            logger.debug("Stack trace of the blocked request", new Exception());
        }
    }


    private void blockIP(String ipAddress){
        // Execute system command to block IP (this command is for Linux server specific)
//        String command = "sudo iptables -A INPUT -s " + ipAddress + " -j DROP";
        String command =  ipAddress ;
        try {
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Blocked IP: " + ipAddress);
            } else {
                System.err.println("Failed to block IP: " + ipAddress);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
