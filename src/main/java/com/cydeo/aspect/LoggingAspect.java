package com.cydeo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Aspect
@Controller
@Slf4j // this annotation is going to give logger instance same thing with line 14
public class LoggingAspect {

//    Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
//@Slf4j is most popular in the market
    //This is the short way using this logger
    //https://projectlombok.org/features/log in this web page there is same this logger

    private String getUserName() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // to get logged in user information
        SimpleKeycloakAccount userDetails = (SimpleKeycloakAccount) authentication.getDetails();
        return userDetails.getKeycloakSecurityContext().getToken().getPreferredUsername();
    // got username from kycloak
    }

    @Pointcut("execution(* com.cydeo.controller.ProjectController.*(..)) || execution(* com.cydeo.controller.TaskController.*(..))")
public void anyProjectAndTaskControllerPC() {}

    @Before("anyProjectAndTaskControllerPC()")
    public void beforeAnyProjectAndTaskControllerAdvice(JoinPoint joinPoint) {
    log.info("Before -> Method: {}, User: {}"
    , joinPoint.getSignature().toShortString()
            ,getUserName());
    }

    @AfterReturning(pointcut = "anyProjectAndTaskControllerPC()", returning = "results")
    public void afterAnyProjectAndTaskControllerAdvice(JoinPoint joinPoint, Object results) {
        log.info("After Returning -> Method: {}, User: {}, Results :{}"
                , joinPoint.getSignature().toShortString()
                ,getUserName()
                , results.toString());
    }

    @AfterThrowing(pointcut = "anyProjectAndTaskControllerPC()", throwing = "exception")
    public void afterReturningAnyProjectAndTaskControllerAdvice(JoinPoint joinPoint, Exception exception) {
        log.info("After Returning -> Method: {}, User: {}, Results :{}"
                , joinPoint.getSignature().toShortString()
                ,getUserName()
                , exception.getMessage());
    }
}
