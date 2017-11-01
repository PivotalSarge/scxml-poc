package io.pivotal.prototype.scxml;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.xml.stream.XMLReporter;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.scxml2.PathResolver;
import org.apache.commons.scxml2.env.SimpleXMLReporter;
import org.apache.commons.scxml2.env.URLResolver;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.CustomAction;
import org.apache.commons.scxml2.model.Log;
import org.apache.commons.scxml2.model.ModelException;
import org.apache.commons.scxml2.model.SCXML;

public class Main extends ArgumentInvoker {
  public static void main(String[] args) {
    Main object = new Main();
    invoke(args, object);

    System.exit(0);
  }

  public void write(String text) {
    System.out.println(text);
  }

  public void increment(Integer i) {
    System.out.println("i=" + (i + 1));
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
      }
      else {
        System.err.println("Reading " + url + " failed!");
      }
    } catch (IOException | ModelException | XMLStreamException e) {
      e.printStackTrace(System.err);
    }
  }

  public void execute() {
//    try {
//      XMLReporter reporter = new SimpleXMLReporter();
//      SCXML scxml = new SCXML();
//      if (scxml != null) {
//    SCXMLExecutor exec = new SCXMLExecutor(<Evaluator>,
//                       <EventDispatcher>, <ErrorReporter>);
//    exec.setStateMachine(<SCXML>);
//    exec.addListener(<SCXML>, <SCXMLListener>);
//    exec.setRootContext(<Context>);
//    exec.go();
//      }
//    } catch (IOException | ModelException | XMLStreamException e) {
//      e.printStackTrace(System.err);
//    }
  }
}
