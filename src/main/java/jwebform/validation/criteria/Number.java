package jwebform.validation.criteria;

import jwebform.element.structure.Validateable;
import jwebform.validation.Criterion;
import jwebform.validation.ValidationResult;

/**
 * Checks that value is a number.
 * 
 * Based on work of armandino (at) gmail.com
 */
public final class Number implements Criterion {

	Number() {
	}

	@Override
	public ValidationResult validate(Validateable value) {
		try {
			Integer.parseInt(value.getValue());
			return ValidationResult.ok();
		} catch (NumberFormatException e) {
			return ValidationResult.fail("jformchecker.not_a_number");
		}
	}

}