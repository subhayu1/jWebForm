package jwebform.processor;

import jwebform.env.Env.EnvWithSubmitInfo;
import jwebform.field.structure.Field;
import jwebform.field.structure.FieldResult;
import jwebform.field.structure.GroupFieldType;
import jwebform.field.structure.SingleFieldType;
import jwebform.validation.FormValidator;
import jwebform.validation.ValidationResult;
import jwebform.validation.Validator;

import java.util.Collections;
import java.util.List;
import java.util.Map;

// this is doing the "hard work": Let each field do the apply function, run validations, run
// form-validations
public class Processor {


  // do the processing of the field, the validation and the form-validation
  public final FieldResults run(EnvWithSubmitInfo envWithSubmitInfo, GroupFieldType group) {
    // call the apply Method
    FieldResults fieldResults = processFields(envWithSubmitInfo, group.getChilds());

    // run postprocessors
    fieldResults = this.runPostProcessors(fieldResults);

    // run the form validators
    if (envWithSubmitInfo.isSubmitted()) {
      FieldValdationResults overridenValidationResults =
          this.runFormValidations(fieldResults, group.getValidators(group.of()));

      // if form-validators changed validaiton results, correct them on the elemtns
      return this.correctFieldResults(fieldResults, overridenValidationResults);
    } else {
      return fieldResults;
    }
  }


  // RFE: Could be made static. Not needed to generate always a list...
  private List<PostProcessor> getPostProcessors() {
    return Collections.singletonList(new CheckDoubleFieldsPostProcessor());
  }

  private FieldResults runPostProcessors(FieldResults fieldResults) {
    for (PostProcessor postProcessor : getPostProcessors()) {
      fieldResults = postProcessor.postProcess(fieldResults);
    }
    return fieldResults;

  }

  private FieldResults processFields(EnvWithSubmitInfo env, List<Field> fields) {
    FieldResults fieldResults = new FieldResults();
    for (Field container : fields) {
      if (container.fieldType instanceof GroupFieldType) {
        processGroup(env, fieldResults, container);
      } else {
        processSingleType(env, fieldResults, container);
      }
    }
    return fieldResults;
  }

  private void processSingleType(EnvWithSubmitInfo env, FieldResults fieldResults, Field field) {
    // here is where the magic happens! The "apply" method of the fields is called.
    FieldResult result = ((SingleFieldType) field.fieldType).apply(env);
    if (env.isSubmitted()) {
      if (result.getValidationResult() != ValidationResult.undefined()) {
        // type has set the validation itself. This might happen in complex types. And
        // will
        // override the following validation
        // --- do nothing
      } else {
        result =
            result.ofValidationResult(new Validator(field.criteria).validate(result.getValue()));

        // validate with external validators
      }
    } else {
      // do nothing, form is not submitted yet.
    }
    if (fieldResults.containsField(field)) {
      throw new IdenticalFieldException(field);
    }
    fieldResults.put(field, result);
  }

  private void processGroup(EnvWithSubmitInfo env, FieldResults fieldResults, Field field) {
    FieldResults groupTypeResults = this.run(env, (GroupFieldType) field.fieldType);
    FieldResult groupResult = ((GroupFieldType) field.fieldType).process(env, groupTypeResults);
    /*
     * if group result is ok in itself, it can happen, that configured criteria render this to false
     * Example: The date itself is okay, but we want a date, that is youger than 3 days...
     */
    if (groupResult.getValidationResult().isValid) {
      groupResult = groupResult
          .ofValidationResult(new Validator(field.criteria).validate(groupResult.getValue()));
    }
    fieldResults.put(field, groupResult.cloneWithChilds(groupTypeResults));
  }

  private FieldValdationResults runFormValidations(FieldResults fieldResults,
      List<FormValidator> formValidators) {

    FieldValdationResults overridenValidationResults = new FieldValdationResults();
    for (FormValidator formValidator : formValidators) {
      overridenValidationResults.merge(formValidator.validate(fieldResults));
    }
    return overridenValidationResults;
  }

  public boolean checkAllValidationResults(FieldResults correctedFieldResults) {
    boolean formIsValid = true;
    for (Map.Entry<Field, FieldResult> entry : correctedFieldResults) {
      if (entry.getValue().getValidationResult() != ValidationResult.ok()) {
        formIsValid = false;
        break;
      }
    }
    return formIsValid;
  }



  private FieldResults correctFieldResults(FieldResults fieldResults,
      FieldValdationResults overridenValidationResults) {
    overridenValidationResults.getResutls().forEach((field, overridenValidationResult) -> {
      FieldResult re = fieldResults.get(field);
      fieldResults.put(field, re.cloneWithNewValidationResult(overridenValidationResult));
    });
    return fieldResults;
  }


  @SuppressWarnings("serial")
  public class IdenticalFieldException extends RuntimeException {

    public IdenticalFieldException(Field field) {
      super("Field with the same name are not allowed. Plese remove double container: "
          + field.fieldType);
    }
  }


}
