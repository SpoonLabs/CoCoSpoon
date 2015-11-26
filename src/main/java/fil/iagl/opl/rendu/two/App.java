package fil.iagl.opl.rendu.two;

import java.io.File;

import org.apache.commons.io.FileUtils;

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
    String inputDirectory = "C:\\workspace\\OPL_Sample\\commons-lang";
    String outputDirectory = "C:\\workspace\\OPL_Sample\\commons-lang_instrumented";

    File inputFile = new File(inputDirectory);
    File outputFile = new File(outputDirectory);

    if (outputFile.exists()) {
      FileUtils.forceDelete(outputFile);
    }

    FileUtils.copyDirectory(inputFile, outputFile);

    String[] spoonArgs = {
      "-i", inputDirectory + "\\src\\main\\java" + ";" + INSTRUMENT_SOURCE_FOLDER,
      "-o", outputDirectory + "\\src\\main\\java",
      // "-i", "C:/workspace/OPL-Rendu2/src/test/java/fil/iagl/opl/rendu/two/samples" + ";" + INSTRUMENT_SOURCE_FOLDER,
      "-p", "fil.iagl.opl.rendu.two.processors.AddWatcherProcessor",
      "--with-imports"
    };

    Launcher.main(spoonArgs);
  }

}
