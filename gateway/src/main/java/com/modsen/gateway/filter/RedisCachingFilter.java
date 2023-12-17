package com.modsen.gateway.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCachingFilter implements GlobalFilter, Ordered {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ModifyResponseBodyGatewayFilterFactory modifyResponseBodyFilterFactory;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!HttpMethod.GET.equals(exchange.getRequest().getMethod())) {
            return chain.filter(exchange);
        }
        String cacheKey = generateCacheKey(exchange.getRequest());
        if (isInCache(cacheKey)) {
            log.info("Cache hit by key={}", cacheKey);
            String response = Objects.requireNonNull(redisTemplate.opsForValue().get(cacheKey)).toString();
            DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(response.getBytes());
            exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
            return exchange.getResponse().writeWith(Flux.just(dataBuffer));
        } else {
            log.info("Cache miss for key={}", cacheKey);
            ModifyResponseBodyGatewayFilterFactory.Config config = new ModifyResponseBodyGatewayFilterFactory.Config();
            config.setRewriteFunction(String.class, String.class, (swe, body) -> {
                log.info("Put value to cache: {}", body);
                redisTemplate.opsForValue().set(cacheKey, body);
                return Mono.just(body);
            });
            return modifyResponseBodyFilterFactory
                    .apply(config)
                    .filter(exchange, chain);
        }
    }

    @Override
    public int getOrder() {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
    }

    private boolean isInCache(String cacheKey) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey));
    }

    private String generateCacheKey(ServerHttpRequest request) {
        return request.getURI() + "?" + request.getQueryParams();
    }
}
