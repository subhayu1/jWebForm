package jwebform.element;

import com.coverity.security.Escape;

import jwebform.element.structure.Decoration;
import jwebform.element.structure.Element;
import jwebform.element.structure.ElementResult;
import jwebform.element.structure.HTMLProducer;
import jwebform.element.structure.OneValueElementProcessor;
import jwebform.env.Env.EnvWithSubmitInfo;

public class TextAreaType implements Element {

  public final OneValueElementProcessor oneValueElement;

  private final Decoration decoration;

  public TextAreaType(String name, Decoration decoration, String initialValue) {
    oneValueElement = new OneValueElementProcessor(name, initialValue);
    this.decoration = decoration;

  }

  @Override
  public ElementResult apply(EnvWithSubmitInfo env) {
    return oneValueElement.calculateElementResult(env, getDefault());
  }

  public HTMLProducer getDefault() {
    return (pi) -> pi.getTheme().getRenderer().renderInputComplex("textarea",
        Escape.html(pi.getElementResult().getValue()), pi, decoration);
  }

  @Override
  public String toString() {
    return String.format("TextAreaInput. name=%s", oneValueElement.name);
  }

}
