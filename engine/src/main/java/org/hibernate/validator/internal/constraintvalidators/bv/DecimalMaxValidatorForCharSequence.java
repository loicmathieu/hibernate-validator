/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.validator.internal.constraintvalidators.bv;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.DecimalMax;

import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/**
 * Check that the character sequence (e.g. string) being validated represents a number, and has a value
 * less than or equal to the maximum value specified.
 *
 * @author Alaa Nassef
 */
public class DecimalMaxValidatorForCharSequence implements ConstraintValidator<DecimalMax, CharSequence> {

	private static final Log LOG = LoggerFactory.make( MethodHandles.lookup() );

	private BigDecimal maxValue;
	private boolean inclusive;

	@Override
	public void initialize(DecimalMax maxValue) {
		try {
			this.maxValue = new BigDecimal( maxValue.value() );
		}
		catch (NumberFormatException nfe) {
			throw LOG.getInvalidBigDecimalFormatException( maxValue.value(), nfe );
		}
		this.inclusive = maxValue.inclusive();
	}

	@Override
	public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
		//null values are valid
		if ( value == null ) {
			return true;
		}
		try {
			int comparisonResult = new BigDecimal( value.toString() ).compareTo( maxValue );
			return inclusive ? comparisonResult <= 0 : comparisonResult < 0;
		}
		catch (NumberFormatException nfe) {
			return false;
		}
	}
}
