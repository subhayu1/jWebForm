package jwebform.element;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jwebform.element.structure.Element;
import jwebform.element.structure.ElementResult;
import jwebform.element.structure.HTMLProducer;
import jwebform.element.structure.PrepareInfos;
import jwebform.element.structure.ProducerInfos;
import jwebform.element.structure.StaticRenderData;
import jwebform.validation.ValidationResult;
import jwebform.validation.Validator;
import jwebform.validation.criteria.Criteria;
import jwebform.view.StringUtils;

/**
 * Date-Input with simple text-fields
 * 
 * @author jochen
 *
 */
public class TextDateInput implements Element {

  final private String name;

  final private Validator validator;

  final private LocalDate initialValue;
  final private OneFieldDecoration decoration;

  final private TextInput day;
  final private TextInput month;
  final private TextInput year;

  public TextDateInput(String name, OneFieldDecoration decoration, LocalDate initialValue,
      Validator validator) {
    this.name = name;
    this.validator = validator;
    this.initialValue = initialValue;
    this.decoration = decoration;

    Validator numberValidator = new Validator(Criteria.number());

    this.day = new TextInput(name + "_day", new OneFieldDecoration("Day"),
        String.valueOf(initialValue.getDayOfMonth()), numberValidator);
    this.month = new TextInput(name + "_month", new OneFieldDecoration("Month"),
        String.valueOf(initialValue.getMonthValue()), numberValidator);
    this.year = new TextInput(name + "_year", new OneFieldDecoration("Year"),
        String.valueOf(initialValue.getYear()), numberValidator);

  }



  public LocalDate getDateValue() {
    return null;
  }

  @Override
  public ElementResult prepare(PrepareInfos renderInfos) {
    ElementResult dayResult = day.prepare(renderInfos);
    PrepareInfos monthRenderInfos =
        renderInfos.cloneWithNewTabIndexIncrease(dayResult.getTabIndexIncrement());
    ElementResult monthResult = month.prepare(monthRenderInfos);
    PrepareInfos yearRenderInfos =
        monthRenderInfos.cloneWithNewTabIndexIncrease(monthResult.getTabIndexIncrement());
    ElementResult yearResult = year.prepare(yearRenderInfos);

    List<ElementResult> childs = new ArrayList<>();
    childs.add(dayResult);
    childs.add(monthResult);
    childs.add(yearResult);

    LocalDate value = initialValue;
    ValidationResult validationResult = ValidationResult.ok();
    try {
      value = this.setupValue(this.initialValue, dayResult.getValue(), monthResult.getValue(),
          yearResult.getValue());
    } catch (DateTimeException | NumberFormatException e) {
      validationResult = ValidationResult.fail("jformchecker.wrong_date_format");
    }

    HTMLProducer renderer =
        renderInfos.getTheme().getHtmlProducer().get("jwebform.element.TextDateInput");
    if (renderer == null) {
      renderer = new TextDateInputRenderer();
    }

    ElementResult result = new ElementResult(name, renderer, validationResult,
        value.format(DateTimeFormatter.ISO_DATE), 3, this, "jwebform.element.TextDateInput",
        childs, new TextDateData(decoration));
    return result;
  }

  public class TextDateData implements StaticRenderData {
    public final OneFieldDecoration decoration;

    public TextDateData(OneFieldDecoration decoration) {
      this.decoration = decoration;
    }
    
  }
  
  public class TextDateInputRenderer implements HTMLProducer {

    @Override
    public String getHTML(ProducerInfos pi) {
      String errorMessage = "";
      if (pi.getVr() != ValidationResult.undefined() && !pi.getVr().isValid) {
        errorMessage = "Problem: " + pi.getVr().getMessage() + "<br>";
      }
      ElementResult dayResult = pi.getChilds().get(0);
      ElementResult monthResult = pi.getChilds().get(1);
      ElementResult yearResult = pi.getChilds().get(2);
      String html = decoration.getLabel() + "<br/>" + errorMessage
          + dayResult.getHtmlProducer()
              .getHTML(new ProducerInfos(pi.getFormId(), pi.getTabIndex(),
                  dayResult.getValidationResult(), null, dayResult.getStaticRenderData()))

          + monthResult.getHtmlProducer()
              .getHTML(new ProducerInfos(pi.getFormId(), pi.getTabIndex() + 1,
                  monthResult.getValidationResult(), null, monthResult.getStaticRenderData()))
          + yearResult.getHtmlProducer().getHTML(new ProducerInfos(pi.getFormId(),
              pi.getTabIndex() + 2, yearResult.getValidationResult(), null, yearResult.getStaticRenderData()))
          + "<br>" + decoration.getHelptext()+"<-- internal renderer -->";
      return html;
    }

  }

  // May throw execption!!
  private LocalDate setupValue(LocalDate initialValue, String dayStr, String monthStr,
      String yearStr) {
    if (StringUtils.isEmpty(dayStr) && StringUtils.isEmpty(monthStr)
        && StringUtils.isEmpty(yearStr)) {
      return initialValue; // TODO: maybe this is wrong: if nothing is entered, it can't be the
                           // initial value!
    }
    int day = getDefaultValueFromRequest(dayStr);
    int month = getDefaultValueFromRequest(monthStr);
    int year = getDefaultValueFromRequest(yearStr);
    return LocalDate.of(year, month, day);
  }


  private int getDefaultValueFromRequest(String input) {
    return Integer.parseInt(input);
  }

}
