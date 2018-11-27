package com.ibkglobal.integrator.engine.builder.model.endpoint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EndpointField {
	
	String  fieldName()    default "";
	
	Class<?>[] groups()    default {}; // Request.class, Reply.class
	
	Class<? extends Payload>[] payload() default {}; 
}
