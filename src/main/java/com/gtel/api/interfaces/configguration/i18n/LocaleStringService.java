package com.gtel.api.interfaces.configguration.i18n;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import java.text.MessageFormat;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Log4j2
public class LocaleStringService {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public Locale getCurrentLocale() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return Locale.getDefault();
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return localeResolver.resolveLocale(request);
    }

    public String getMessage(String messageCode, String defaultMessage, Object... params) {
        Locale currentLocale = getCurrentLocale();
        try {
            return messageSource.getMessage(messageCode, params, currentLocale);
        }catch (Exception ex) {
            log.error("Can not find messages for locale {}", currentLocale.getLanguage());
            try {
                return MessageFormat.format(defaultMessage, params);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return defaultMessage;
            }
        }
    }
}
