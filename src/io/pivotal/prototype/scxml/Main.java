package io.pivotal.prototype.scxml;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.xml.stream.XMLReporter;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.scxml2.Context;
import org.apache.commons.scxml2.ErrorReporter;
import org.apache.commons.scxml2.Evaluator;
import org.apache.commons.scxml2.EventDispatcher;
import org.apache.commons.scxml2.PathResolver;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.SCXMLListener;
import org.apache.commons.scxml2.env.SimpleContext;
import org.apache.commons.scxml2.env.SimpleDispatcher;
import org.apache.commons.scxml2.env.SimpleErrorReporter;
import org.apache.commons.scxml2.env.SimpleSCXMLListener;
import org.apache.commons.scxml2.env.SimpleXMLReporter;
import org.apache.commons.scxml2.env.URLResolver;
import org.apache.commons.scxml2.env.javascript.JSContext;
import org.apache.commons.scxml2.env.javascript.JSEvaluator;
import org.apache.commons.scxml2.env.minimal.MinimalEvaluator;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.CustomAction;
import org.apache.commons.scxml2.model.Final;
import org.apache.commons.scxml2.model.Log;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.commons.scxml2.model.SCXML;
import org.apache.commons.scxml2.model.SimpleTransition;
import org.apache.commons.scxml2.model.Transition;

public class Main extends ArgumentInvoker {
  public static void main(String[] args) {
    int status = 0;
    try {
      new Main().invoke(args);
    } catch (Exception e) {
      status = 1;
      e.printStackTrace(System.err);
    }
    System.exit(status);
  }

  public void execute() {
    try {
      SCXML stateMachine = new SCXML();
      final Final state = new Final();
      stateMachine.addChild(state);
      SimpleTransition transition = new SimpleTransition();
      transition.setNext(state.getId());
      stateMachine.setInitialTransition(new SimpleTransition());
      Evaluator evaluator = new JSEvaluator();
      EventDispatcher eventDispatcher = new SimpleDispatcher();
      ErrorReporter errorReporter = new SimpleErrorReporter();
      SCXMLExecutor exec = new SCXMLExecutor(evaluator, eventDispatcher, errorReporter);
      exec.setStateMachine(stateMachine);
      SCXMLListener listener = new SimpleSCXMLListener();
      exec.addListener(stateMachine, listener);
      Context context = new JSContext();
      exec.setRootContext(context);
      exec.go();
    } catch (ModelException e) {
      e.printStackTrace(System.err);
    }
  }

  public void read(String url) {
    System.out.println("url=" + url);
    try {
      XMLReporter reporter = new SimpleXMLReporter();
      PathResolver resolver = new URLResolver(new URL("http://pivotal.io/"));
      List<CustomAction> actions = Arrays
          .asList(new CustomAction("http://pivotal.io/scxml", "log", Log.class));
      // Through a Configuration object the reading of a SCXML document can be configured and customized.
      SCXMLReader.Configuration
          configuration =
          new SCXMLReader.Configuration(reporter, resolver, actions);
      SCXML scxml = SCXMLReader.read(new URL(url), configuration);
      if (scxml != null) {
        System.out.println("Reading " + url + " succeeded.");
      } else {
        System.err.println("Reading " + url + " failed!");
      }
    } catch (IOException | ModelException | XMLStreamException e) {
      e.printStackTrace(System.err);
    }
  }

  public void increment(Integer i) {
    System.out.println("i=" + (i + 1));
  }

  public void write(String text) {
    System.out.println(text);
  }

  @Override
  protected String getDefaultMethodName() {
    return "execute";
  }
}
