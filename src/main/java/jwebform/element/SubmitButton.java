package jwebform.element;

import jwebform.element.structure.ElementResult;
import jwebform.element.structure.HTMLProducer;
import jwebform.element.structure.PrepareInfos;
import jwebform.element.structure.Themable;
import jwebform.validation.ValidationResult;

public class SubmitButton implements Themable {

  public static String KEY = "jwebform.element.SubmitButton";
  
  public final String label;

  public SubmitButton(String label) {
    this.label = label;
  }

  @Override
  public ElementResult prepare(PrepareInfos renderInfos) {
    HTMLProducer producer = renderInfos.getTheme().getProducer(this);
    return new ElementResult("submit", producer, ValidationResult.ok(), "", 1, 
        this.getKey(), null, this);
  }

  @Override
  public String getKey() {
    return SubmitButton.KEY;
  }

  @Override
  public HTMLProducer getDefault() {
    return producerInfos -> "<input tabindex=\"" + producerInfos.getTabIndex() + "\" type=\"submit\" value=\""
        + label + "\"><!-- own renderer -->";
  }

}
