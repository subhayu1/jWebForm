package jwebform.validation.criteria;

import jwebform.element.structure.Validateable;
import jwebform.validation.Criterion;
import jwebform.validation.ValidationResult;

/**
 * provides some utils for criterias, that compare numbers (currentyl: min/max)
 * 
 * @author jpier
 *
 */
public abstract class AbstractNumberComparingCriterion implements Criterion {
	@Override
	public ValidationResult validate(Validateable value) {
		try {
			int input = Integer.parseInt(value.getValue());
			return validateNumberAndSetError(input);
		} catch (NumberFormatException e) {
			return ValidationResult.fail("jformchecker.not_a_number");
		}
	}

	public abstract ValidationResult validateNumberAndSetError(int input);

}