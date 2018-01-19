package jwebform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jwebform.element.structure.ElementContainer;
import jwebform.element.structure.ElementResult;
import jwebform.element.structure.ForceFileuploadMethod;
import jwebform.element.structure.ProducerInfos;
import jwebform.view.StartEndRenderer;
import jwebform.view.Theme;
import jwebform.view.theme.BootstrapTheme;

public final class View {

  private final Map<ElementContainer, ElementResult> elementResults;
  private final String formId;
  private final Theme theme;

  public View(String formId, Map<ElementContainer, ElementResult> elementResults, Theme theme) {
    this.formId = formId;
    this.elementResults = elementResults;
    this.theme = theme;
  }

  public View(String formId, Map<ElementContainer, ElementResult> elementResults) {
    this.formId = formId;
    this.elementResults = elementResults;
    this.theme = null;
  }

  public String getHtml() {
    return getHtml("POST", true);
  }

  // fulll version
  public String getHtml(String method, boolean html5Validation) {
    StringBuilder html = new StringBuilder();
    html.append(ensureThemeIsThere().getStart(formId, method, html5Validation,
        determineUploadTypeAutomatically()));
    int tabIndex = 1;
    ProducerInfos pi;
    for (Map.Entry<ElementContainer, ElementResult> entry : elementResults.entrySet()) {
      ElementResult elementResult = entry.getValue();
      ElementContainer container = entry.getKey();
      List<ProducerInfos> childs = createProducerInfoChilds(elementResult.getChilds(), tabIndex);
      pi = new ProducerInfos(formId, tabIndex, ensureThemeIsThere(), elementResult, container,
          childs);
      html.append(pi.getHtml());
      tabIndex += elementResult.getStaticElementInfo().getTabIndexIncrement();
    }

    html.append(ensureThemeIsThere().getEnd());
    return html.toString();
  }

  private List<ProducerInfos> createProducerInfoChilds(
      Map<ElementContainer, ElementResult> childs,
      int tabIndex) {
    List<ProducerInfos> listOfPis = new ArrayList<>(); // RFE: only, if childs is not empty!
    // RFE: This allows only one depth! It would be cooler, if we can do infinite depth
    childs.forEach((container, result) -> listOfPis
        .add(new ProducerInfos(formId, tabIndex, ensureThemeIsThere(), result, container)));
    return listOfPis;
  }

  private Theme ensureThemeIsThere() {
    if (theme == null) {
      return BootstrapTheme.instance();
    } else {
      return theme;
    }

  }

  private boolean determineUploadTypeAutomatically() {
    for (Map.Entry<ElementContainer, ElementResult> entry : elementResults.entrySet()) {
      if (entry.getKey().element instanceof ForceFileuploadMethod) {
        return true;
      } ;
    }
    return false;
  }


  ////////// For rendering within templates



  public List<String> getElementNames() {
    List<String> names = new ArrayList<>();
    elementResults.forEach((k, v) -> names.add(v.getStaticElementInfo().getName()));
    return names;
  }

  public List<ProducerInfos> getUnrenderedElements() {
    List<ProducerInfos> elements = new ArrayList<>();
    int tabIndex = 1;
    for (Map.Entry<ElementContainer, ElementResult> entry : elementResults.entrySet()) {
      ElementResult elementResult = entry.getValue();
      ProducerInfos pi = new ProducerInfos(formId, tabIndex, ensureThemeIsThere(), elementResult,
          entry.getKey(), createProducerInfoChilds(elementResult.getChilds(), tabIndex));
      elements.add(pi);
      tabIndex += elementResult.getStaticElementInfo().getTabIndexIncrement();
    }
    return elements;
  }

  public List<DrawableElement> getDrawableElements() {
    List<DrawableElement> elements = new ArrayList<>();
    int tabIndex = 1;
    for (Map.Entry<ElementContainer, ElementResult> entry : elementResults.entrySet()) {
      ElementResult elementResult = entry.getValue();
      ProducerInfos pi = new ProducerInfos(formId, tabIndex, ensureThemeIsThere(), elementResult,
          entry.getKey(), createProducerInfoChilds(elementResult.getChilds(), tabIndex));
      elements.add(new DrawableElement(pi));
      tabIndex += elementResult.getStaticElementInfo().getTabIndexIncrement();
    }
    return elements;
  }



  public Map<String, ProducerInfos> getAllUnrenderedElements() {
    Map<String, ProducerInfos> elements = new LinkedHashMap<>();
    int tabIndex = 1;
    for (Map.Entry<ElementContainer, ElementResult> entry : elementResults.entrySet()) {
      ElementResult elementResult = entry.getValue();
      ProducerInfos pi =
          new ProducerInfos(formId, tabIndex, ensureThemeIsThere(), elementResult, entry.getKey());
      elements.put(elementResult.getStaticElementInfo().getName(), pi);
      tabIndex += elementResult.getStaticElementInfo().getTabIndexIncrement();
    }
    return elements;
  }



  public Map<String, RenderedElement> getElements() {
    Map<String, RenderedElement> elements = new LinkedHashMap<>();
    int tabIndex = 1;
    for (Map.Entry<ElementContainer, ElementResult> entry : elementResults.entrySet()) {
      ElementResult elementResult = entry.getValue();
      ProducerInfos pi =
          new ProducerInfos(formId, tabIndex, ensureThemeIsThere(), elementResult, entry.getKey());
      elements.put(elementResult.getStaticElementInfo().getName(),
          new RenderedElement(pi.getHtml(), pi, elementResult));
      tabIndex += elementResult.getStaticElementInfo().getTabIndexIncrement();
    }
    return elements;
  }

  public StartEndRenderer getStartEnd() {
    return new StartEndRenderer(formId, "POST", true, determineUploadTypeAutomatically());
  }

  // this is for simple template-engines like mustache
  public class DrawableElement {
    private final ProducerInfos producerInfos;
    private final Map<String, Boolean> elementNameInfo;

    public Map<String, Boolean> getElementNameInfo() {
      return elementNameInfo;
    }

    public DrawableElement(ProducerInfos producerInfos) {
      this.producerInfos = producerInfos;
      elementNameInfo = new HashMap<>();
      elementNameInfo.put(
          producerInfos.getElementTypeName().replaceAll("jwebform\\.element\\.", ""), Boolean.TRUE);
    }

    public ProducerInfos getProducerInfos() {
      return producerInfos;
    }

  }

  public class RenderedElement {
    private final String html;
    private final ProducerInfos producerInfos;
    private final ElementResult elementResult;

    public RenderedElement(String html, ProducerInfos producerInfos, ElementResult elementResult) {
      this.html = html;
      this.producerInfos = producerInfos;
      this.elementResult = elementResult;
    }

    public String getHtml() {
      return html;
    }

    public ProducerInfos getProducerInfos() {
      return producerInfos;
    }

    public ElementResult getElementResult() {
      return elementResult;
    }

  }


}
