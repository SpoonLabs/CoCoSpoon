package fil.iagl.opl.rendu.two.samples;

import java.util.Random;

public class Sample2 {

  private String test2;
  private int test;

  private Random random;

  public Sample2(int test, String test2) {
    this.test2 = test2;
    this.random = new Random(System.currentTimeMillis());
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

}
