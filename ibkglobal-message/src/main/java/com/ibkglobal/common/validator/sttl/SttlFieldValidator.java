package com.ibkglobal.common.validator.sttl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SttlFieldValidator implements ConstraintValidator<SttlField, Object> {

	private String  fieldName;
	private int     length;
	private boolean valueCheck;
	
	@Override
	public void initialize(SttlField constraintAnnotation) {
		this.fieldName    = constraintAnnotation.fieldName();
		this.length       = constraintAnnotation.length();
		this.valueCheck   = constraintAnnotation.valueCheck();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {			
		boolean result  = true;
		
		context.disableDefaultConstraintViolation();
		
		// Null Check
		if (value != null) {
			// Length Check
			if (value.toString().getBytes().length > length) {
				context.buildConstraintViolationWithTemplate(fieldName + " : Length Error").addConstraintViolation();
				result = false;
			}
			
			// Field Valid Check
			if (result != false && fieldName != "") {
				result = SttlFieldUtil.fieldValueValid(fieldName, value);
				
				if (result == false) {
					context.buildConstraintViolationWithTemplate(fieldName + " : Value Error").addConstraintViolation();
				}
			}
		} else {
			// △, ▲ Check X
			if (valueCheck) {
				context.buildConstraintViolationWithTemplate(fieldName + " : Null Error").addConstraintViolation();
				result = false;
			}
		}
		
		return result;
	}
}
