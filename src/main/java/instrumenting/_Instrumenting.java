package instrumenting;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class _Instrumenting {

  public static String TMP_FILE_NAME;

  public static Map<String, Set<_Line>> lines = new HashMap<String, Set<_Line>>();

  public static void addInstrumentedClass(String qualifiedName) {
    if (lines.get(qualifiedName) == null) {
      lines.put(qualifiedName, new TreeSet<_Line>());
    }
  }

  public static void addInstrumentedStatement(String qualifiedName, int position) {
    lines.get(qualifiedName).add(new _Line(position));
  }

  public static void isPassedThrough(String qualifiedName, final _Line _line) {
    if (lines.isEmpty()) {
      init();
    }
    _Line insideMap = lines.get(qualifiedName).parallelStream().filter(line -> line.getPosition() == _line.getPosition()).findFirst().orElse(null);
    if (insideMap != null && !insideMap.isExecuted()) {
      insideMap.setExecuted(true);
      System.out.println(qualifiedName + ":" + insideMap.getPosition() + " has been executed!");
    }
  }

  private static void init() {
    try {
      FileInputStream fin = new FileInputStream(new File(TMP_FILE_NAME));
      ObjectInputStream ois = new ObjectInputStream(fin);
      lines = (Map<String, Set<_Line>>) ois.readObject();
      ois.close();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

}
