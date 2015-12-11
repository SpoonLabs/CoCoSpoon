package fil.iagl.opl.rendu.two.samples;

import java.util.Arrays;

public class ForSample {
  public static void main(String[] args) {
    for (int i = 0; i < 10; i++) {
      System.out.println(i);
    }

    for (int i = 0; i < 10; i++)
      System.out.println(i);

    for (int i : Arrays.asList(1, 2, 3)) {
      System.out.println(i);
    }

    for (int i : Arrays.asList(1, 2, 3))
      System.out.println(i);

  }
}
