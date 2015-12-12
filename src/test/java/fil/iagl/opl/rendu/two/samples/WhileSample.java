package fil.iagl.opl.rendu.two.samples;

public class WhileSample {

  public static void main(String[] args) {
    int i = 0;

    while (i < 10) {
      System.out.println("mon while");
      i++;
    }

    while (true)
      System.out.println("mon while sans block");

  }

}
