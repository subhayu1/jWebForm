package jwebform.element;

import java.util.LinkedHashMap;

import jwebform.element.structure.ElementResult;
import jwebform.element.structure.RenderInfos;
import jwebform.element.structure.TabIndexAwareElement;
import jwebform.element.structure.Validateable;
import jwebform.env.Request;
import jwebform.validation.ValidationResult;
import jwebform.validation.Validator;
import jwebform.view.StringUtils;
import jwebform.view.Tag;
import jwebform.view.TagAttributes;

public class TextInput implements TabIndexAwareElement, Validateable {
	
	final private String name; //
	// TBD: Does it make sense to introduce a Label class here?
	final private String label; //
	
	final private String value;

	final private String helptext; //
	
	final private Validator validator;	//
	
	final private ValidationResult validationResult;
	
	final private String placeholder; //
	
	final private String formId;
	
	public TextInput(String formId, String name, Request request, String label, String initialValue, String helptext, String placeholder, Validator validator) {
		this.name = name;
		this.label = label;
		this.helptext = helptext;
		this.validator = validator;
		this.formId = formId+"-";
		this.value = this.setupValue(request, initialValue);
		this.placeholder = placeholder;
		this.validationResult = this.validate(request);
	}


	@Override
	public String getValue() {
		return value;
	}


	@Override
	public ElementResult getHtml(RenderInfos renderInfos) {
		ValidationResult validationResultToWorkWith = renderInfos.getOverrideValidationResult()==ValidationResult.undefined()?validationResult:renderInfos.getOverrideValidationResult();
		
		String errorMessage = "";
		Tag wrapper = new Tag("div", "class", "form-group");
		if (validationResultToWorkWith != ValidationResult.undefined() && validationResultToWorkWith.isValid) {
			wrapper.getTagAttributes().addToAttribute("class", " has-success");	
		}
		if (validationResultToWorkWith != ValidationResult.undefined() && !validationResultToWorkWith.isValid) {
			wrapper.getTagAttributes().addToAttribute("class", " has-error");
			errorMessage = "Problem: " + validationResultToWorkWith.getMessage() + "<br>";
		}
		TagAttributes labelTagAttr = new TagAttributes("for", formId+name);
		Tag labelTag = new Tag("label", labelTagAttr, label+":");

		LinkedHashMap<String, String> attrs = new LinkedHashMap<>();
		attrs.put("tabindex", Integer.toString(renderInfos.getTabIndex()));
		attrs.put("type", "text");
		attrs.put("name", formId + name);
		attrs.put("value", value);
		
		if (!StringUtils.isEmpty(placeholder)) {
			attrs.put("placeholder", placeholder);
		}

		String helpHTML = "";
		if (!StringUtils.isEmpty(helptext)) {
			TagAttributes helpAttributes = new TagAttributes();
			helpAttributes.addToAttribute("id", "helpBlock-" + name);
			helpAttributes.addToAttribute("class", "help-block");
			Tag help = new Tag("span",helpAttributes, helptext);
			helpHTML = help.getComplete();
			attrs.put("aria-describedby", "helpBlock-" + name);
		}

		
		
		TagAttributes inputTagAttr = new TagAttributes(attrs);
		Tag inputTag = new Tag("input", inputTagAttr);
		ElementResult result = new ElementResult(name, wrapper.getStartHtml() +errorMessage+ labelTag.getComplete() + inputTag.getStartHtml()+ helpHTML + wrapper.getEndHtml() +"\n");
		return result;
	}

	@Override
	public ValidationResult getValidationResult() {
		return validationResult;
		
	}


	@Override
	public int getTabIndexIncrement() {
		return 1;
	}


	private String setupValue(Request request, String initialValue){
		if (request.getParameter(formId + name) != null) {
			return request.getParameter(formId+name);
		}
		return initialValue;
	}
	
	private ValidationResult validate(Request request) {
		if (request.getParameter(formId+name) != null) {
			return validator.validate(this, getValue());
		}
		return ValidationResult.undefined();
	}


	
	
	
}
