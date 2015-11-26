package fil.iagl.opl.rendu.two.samples;

public class Main {

  public static void main(String[] args) {
    Sample sample = new Sample(2, "foo");
    Sample2 sample2 = new Sample2(2, "foo");
    int a = 2;
    int b = 3;

    if (sample.equals(sample2)) {
      System.out.println("foo");
    } else if (a == b) {
      System.out.println("a==b");
    } else {
      System.out.println("bar");
    }

    if (args.length == 2) {
      System.out.println("yolo");
    }

    if (args.length == 2)
      System.out.println("yolo");

    switch (sample.getRandom().nextInt()) {
      case 1:
        System.out.println("case1");
        return;
      case 2:
        System.out.println("case2");
      default:
        System.out.println("default");
        break;
    }

    sample.getRandom();
    sample.setTest(4);

    sample2.getTest2();
    sample2.setTest2(null);

    try {
      System.out.println("This method throw an exception!!!");
    } catch (RuntimeException e) {
      System.out.println("Catch this exception!!!");
    }
  }

}
