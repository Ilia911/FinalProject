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
public class SuccessfulExecutionMethodFindAdvice {

    private static final String SUCCESSFUL_MESSAGE_PATTERN = "Method: %s was successfully called";

    @Pointcut("execution(* com.itrex.java.lab.repository.impl.*.find*(..))")
    public void findMethodsInRepositories() {
    }

    @AfterReturning(value = "findMethodsInRepositories()", returning = "entity")
    public void printSuccessfulExecutionResult(JoinPoint jp, Object entity) {

        log.info(String.format(SUCCESSFUL_MESSAGE_PATTERN, jp.getSignature().getName()));
        log.info(entity.toString());
    }
}
