package fil.iagl.opl.cocospoon.tools;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.Filter;

public class EqualsElementFilter implements Filter<CtElement> {

  private CtElement lookingFor;

  public EqualsElementFilter(CtElement lookingFor) {
    this.lookingFor = lookingFor;
  }

  @Override
  public boolean matches(CtElement element) {
    return this.lookingFor.equals(element);
  }

}
