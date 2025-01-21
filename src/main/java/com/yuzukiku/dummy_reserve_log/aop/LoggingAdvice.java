package com.yuzukiku.dummy_reserve_log.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAdvice {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAdvice.class);

    @Before("execution(* com.yuzukiku.dummy_reserve_log.*(..))")
    public void logMethodStart(JoinPoint joinPoint) {
        logger.debug("Method start: {}({})", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "execution(* com.yuzukiku.dummy_reserve_log..*(..))", returning = "result")
    public void logMethodEnd(JoinPoint joinPoint, Object result) {
        logger.debug("Method end: {}, result: {}", joinPoint.getSignature().getName(), result);
    }
}
