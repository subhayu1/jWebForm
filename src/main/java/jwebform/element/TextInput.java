package jwebform.element;

import java.util.LinkedHashMap;

import com.coverity.security.Escape;

import jwebform.element.structure.TabIndexAwareElement;
import jwebform.element.structure.Validateable;
import jwebform.view.Tag;
import jwebform.view.TagAttributes;

public class TextInput implements TabIndexAwareElement, Validateable {
	
	String name;

	public TextInput(String name, String label, String value, String helptext) {
		super();
		this.name = name;
		this.label = label;
		this.value = value;
		this.helptext = helptext;
	}

	// TBD: Does it make sense to introduce a Label class here?
	String label;
	
	String value;

	String helptext;
	
	int tabIndex=0;

	@Override
	public String getHtml() {
		TagAttributes labelTagAttr = new TagAttributes("for", "form-id-"+name);
		Tag labelTag = new Tag("label", labelTagAttr, label+":");

		LinkedHashMap<String, String> attrs = new LinkedHashMap<>();
		attrs.put("tabindex", Integer.toString(tabIndex));
		attrs.put("type", "text");
		attrs.put("name", name);
		attrs.put("value", Escape.html(value));
		TagAttributes inputTagAttr = new TagAttributes(attrs);
		Tag inputTag = new Tag("input", inputTagAttr);
		
		return labelTag.getComplete() + inputTag.getStartHtml();
	}

	@Override
	public void overwriteValidationResult() {
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public int feedTabIndex(int currentTabIndex) {
		tabIndex = currentTabIndex;
		return tabIndex;
	}
	
	
	
}
