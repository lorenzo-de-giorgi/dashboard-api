package com.example.dashboardapi.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;

/**
 * Configurazione che applica automaticamente un prefisso alle rotte dei controller.
 *
 * Regole in questa implementazione:
 * - Se una mapping contiene già un pattern che inizia con `/auth` o `/api`, non viene prefissata.
 * - Altrimenti viene anteposto `/api/v1` a tutte le rotte registrate.
 *
 * Vantaggio: non serve modificare i controller esistenti; puoi continuare a definire
 * le rotte normalmente e avranno il prefisso `/api/v1` automaticamente.
 */
@Configuration
public class ApiPrefixConfig implements WebMvcRegistrations {

    private static final String API_PREFIX = "/api/v1";

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping() {
            @Override
            protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
                if (mapping != null && shouldPrefix(mapping)) {
                    RequestMappingInfo prefixMapping = RequestMappingInfo.paths(API_PREFIX).build();
                    mapping = prefixMapping.combine(mapping);
                }
                super.registerHandlerMethod(handler, method, mapping);
            }

            private boolean shouldPrefix(RequestMappingInfo mapping) {
                try {
                    // controlla i pattern già definiti e salta se uno di essi inizia con /auth o /api
                    for (String pattern : mapping.getPatternsCondition().getPatterns()) {
                        if (pattern.startsWith("/auth") || pattern.startsWith("/api")) {
                            return false;
                        }
                    }
                } catch (Exception ex) {
                    // se qualcosa va storto, non rompere il registro: non prefissare
                    return false;
                }
                return true;
            }
        };
    }

}
