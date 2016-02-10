package fil.iagl.opl.cocospoon.samples;

public class SynchronizedSample {

  public static void main(String[] args) {

    synchronized (args) {
      System.out.println("foo");
    }

    synchronized (args) {
      System.out.println("bar");
    }

  }

}
