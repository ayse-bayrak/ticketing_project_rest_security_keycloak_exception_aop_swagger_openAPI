package com.cydeo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

// this class really impressed so popular topic
@Aspect
@Component
@Slf4j
public class PerformanceAspect {
    @Pointcut("@annotation(com.cydeo.annotation.ExecutionTime)")
    public void executionTimePC() {}

    /*
    we added this to UserController class
    @ExecutionTime
    @GetMapping("/{username}")
     */

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
    /*
    Logs on console about @Around
    PerformanceAspect - Execution starts:
    --
    --
    PerformanceAspect - Time taken to execute: 4 ms - Method : UserController.getUsers()
     */
}
