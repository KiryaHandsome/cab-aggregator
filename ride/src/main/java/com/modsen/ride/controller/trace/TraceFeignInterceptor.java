package com.modsen.ride.controller.trace;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class TraceFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        Map<String, Collection<String>> headers = template.headers();
        if (traceHeaderNotPresent(headers)) {
            String traceId = RequestTrace.getId()
                    .orElseGet(() -> UUID.randomUUID().toString());
            RequestTrace.setId(traceId);
            MDC.put(RequestTrace.TRACE_ID_HEADER, traceId);
            template.header(RequestTrace.TRACE_ID_HEADER, traceId);
            log.trace("traceId={}", traceId);
            ;
        }
    }

    private boolean traceHeaderNotPresent(Map<String, Collection<String>> headers) {
        Collection<String> values = headers.get(RequestTrace.TRACE_ID_HEADER);
        return values == null || values.isEmpty();
    }
}
