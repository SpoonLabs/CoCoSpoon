package instrumenting;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

public class _Instrumenting {

  public static String CURRENT_DIR;

  /** a map of qualifiedName -> line number -> execution */
  public static Map<String, Map<Integer, Boolean>> lines = new HashMap<String, Map<Integer, Boolean>>();

  public static void addInstrumentedClass(String qualifiedName) {
    registerClass(qualifiedName);
  }

  public static void addInstrumentedStatement(String qualifiedName, int position) {
    registerClass(qualifiedName);
    lines.get(qualifiedName).put(position, false);
  }

  public static void isPassedThrough(String qualifiedName, final int position) {
    registerClass(qualifiedName);
    lines.get(qualifiedName).put(position, true);
  }

  // private method
  private static void registerClass(String qualifiedName) {
    if (lines.get(qualifiedName) == null) {
      lines.put(qualifiedName, new HashMap<Integer, Boolean>());
    }
  }
}
