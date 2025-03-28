package com.gtel.api.interfaces.configguration.i18n;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class LocaleConfiguration extends AcceptHeaderLocaleResolver {
    private static final Locale DEFAULT_LOCALE = Locale.of("vn");

    private static final Map<String, Locale> SUPPORT_LOCALES = Stream.of(
            Locale.ENGLISH,
            DEFAULT_LOCALE
    ).collect(Collectors.toMap(Locale::getLanguage, locale->locale));

    public LocaleConfiguration() {
        super();
        setSupportedLocales(new ArrayList<>(SUPPORT_LOCALES.values()));
        setDefaultLocale(DEFAULT_LOCALE);
    }

    @NotNull
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        if (Objects.isNull(language) || Strings.isBlank(language)) {
            return Objects.requireNonNull(getDefaultLocale());
        }
        List<Locale.LanguageRange> languageRanges = Locale.LanguageRange.parse(language);
        Locale foundLocale = Locale.lookup(languageRanges, getSupportedLocales());
        if (Objects.isNull(foundLocale)) {
            return Objects.requireNonNull(getDefaultLocale());
        }
        return foundLocale;
    }

    @Override
    public Locale getDefaultLocale() {
        return DEFAULT_LOCALE;
    }
}
