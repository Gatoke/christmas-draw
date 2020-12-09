package com.github.gatoke.christmasdraw.port.adapter.logger;

import com.github.gatoke.christmasdraw.domain.DrawResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.StringJoiner;

import static java.lang.String.format;

@Slf4j
@Aspect
@Component
public class DrawResultsLogger {

    @AfterReturning(
            pointcut = "execution(* com.github.gatoke.christmasdraw.application.DrawApplicationService.performDraw(..))",
            returning = "drawResults"
    )
    public void log(final Set<DrawResult> drawResults) {
        final StringJoiner logMessage = new StringJoiner(System.lineSeparator());
        drawResults.forEach(result -> logMessage.add(format(
                "%s -> %s (%s)",
                result.getPerformerId(),
                result.getResultId(),
                result.getResultDisplayName()))
        );
        log.info("Draw performed. Result: " + System.lineSeparator() + logMessage.toString());
    }
}
