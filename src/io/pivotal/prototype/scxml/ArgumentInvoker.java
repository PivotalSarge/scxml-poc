package io.pivotal.prototype.scxml;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class ArgumentInvoker {
  public static void invoke(String[] args, Object object)
      throws InvocationTargetException, IllegalAccessException, InstantiationException {
    invoke(new LinkedList<>(Arrays.asList(args)), object);
  }

  private static void invoke(Queue<String> arguments, Object object)
      throws IllegalAccessException, InvocationTargetException, InstantiationException {
    while (!arguments.isEmpty()) {
      final String name = arguments.remove();
      invoke(arguments, object, name);
    }
  }

  private static void invoke(Queue<String> arguments, Object object, String name)
      throws IllegalAccessException, InvocationTargetException, InstantiationException {
    final Method method = findMethod(object.getClass(), name);
    if (method != null) {
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
    }
  }

  private static Method findMethod(Class clazz, String name) {
    if (name != null) {
      for (Method method : clazz.getDeclaredMethods()) {
        if (name.equals(method.getName())) {
          return method;
        }
      }
    }
    return null;
  }

  protected String getDefaultMethodName() {
    return null;
  }

  public void invoke()
      throws InvocationTargetException, IllegalAccessException, InstantiationException {
    invoke(new String[0]);
  }

  public void invoke(String[] args)
      throws InvocationTargetException, IllegalAccessException, InstantiationException {
    if (0 < args.length) {
      invoke(new LinkedList<>(Arrays.asList(args)), this);
    } else {
      invoke(new LinkedList<>(), this, getDefaultMethodName());

    }
  }
}
