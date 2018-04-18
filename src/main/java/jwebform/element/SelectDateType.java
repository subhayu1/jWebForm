package jwebform.element;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jwebform.element.structure.CommonSelects;
import jwebform.element.structure.DateTypeHelper;
import jwebform.element.structure.Decoration;
import jwebform.element.structure.ElementContainer;
import jwebform.element.structure.ElementResult;
import jwebform.element.structure.GroupType;
import jwebform.env.Env.EnvWithSubmitInfo;
import jwebform.validation.FormValidator;
import jwebform.validation.Validator;
import jwebform.validation.criteria.Criteria;

/**
 * Date-Input with dropdown selects
 * 
 * @author jochen
 *
 */
public class SelectDateType implements GroupType {


  final private ElementContainer day;
  final private ElementContainer month;
  final private ElementContainer year;

  final private DateTypeHelper helper;

  public SelectDateType(String name, LocalDate initialValue, int yearStart, int yearEnd) {
    Validator numberValidator = new Validator(Criteria.number());
    this.day = new SelectType(name + "_day", String.valueOf(initialValue.getDayOfMonth()),
        CommonSelects.build().buildDays()).of(numberValidator, new Decoration("Day"));
    this.month = new SelectType(name + "_month", String.valueOf(initialValue.getMonthValue()),
        CommonSelects.build().buildMonths()).of(numberValidator, new Decoration("Month"));
    this.year = new SelectType(name + "_year", String.valueOf(initialValue.getYear()),
        CommonSelects.build().getYears(yearStart, yearEnd)).of(numberValidator,
            new Decoration("Year"));
    helper = new DateTypeHelper(day, month, year, initialValue, name);
  }



  @Override
  public List<ElementContainer> getChilds() {
    return Arrays.asList(day, month, year);
  }


  @Override
  public ElementResult process(EnvWithSubmitInfo env, Map<ElementContainer, ElementResult> childs) {
    return helper.processDateVal(env, childs, "select date");
  }


  @Override
  public List<FormValidator> getValidators(ElementContainer source) {
    return helper.getValidators(source);
  }


}
