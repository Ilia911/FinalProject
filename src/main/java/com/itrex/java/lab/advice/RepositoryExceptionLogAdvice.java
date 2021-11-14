package com.itrex.java.lab.advice;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RepositoryExceptionLogAdvice {

    private static final String EXCEPTION_MESSAGE_PATTERN = "There was an exception in method: %s with message: %s";

    @AfterThrowing(value = "execution(* com.itrex.java.lab.repository.impl.*.*(..))", throwing = "exception")
    public void logException(JoinPoint jp, Exception exception) {

        log.error(String.format(EXCEPTION_MESSAGE_PATTERN, jp.getSignature().getName(), exception.getMessage()));
        log.error(Arrays.toString(exception.getStackTrace()));
    }
}
