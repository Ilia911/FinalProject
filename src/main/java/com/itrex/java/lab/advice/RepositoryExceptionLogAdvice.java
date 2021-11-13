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

    @AfterThrowing(value = "execution(* com.itrex.java.lab.repository.impl.*.*(..))", throwing = "exception")
    public void logException(JoinPoint jp, Exception exception) {

        log.error("There was an exception in " + jp.getSignature() + " is " + exception.getMessage());
        log.error(Arrays.toString(exception.getStackTrace()));
    }
}
