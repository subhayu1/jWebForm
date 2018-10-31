package jwebform.spring;

import java.util.function.BiConsumer;

import jwebform.FormResult;
import jwebform.env.Env;
import jwebform.env.EnvBuilder;
import jwebform.env.Request;
import jwebform.env.SessionGet;
import jwebform.env.SessionSet;
import jwebform.integration.Bean2Form;
import jwebform.spring.internal.InternalFormRunner;
import jwebform.themes.FormRenderer;

// Container, that holds a jWebForm Form (or a normal bean) and provides a facade to jwebform
// objects
// typically this will be filled by the framework
public class JWebForm {
  private final Env env;
  private final BiConsumer<String, Object> model;
  private final FormRunnerConfig formRunnerConfig;
  private final InternalFormRunner formRunner = new InternalFormRunner();

  public JWebForm(Request request, SessionGet sessionGet, SessionSet sessionSet,
      BiConsumer<String, Object> model, FormRunnerConfig formRunnerConfig) {
    this.env = new EnvBuilder().of(request, sessionGet, sessionSet);
    this.model = model;
    this.formRunnerConfig = formRunnerConfig;
  }

  public FormResult run(Object formOrBean) {
    return formRunner.run(formOrBean, env, model, formRunnerConfig);
  }


}
