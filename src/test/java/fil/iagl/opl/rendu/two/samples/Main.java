package fil.iagl.opl.rendu.two.samples;

public class Main {

  public static void main(String[] args) {
    Sample sample = new Sample(2, "foo");
    Sample2 sample2 = new Sample2(2, "foo");

    sample.getRandom();
    sample.setTest(4);

    sample2.getTest2();
    sample2.setTest2(null);
  }

}
