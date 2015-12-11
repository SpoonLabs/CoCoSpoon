package fil.iagl.opl.rendu.two.samples;

public class TrySample {

  public static void main(String[] args) {
    try {
      System.out.println("test");
      System.out.println("test");
      System.out.println("test");
    } catch (Exception e) {
      System.out.println("exception");
    } catch (IllegalAccessError e) {
      System.out.println("exception");
    }
  }

}
