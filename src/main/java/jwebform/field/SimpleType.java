package jwebform.field;

import jwebform.field.structure.FieldResult;
import jwebform.field.structure.SingleFieldType;
import jwebform.field.structure.StaticFieldInfo;
import jwebform.env.Env.EnvWithSubmitInfo;
import jwebform.validation.ValidationResult;

// Just for demonstration!
public class SimpleType implements SingleFieldType {

  @Override
  public FieldResult apply(EnvWithSubmitInfo env) {
    return FieldResult.builder()
        .withStaticElementInfo(new StaticFieldInfo("", t -> "simple\n", 0)).withValidationResult(ValidationResult
        .ok()).build();
  }

}
