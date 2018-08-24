package jwebform;

import jwebform.field.structure.Field;
import jwebform.field.structure.GroupFieldType;
import jwebform.env.Env;
import jwebform.processor.FieldResults;
import jwebform.processor.FormResultBuilder;
import jwebform.processor.Processor;

import java.util.List;

// Represents a form
public final class Form {

  private final String id;
  private final FormResultBuilder formResultBuilder;

  private final GroupFieldType group;

  public Form(String id, GroupFieldType group, FormResultBuilder formResultBuilder) {
    this.id = id;
    this.formResultBuilder = formResultBuilder;
    this.group = group;
  }


  // process each fieldType, run validations
  public final FormResult run(Env env) {
    Processor p = new Processor();
    FieldResults result = p.run(env.getEnvWithSumitInfo(id), group);
    return formResultBuilder.build(id, result, p.checkAllValidationResults(result));
  }



  public final List<Field> getElements() {
    return group.getChilds();
  }

  public final String getId() {
    return id;
  }



}
