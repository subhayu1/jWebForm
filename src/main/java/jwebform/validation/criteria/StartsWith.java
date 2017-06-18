package jwebform.validation.criteria;

import jwebform.element.structure.Validateable;
import jwebform.validation.Criterion;
import jwebform.validation.ValidationResult;

/**
 * Checks if value starts with the given string.
 * 
 * Based on work of armandino (at) gmail.com
 */
public final class StartsWith implements Criterion {
	private String[] prefixes;

	StartsWith(String... prefixes) {
		this.prefixes = prefixes;
	}

	@Override
	public ValidationResult validate(Validateable value) {
		boolean isValid = false;
		for (String prefix : prefixes) {
			if (value.getValue().startsWith(prefix))
				isValid = true;
		}

		if (!isValid) {
			return ValidationResult.fail("jformchecker.starts_with", (Object[]) prefixes);
		}
		return ValidationResult.ok();
	}

}