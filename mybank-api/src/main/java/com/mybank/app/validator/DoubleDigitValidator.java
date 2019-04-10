package com.mybank.app.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DoubleDigitValidator implements ConstraintValidator<DoubleDigit, Double> {

	@Override
	public boolean isValid(Double number, ConstraintValidatorContext arg1) {
		String[] check = Double.toString(number).split("[\\.\\,]+");
		return !(check[1].length() > 2 );
	}

}
