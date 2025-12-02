package com.example.dashboardapi.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Filter che aggiunge dinamicamente il prefisso configurabile (es. `/api/v1`) alle richieste entranti
 * quando la rotta non inizia già con una delle esclusioni (es. `/auth`, `/api`, `/swagger`, `/actuator`).
 *
 * Il prefisso è configurabile tramite la property `api.prefix` in `application.yml`.
 */
@Component
public class ApiPrefixFilter implements Filter {

    private final String apiPrefix;
    private final List<String> exclusions;

    public ApiPrefixFilter(@Value("${api.prefix:/api/v1}") String apiPrefix,
                           @Value("${api.exclusions:/auth,/api,/swagger,/actuator}") String exclusionsCsv) {
        this.apiPrefix = apiPrefix == null || apiPrefix.isBlank() ? "/api/v1" : apiPrefix.trim();
        this.exclusions = Arrays.stream(exclusionsCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();

        if (shouldPrefix(path, req.getContextPath())) {
            HttpServletRequest wrapped = new HttpServletRequestWrapper(req) {
                @Override
                public String getRequestURI() {
                    return prefixed(path);
                }

                @Override
                public String getServletPath() {
                    String ctx = getContextPath();
                    String uri = prefixed(path);
                    if (ctx != null && uri.startsWith(ctx)) {
                        return uri.substring(ctx.length());
                    }
                    return uri;
                }

                @Override
                public StringBuffer getRequestURL() {
                    StringBuffer url = super.getRequestURL();
                    int idx = url.indexOf(getRequestURI());
                    if (idx != -1) {
                        url.replace(idx, url.length(), getRequestURI());
                    }
                    return url;
                }

                private String prefixed(String original) {
                    String ctx = getContextPath();
                    String withCtx = (ctx != null ? ctx : "") + apiPrefix;
                    if (original.startsWith(withCtx)) return original;
                    return (ctx != null ? ctx : "") + apiPrefix + original.substring(ctx != null ? ctx.length() : 0);
                }
            };

            chain.doFilter(wrapped, response);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean shouldPrefix(String path, String contextPath) {
        if (path == null) return false;
        String p = path.startsWith(contextPath) ? path.substring(contextPath.length()) : path;
        for (String ex : exclusions) {
            if (p.startsWith(ex)) return false;
        }
        return true;
    }
}
