package com.modsen.driver.controller.trace;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class TraceHeaderInterceptor extends OncePerRequestFilter implements Ordered {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String traceId = Optional.ofNullable(request.getHeader(RequestTrace.TRACE_ID_HEADER))
                    .orElseGet(() -> UUID.randomUUID().toString());
            log.trace("traceId={}", traceId);
            RequestTrace.setId(traceId);
            MDC.put(RequestTrace.TRACE_ID_HEADER, traceId);
            filterChain.doFilter(request, response);
        } finally {
            RequestTrace.removeId();
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}