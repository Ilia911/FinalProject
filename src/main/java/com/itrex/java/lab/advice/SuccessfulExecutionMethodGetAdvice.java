package com.itrex.java.lab.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class SuccessfulExecutionMethodGetAdvice {

    private static final String SUCCESSFUL_MESSAGE_PATTERN = "Method: %s with successfully called";

    @Pointcut("execution(* com.itrex.java.lab.service.impl.*.find*(..))")
    public void findMethodsInRepositories() {
    }

    @Around(value = "findMethodsInRepositories()")
    public Object printSuccessfulExecutionResult(ProceedingJoinPoint jp) throws Throwable {

        Object result = jp.proceed();
        log.info(String.format(SUCCESSFUL_MESSAGE_PATTERN, jp.getSignature().getName()));
        log.info(result.toString());

        return result;
    }
}
