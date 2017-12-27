package jwebform.element;

import java.util.ArrayList;
import java.util.List;

import jwebform.element.structure.Decoration;
import jwebform.element.structure.Element;
import jwebform.element.structure.ElementResult;
import jwebform.element.structure.HTMLProducer;
import jwebform.element.structure.OneValueElementProcessor;
import jwebform.env.Env.EnvWithSubmitInfo;
import jwebform.view.ElementRenderer;

public class RadioType implements Element {

  public final static String KEY = "jwebform.element.RadioInput";

  final public List<RadioInputEntry> entries;

  public final OneValueElementProcessor oneValueElement;
  public final Decoration decoration;

  // RFE: Add groups too!
  public RadioType(String name, Decoration decoration, String initialValue, String keys[],
      String values[]) {
    this.oneValueElement = new OneValueElementProcessor(name, initialValue);
    entries = generateEntriesFromKeyValues(keys, values);
    this.decoration = decoration;
  }

  @Override
  public ElementResult apply(EnvWithSubmitInfo env) {
    return oneValueElement.calculateElementResultWithInputCheck(env, KEY, getDefault(),
        t -> ensureValueIsAllowed(t));
  }

  /**
   * This ensures, that only the keys will be allowed for valid values.
   * 
   * @param fetchValue
   * @return
   */
  private boolean ensureValueIsAllowed(String fetchValue) {
    for (RadioInputEntry selectInputEntry : entries) {
      if (selectInputEntry.getKey().equals(fetchValue)) {
        return true;
      }
    }
    return false;
  }

  private List<RadioInputEntry> generateEntriesFromKeyValues(String[] keys, String[] values) {
    if (keys.length != values.length) {
      throw new IllegalArgumentException("Key / Values with unequal length");
    }
    List<RadioInputEntry> entriesToCreate = new ArrayList<>();
    for (int i = 0; i < keys.length; i++) {
      entriesToCreate.add(this.generateEntry(keys[i], values[i]));
    }
    return entriesToCreate;
  }

  public RadioInputEntry generateEntry(String key, String value) {
    return new RadioInputEntry(key, value);
  }


  @Override
  public String toString() {
    return String.format("RadioInput. name=%s", oneValueElement.name);
  }


  // very simple version!
  public HTMLProducer getDefault() {
    return (pi) -> {
      return pi.getTheme().getRenderer().renderInputFree(
          pi.getTheme().getRadioRenderer().renderInputs(pi, entries), pi,
          decoration, ElementRenderer.InputVariant.radio);
    };
  }



  // class that represents an entry in the selectInput
  public class RadioInputEntry {
    private final String key;
    private final String value;

    public RadioInputEntry(String key, String value) {
      this.key = key;
      this.value = value;
    }

    public String getKey() {
      return key;
    }

    public String getValue() {
      return value;
    }
  }



  public List<RadioInputEntry> getEntries() {
    return entries;
  }


}
