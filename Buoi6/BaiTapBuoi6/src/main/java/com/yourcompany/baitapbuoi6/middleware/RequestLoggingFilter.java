package com.yourcompany.baitapbuoi6.middleware;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    // Khớp API dạng /api/Student/{id}, ví dụ /api/Student/0 hoặc /api/Student/-1
    private static final Pattern STUDENT_ID_PATH =
            Pattern.compile("^/api/Student/(-?\\d+)/?$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        System.out.printf("[%s] Method: %s - Path: %s%n",
                LocalDateTime.now().format(TIME_FORMAT),
                request.getMethod(),
                path);

        try {
            Matcher matcher = STUDENT_ID_PATH.matcher(path);

            if (matcher.matches()
                    && new BigInteger(matcher.group(1)).signum() <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("text/plain;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("Student id không hợp lệ");
                return; // Không gọi filterChain, nên request không tới Controller
            }

            filterChain.doFilter(request, response); // Cho request đi tiếp
        } finally {
            System.out.println("Status Code: " + response.getStatus());
        }
    }
}