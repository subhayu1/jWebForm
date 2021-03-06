package jwebform;

import java.util.List;
import jwebform.env.Env;
import jwebform.field.structure.Field;
import jwebform.field.structure.GroupFieldType;
import jwebform.processor.FieldResults;
import jwebform.processor.FormResultBuilder;
import jwebform.processor.Processor;

/**
 * Represents a form Holds Fields and a formId - and can be "run"
 */
public final class Form {

  private final String id;

  private final GroupFieldType group;
  // RFE: maybe we should inject this in the future too.
  private final static Processor processor = new Processor();


  public Form(String id, GroupFieldType group) {
    this.id = id;
    this.group = group;
  }

  // process each fieldType, run validations
  @Deprecated // FormResultBuilder is not nessessary any more (because formResult should be as is)
              // Encapsulate FormResult instead
  public final FormResult run(Env env, FormResultBuilder formResultBuilder) {
    FieldResults result = processor.run(env.getEnvWithSumitInfo(id), group);
    return formResultBuilder.build(id, result, processor.checkAllValidationResults(result),
        env.getEnvWithSumitInfo(id).isFirstRun());
  }

  public final FormResult run(Env env) {
    FieldResults result = processor.run(env.getEnvWithSumitInfo(id), group);
    FormResult formResult = new FormResult(id, result, processor.checkAllValidationResults(result),
        env.getEnvWithSumitInfo(id).isFirstRun());
    return formResult;
  }


  public final List<Field> getFields() {
    return group.getChilds();
  }

  public final String getId() {
    return id;
  }

}
