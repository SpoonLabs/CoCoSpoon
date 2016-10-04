package fil.iagl.opl.cocospoon.tools;


import com.sanityinc.jargs.CmdLineParser;
import com.sanityinc.jargs.CmdLineParser.Option;


public class Params {

  private String inputSource;
  private String outputSource;
  private String classpath;

  public void handleArgs(String[] args) {
    CmdLineParser parse = new CmdLineParser();

    Option<String> inputSourceOption = parse.addStringOption('i', "input-path");
    Option<String> outputSourceOption = parse.addStringOption('o', "output-path");
    Option<String> classPathOption = parse.addStringOption('c', "classpath");

    try {
      parse.parse(args);
    } catch (CmdLineParser.OptionException e) {
      System.err.println(e.getMessage());
      printUsage();
      System.exit(2);
    }

    inputSource = parse.getOptionValue(inputSourceOption);
    outputSource = parse.getOptionValue(outputSourceOption);
    classpath = parse.getOptionValue(classPathOption);

    if (inputSource == null || outputSource == null) {
      printUsage();
      System.exit(2);
    }
  }

  private void printUsage() {
    System.err.println("Usage: CoCoSpoon\n"
      + " -i, --input-path\t\tpath_program\n"
      + " -o, --output-path\t\tinstrumented sources destination\n"
      + "[-c, --classpath]\t\tclasspath\n"
      + "[-v, --view-type]\t\tview type : | OVERALL | INTERACTIVE | TEXT \n");
  }

  public String getClasspath() {
    return classpath;
  }

  public void setClasspath(String classpath) {
    this.classpath = classpath;
  }

  public String getInputSource() {
    return inputSource;
  }

  public String getOutputSource() {
    return outputSource;
  }

}
