package fil.iagl.opl.rendu.two;

import spoon.Launcher;

/**
 * Hello world!
 *
 */
public class App {
  private static final String INSTRUMENT_SOURCE_FOLDER = "src/main/java/instrumenting";

  /**
   * TODO : Reste a fixer les throw / continue etc
   */
  public static void main(String[] args) throws Exception {
    // String inputDirectory = "C:\\Users\\RMS\\Documents\\workspace-sts-3.7.0.RELEASE\\OPL_Sample\\commons-lang";
    // String outputDirectory = "C:\\Users\\RMS\\Documents\\workspace-sts-3.7.0.RELEASE\\OPL_Sample\\commons-lang_instrumented";

    String inputDirectory = "C:\\Users\\RMS\\Documents\\workspace-sts-3.7.0.RELEASE\\OPL-Rendu2\\src\\test\\java\\fil\\iagl\\opl\\rendu\\two\\samples\\Sample.java";

    // File inputFile = new File(inputDirectory);
    // File outputFile = new File(outputDirectory);

    // if (outputFile.exists()) {
    // FileUtils.forceDelete(outpCutFile);
    // }

    // FileUtils.copyDirectory(inputFile, outputFile);

    String[] spoonArgs = {
      "-i", inputDirectory + ";" + INSTRUMENT_SOURCE_FOLDER,
      // "-i", inputDirectory + "\\src\\main\\java" + ";" + INSTRUMENT_SOURCE_FOLDER,
      // "-o", outputDirectory + "\\src\\main\\java",
      "-p", "fil.iagl.opl.rendu.two.processors.AddWatcherProcessor",
      "--with-imports"
    };

    Launcher.main(spoonArgs);
  }

}
