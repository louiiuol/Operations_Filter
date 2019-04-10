package com.mybank.app.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy= DoubleDigitValidator.class)
public @interface DoubleDigit {
	String message() default "The amount musn't contain more than 2 digits behind decimal.";

	Class<?>[] groups() default {};

	Class <? extends Payload>[] payload() default {};


}
