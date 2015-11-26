package fil.iagl.opl.rendu.two.samples;

import java.util.Random;

public class Sample {

  private int test;

  private String test2;

  private Random random;

  public Sample(int test, String test2) {
    super();
    this.test = test;
    this.test2 = test2;
    this.random = new Random(System.currentTimeMillis());
  }

  public Sample(int test) {
    this(test, "test");
  }

  public int getTest() {
    return test;
  }

  public void setTest(int test) {
    this.test = test;
  }

  public String getTest2() {
    return test2;
  }

  public void setTest2(String test2) {
    this.test2 = test2;
  }

  public Random getRandom() {
    return random;
  }

  public void setRandom(Random random) {
    this.random = random;
  }

}
