package fil.iagl.opl.rendu.two.insert.impl;

import fil.iagl.opl.rendu.two.insert.Insertion;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCase;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtSwitch;
import spoon.reflect.declaration.CtElement;

public class SwitchInsert implements Insertion {

  @Override
  public boolean match(CtElement element) {
    return element instanceof CtSwitch;
  }

  @Override
  public void apply(CtElement element, CtStatement statementToInsert) {
    CtSwitch<CtCase<?>> ctSwitch = (CtSwitch) element;

    CtBlock<?> ctBlock = element.getParent(CtBlock.class);
    int idx = -1;
    for (int i = 0; i < ctBlock.getStatements().size(); i++) {
      if (ctBlock.getStatement(i) == ctSwitch) {
        idx = i;
      }
    }
    if (idx != -1)
      ctBlock.getStatements().add(idx, statementToInsert);
  }

}
