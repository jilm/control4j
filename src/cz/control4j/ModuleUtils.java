package cz.control4j;

import cz.lidinsky.tools.CommonException;
import cz.lidinsky.tools.ExceptionCode;
import java.util.Arrays;

public class ModuleUtils {

  /**
   * It search for a class annotation Input which alias property is equal to
   * the given key parameter. The index property of the found annotation is
   * returned.
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

}
