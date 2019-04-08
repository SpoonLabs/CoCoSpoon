package instrumenting;

import java.util.Map;
import java.util.Optional;
import java.util.TimerTask;

public class _TextView extends TimerTask {
  private static final char TEXTUAL_ARRAY_CHAR = '#';

  private static final char TEXTUAL_ARRAY_SIZE = 20;

  @Override
  public void run() {
    displayResultPerPackageInConsole(Optional.empty());
  }

  private void displayResultPerPackageInConsole(Optional<String> optionalPackageName) {
    for (Map.Entry<String, Map<Integer, Boolean>> entry : _Instrumenting.lines.entrySet()) {
      Float nbLineExecuted = 0.0F;
      Float nbLineTotal = 0.0F;
      for (boolean isExecuted : entry.getValue().values()) {
        if (isExecuted) {
          nbLineExecuted++;
        }
      }
      nbLineTotal += entry.getValue().size();
      Float covergeRatio = nbLineExecuted / nbLineTotal;
      String arrayTab = "";
      int nbArrayChar = ((int) ((TEXTUAL_ARRAY_SIZE) * covergeRatio));
      for (int i = 0; i < nbArrayChar; i++)
        arrayTab += TEXTUAL_ARRAY_CHAR;
      System.out.format("%-80s[%-20s] %.2f%%%n", entry.getKey(), arrayTab, (covergeRatio * 100));
    }
  }

  private String getLongestCommonPrefix(String[] strings) {
    if ((strings.length) == 0) {
      return "";
    }
    for (int prefixLen = 0; prefixLen < (strings[0].length()); prefixLen++) {
      char c = strings[0].charAt(prefixLen);
      for (int i = 1; i < (strings.length); i++) {
        if ((prefixLen >= (strings[i].length())) || ((strings[i].charAt(prefixLen)) != c)) {
          return strings[i].substring(0, prefixLen);
        }
      }
    }
    return strings[0];
  }

}
