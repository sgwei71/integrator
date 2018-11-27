package com.ibkglobal.common.validator.sttl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SttlFieldValidator.class)
public @interface SttlField {
	
	String  message()      default "{ErrorContent}";
	
	String  fieldName()    default "";
	
	int     length();
	
	boolean valueCheck()   default true;

	String  defaultValue() default "";
	
	Class<?>[] groups()    default {}; // Request.class, Reply.class
	
	Class<? extends Payload>[] payload() default {}; 
}
