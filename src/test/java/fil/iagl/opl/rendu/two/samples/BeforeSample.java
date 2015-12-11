package fil.iagl.opl.rendu.two.samples;

public class BeforeSample {

  public static void main(String[] args) {
    for (int i = 0; i < args.length; i++) {
      continue;
    }

    switch (args.length) {
      case 1:
        break;
    }

    throw new RuntimeException();

  }

  public int nonVoidMethod() {
    return 1;
  }
}
