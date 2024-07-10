package com.cydeo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) //This annotation I will put top of the Method
@Retention(RetentionPolicy.RUNTIME)// in the runtime it will be active
public @interface DefaultExceptionMessage {

    String defaultMessage() default "";

}
//i am trying to create on annotation i can put top of the message
//if there is an certain exception in the method, I am gonna put this annotation top of the model,
//this method is gonna throw that exception.