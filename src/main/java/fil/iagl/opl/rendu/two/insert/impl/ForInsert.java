package fil.iagl.opl.rendu.two.insert.impl;

import fil.iagl.opl.rendu.two.insert.Insertion;
import spoon.reflect.code.CtFor;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;

public class ForInsert implements Insertion {

  @Override
  public boolean match(CtElement element) {
    return element instanceof CtFor;
  }

  @Override
  public void apply(CtElement element, CtStatement statementToInsert) {
    CtFor ctFor = (CtFor) element;
    ctFor.getBody().insertBefore(statementToInsert);
  }

}
