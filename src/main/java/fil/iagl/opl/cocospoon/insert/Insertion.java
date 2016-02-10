package fil.iagl.opl.cocospoon.insert;

import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtElement;

public interface Insertion {

  boolean match(CtElement element);

  void apply(CtElement element, CtStatement statementToInsert);

}
