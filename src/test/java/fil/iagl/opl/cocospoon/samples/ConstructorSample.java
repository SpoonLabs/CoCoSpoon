package fil.iagl.opl.cocospoon.samples;

public class ConstructorSample {

  private String foo;
  private String bar;

  public ConstructorSample() {
    super();
  }

  public ConstructorSample(String foo) {
    this();
    this.foo = foo;
  }

  public ConstructorSample(String foo, String bar) {
    this(foo);
    this.bar = bar;
  }
}
