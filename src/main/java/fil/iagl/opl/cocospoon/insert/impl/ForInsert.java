package fil.iagl.opl.cocospoon.insert.impl;

import fil.iagl.opl.cocospoon.insert.Insertion;
import spoon.reflect.code.CtFor;
import spoon.reflect.code.CtForEach;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;

public class ForInsert implements Insertion {

  @Override
  public boolean match(CtElement element) {
    return element instanceof CtFor || element instanceof CtForEach;
  }

  @Override
  public void apply(CtElement element, CtStatement statementToInsert) {
    if (element instanceof CtFor) {
      CtFor ctFor = (CtFor) element;
      ctFor.getBody().insertBefore(statementToInsert);
    }
    if (element instanceof CtForEach) {
      CtForEach ctForEach = (CtForEach) element;
      ctForEach.getBody().insertBefore(statementToInsert);
    }

  }

}
