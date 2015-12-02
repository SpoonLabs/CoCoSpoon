package fil.iagl.opl.rendu.two.tools;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.Filter;

public class ContainsSameElementFilter implements Filter<CtElement> {

  private CtElement lookingFor;

  public ContainsSameElementFilter(CtElement lookingFor) {
    this.lookingFor = lookingFor;
  }

  @Override
  public boolean matches(CtElement element) {
    return this.lookingFor == element;
  }

}
