package com.gtel.api.interfaces.intercepter;

import com.gtel.api.domains.exceptions.ApplicationException;
import com.gtel.api.interfaces.configguration.i18n.LocaleStringService;
import com.gtel.api.interfaces.models.response.BadRequestResponse;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    protected final HttpServletRequest httpServletRequest;

    private final Tracer tracer;

    private final LocaleStringService localeStringService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> hanldeException(Exception e) {
        log.error("ERROR", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<BadRequestResponse> hanldeException(ApplicationException ex) {
        log.info("handleApplicationException {} with message {}, title {}, data {}", ex.getCode(), ex.getMessage(), ex.getTitle(), ex.getData(), ex);
        BadRequestResponse response = new BadRequestResponse(ex, httpServletRequest);

        String message = localeStringService.getMessage("test.message", ex.getMessage());
        response.setTitle(message);

        String traceId = tracer.currentTraceContext().context() == null ? Thread.currentThread().getName() : tracer.currentTraceContext().context().traceId();

        response.setRequestId(traceId);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
