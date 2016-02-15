package cz.control4j;

import static cz.lidinsky.tools.CollectionUtils.getSingleton;
import cz.lidinsky.tools.CommonException;
import cz.lidinsky.tools.ExceptionCode;
import cz.lidinsky.tools.Tools;
import cz.lidinsky.tools.reflect.Setter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class ModuleUtils {

  /**
   * It search for a class Input annotation whose alias property is equal to
   * the given key parameter. The index property of the annotation that was
   * found is returned.
   *
   * @param moduleClass
   *
   * @param key
   *            an identifier of the input
   *
   * @return an index to the input array
   *
   * @throws CommonException
   *            if the given key is not supported by this module
   *
   * @see Input
   */
  public static int getInputIndex(
      Class<? extends Module> moduleClass, String key) {
    Input[] inputAnno = moduleClass.getDeclaredAnnotationsByType(Input.class);
    return Arrays.stream(inputAnno)
      .filter(anno -> anno.alias().equals(key))
      .mapToInt(anno -> anno.index())
      .findAny()
      .orElseThrow(
          () -> new CommonException()
          .setCode(ExceptionCode.NO_SUCH_ELEMENT)
          .set("message", "There is not an input with given key!")
          .set("key", key));
  }

  /**
   * Sets the value of the given property of the given module. The property is
   * identified by the key. This method finds method that is annotated by the
   * setter annotation with the equal identifier.
   *
   * @param module
   *            module which property should be set
   *
   * @param key
   *            identifier of the property
   *
   * @param value
   *            the value to be set
   */
  public static void setProperty(Module module, String key, String value) {
    try {
    // find appropriate method
    Method setter = getSetterMethod(module.getClass(), key);
    // recognize the right datatype
    Class parameterType = getSingleton(setter.getParameterTypes());
    // convert value
    Object parsedValue = Tools.parse(parameterType, value);
    // call the setter method
    setter.invoke(module, parsedValue);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new CommonException()
          .setCause(e)
          .set("message", "I was not able to set some module property!")
          .set("module", module)
          .set("key", key)
          .set("value", value);
    }
  }

  /**
   * Finds methods of the given class that are annotated by the setter
   * annotation with value which is equal to the given key.
   *
   * @param _class
   * @param key
   * @return
   */
  protected static Method getSetterMethod(Class _class, String key) {
    Method[] methods = _class.getDeclaredMethods();
    Collection<Method> filtered = Arrays.stream(methods)
        .filter(method -> cz.lidinsky.tools.Tools.equals(method.getAnnotation(Setter.class).value(), key))
        .collect(Collectors.toSet());
    return getSingleton(filtered);
  }

  /**
   * Creates and returns instance of the module with given class name.
   *
   * @param className
   *            class name of the required module implementation
   *
   * @return created instance of the object with given class name
   */
  public static Module createModuleInstance(String className) {
    try {
      Class<Module> moduleClass = (Class<Module>) Class.forName(className);
      Module module = moduleClass.newInstance();
      return module;
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      throw new CommonException()
          .setCause(e)
          .set("message", "Couldn't create instance of the module with given class name!")
          .set("class name", className);
    } catch (ClassCastException e) {
      throw new CommonException()
          .setCause(e)
          .set("message", "The class with given name is not descendant of the Module class!")
          .set("class name", className);
    } catch (NullPointerException e) {
      throw new CommonException()
          .setCause(e)
          .set("message", "Could not create module instance because of null class name!");
    }
  }

}
