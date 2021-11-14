package com.itrex.java.lab.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class SuccessfulExecutionMethodGetAdvice {

    private static final String SUCCESSFUL_MESSAGE_PATTERN = "Method: %s with successfully called";

    @Pointcut("execution(* com.itrex.java.lab.repository.impl.*.find*(..))")
    public void findMethodsInRepositories() {
    }

    @AfterReturning(value = "findMethodsInRepositories()")
    public void printSuccessfulExecutionResult(JoinPoint jp) {

        log.info(String.format(SUCCESSFUL_MESSAGE_PATTERN, jp.getSignature().getName()));
    }
}
