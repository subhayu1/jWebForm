package jwebform.element.renderer.bootstrap;

import com.coverity.security.Escape;

import jwebform.element.TextAreaType;
import jwebform.element.structure.HTMLProducer;
import jwebform.element.structure.ProducerInfos;

public class BootstrapTextAreaRenderer implements HTMLProducer {

  @Override
  public String getHTML(ProducerInfos pi) {
    return pi.getTheme().getRenderer().renderInputComplex("textarea",
        Escape.html(pi.getElementResult().getValue()), pi,
        ((TextAreaType) pi.getElementResult().getSource()).oneValueElement.decoration);
  }

}
