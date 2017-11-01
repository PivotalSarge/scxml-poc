package io.pivotal.prototype.scxml;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class ArgumentInvoker {
  public static void invoke(String[] args, Object object) {
    invoke(new LinkedList<>(Arrays.asList(args)), object);
  }

  protected void invoke(String[] args) {
    invoke(new LinkedList<>(Arrays.asList(args)), this);
  }

  private static void invoke(Queue<String> arguments, Object object) {
    while (!arguments.isEmpty()) {
      final String name = arguments.remove();
      for (Method method : object.getClass().getDeclaredMethods()) {
        if (name.equals(method.getName())) {
          try {
            if (0 < method.getParameterCount()) {
              Object[] parameters = new Object[method.getParameterCount()];
              int index = 0;
              for (Type type : method.getParameterTypes()) {
                if (!arguments.isEmpty()) {
                  final String parameter = arguments.remove();
                  try {
                    Constructor constructor =
                        ((Class) type).getConstructor(String.class);
                    parameters[index++] = constructor.newInstance(parameter);
                  } catch (NoSuchMethodException e) {
                    parameters[index++] = parameter;
                  }
                } else {
                  parameters[index++] = null;
                }
              }
              method.invoke(object, parameters);
            } else {
              method.invoke(object);
            }
          } catch (Exception e) {
            e.printStackTrace(System.err);
          }
          break;
        }
      }
    }
  }
}
