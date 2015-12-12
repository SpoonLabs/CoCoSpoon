package instrumenting;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class _Instrumenting {

  private static final char TEXTUAL_ARRAY_CHAR = '#';
  private static final char TEXTUAL_ARRAY_SIZE = 20;

  public static String TMP_FILE_NAME;

  public static Map<String, Map<Integer, Boolean>> lines = new HashMap<String, Map<Integer, Boolean>>();

  public static void addInstrumentedClass(String qualifiedName) {
    if (lines.get(qualifiedName) == null) {
      System.out.printf("Intrumenting %s ...\n", qualifiedName);
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

  @SuppressWarnings("unchecked")
  private static void init() {
    try {
      FileInputStream fin = new FileInputStream(new File(TMP_FILE_NAME));
      ObjectInputStream ois = new ObjectInputStream(fin);
      lines = (Map<String, Map<Integer, Boolean>>) ois.readObject();
      ois.close();

      // KEEP GRAPH OPEN
      Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
        public void run() {
          while (true) {
          }
        }
      }, "Shutdown-thread"));

      // BARCHART VIEW

      new Thread(new Runnable() {

        @Override
        public void run() {
          _BarChart.run();
        }
      }).start();

      // TEXT VIEW

      // new Timer().schedule(new TimerTask() {
      // @Override
      // public void run() {
      // _Instrumenting.displayResultPerPackageInConsole(null);
      // }
      // }, 0, 250);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  public static void displayResultPerPackageInConsole(String packageName) {
    String commonPackage;
    if (packageName == null) {
      commonPackage = getLongestCommonPrefix(lines.keySet().toArray(new String[lines.keySet().size()]));
      commonPackage = commonPackage.substring(0, commonPackage.length() - 1);
    } else {
      commonPackage = packageName;
    }

    Float nbLineExecuted = 0f;
    Float nbLineTotal = 0f;
    for (Entry<String, Map<Integer, Boolean>> entry : lines.entrySet()) {
      if (entry.getKey().startsWith(commonPackage)) {
        for (boolean isExecuted : entry.getValue().values()) {
          if (isExecuted) {
            nbLineExecuted++;
          }
        }
        nbLineTotal += entry.getValue().size();
      }
    }

    Float covergeRatio = nbLineExecuted / nbLineTotal;
    // System.out.printf("%s -> %.2f%%\n", commonPackage, covergeRatio);
    String arrayTab = "";
    int nbArrayChar = (int) (TEXTUAL_ARRAY_SIZE * covergeRatio);
    for (int i = 0; i < nbArrayChar; i++)
      arrayTab += TEXTUAL_ARRAY_CHAR;
    System.out.format("%-40s[%-20s] %.2f%%%n", commonPackage, arrayTab, covergeRatio * 100);

    Set<String> subDirectPackage = new HashSet<String>();
    for (Entry<String, Map<Integer, Boolean>> entry : lines.entrySet()) {
      if (entry.getKey().substring(0, entry.getKey().lastIndexOf(".")).contains(commonPackage)
        && entry.getKey().substring(0, entry.getKey().lastIndexOf(".")).split("\\.").length > commonPackage.split("\\.").length) {
        subDirectPackage.add(String.format("%s.%s", commonPackage, entry.getKey().split("\\.")[commonPackage.split("\\.").length]));
      }
    }
    if (!subDirectPackage.isEmpty()) {
      for (String subPackage : subDirectPackage) {
        displayResultPerPackageInConsole(subPackage);
      }
    }
  }

  public static void displayResultPerClassInConsole() {
    for (Entry<String, Map<Integer, Boolean>> entry : lines.entrySet()) {
      Float nbLineExecuted = 0f;
      for (boolean isExecuted : entry.getValue().values()) {
        if (isExecuted) {
          nbLineExecuted++;
        }
      }
      Float covergeRatio = nbLineExecuted / entry.getValue().size();
      System.out.printf("%s -> %.2f%%\n", entry.getKey(), covergeRatio);
    }
  }

  private static String getLongestCommonPrefix(String[] strings) {
    if (strings.length == 0) {
      return ""; // Or maybe return null?
    }

    for (int prefixLen = 0; prefixLen < strings[0].length(); prefixLen++) {
      char c = strings[0].charAt(prefixLen);
      for (int i = 1; i < strings.length; i++) {
        if (prefixLen >= strings[i].length() ||
          strings[i].charAt(prefixLen) != c) {
          // Mismatch found
          return strings[i].substring(0, prefixLen);
        }
      }
    }
    return strings[0];
  }

}
