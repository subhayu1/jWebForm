package jwebform.element;

import jwebform.element.structure.Element;
import jwebform.element.structure.ElementResult;
import jwebform.element.structure.HTMLProducer;
import jwebform.element.structure.RenderInfos;
import jwebform.validation.ValidationResult;

public class SimpleElement implements Element{

	@Override
	public ElementResult run(RenderInfos renderInfos) {
		return new ElementResult("simple", new SimpleElementRenderer(), 
				ValidationResult.ok(), "", 0);
	}

	public class SimpleElementRenderer implements HTMLProducer {
		@Override
		public String getHTML(ValidationResult vr, int tabIndex) {
			return "simple\n";
		}
		
	}

}
