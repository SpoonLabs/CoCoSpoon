package fil.iagl.opl.rendu.two.insert.impl;

import fil.iagl.opl.rendu.two.insert.Insertion;
import spoon.reflect.code.CtDo;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;

public class DoInsert implements Insertion {

  @Override
  public boolean match(CtElement element) {
    return element instanceof CtDo;
  }

  @Override
  public void apply(CtElement element, CtStatement statementToInsert) {
    CtDo ctDo = (CtDo) element;
    ctDo.getBody().insertBefore(statementToInsert);
  }

}
