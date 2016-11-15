package instrumenting;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

public class _Instrumenting {

  public static String TMP_FILE_NAME;

  public static String CURRENT_DIR;

  public static Map<String, Map<Integer, Boolean>> lines = new HashMap<String, Map<Integer, Boolean>>();

  public static void addInstrumentedClass(String qualifiedName) {
    if (lines.get(qualifiedName) == null) {
      lines.put(qualifiedName, new HashMap<Integer, Boolean>());
    }
  }

  public static void addInstrumentedStatement(String qualifiedName, int position) {
    lines.get(qualifiedName).put(position, false);
  }

  public static void isPassedThrough(String qualifiedName, final int position) {
    if (lines.isEmpty()) {
      init();
    }

    lines.get(qualifiedName).put(position, true);
  }

  @SuppressWarnings(value = "unchecked")
  private static void init() {
    try {
      FileInputStream fin = new FileInputStream(new File(_Instrumenting.TMP_FILE_NAME));
      ObjectInputStream ois = new ObjectInputStream(fin);
      _Instrumenting.lines = ((Map<String, Map<Integer, Boolean>>) (ois.readObject()));
      ois.close();

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
