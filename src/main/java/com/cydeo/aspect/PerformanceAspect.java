package com.cydeo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;


// in the interview when she display the this class interviewer  really impressed so popular topic
@Aspect
@Component
@Slf4j
public class PerformanceAspect {
    @Pointcut("@annotation(com.cydeo.annotation.ExecutionTime)")
    public void executionTimePC() {}

    @Around("executionTimePC()")
    public Object aroundAnyExecutionTimeAdvice (ProceedingJoinPoint proceedingJoinPoint) {

        long beforeTime = System.currentTimeMillis(); // give me current time in mlsecond
        Object result = null;
        log.info("Execution starts:");

        try{
            result = proceedingJoinPoint.proceed();
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }

        long afterTime = System.currentTimeMillis();
        log.info("Time taken to execute: {} ms - Method : {}"
                , (afterTime-beforeTime), proceedingJoinPoint.getSignature().toShortString());

        return result;

    }
}
