package fil.iagl.opl.cocospoon;


import fil.iagl.opl.cocospoon.insert.Insertion;
import fil.iagl.opl.cocospoon.insert.impl.BeforeInsert;
import fil.iagl.opl.cocospoon.processors.WatcherProcessor;
import fil.iagl.opl.cocospoon.samples.MethodSample;
import fil.iagl.opl.cocospoon.tools.ContainsSameElementFilter;
import org.fest.assertions.Assertions;
import org.junit.Test;
import spoon.Launcher;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CocoSpoonTest {

  @Test
  public void test() throws Exception {
    // contract: instrumenting does not depend from anything
    Launcher l = new Launcher();
    l.addInputResource("src/main/java/instrumenting/");
    l.getEnvironment().setShouldCompile(true);
    l.run();

    // contract: all processors together are fine
    Launcher l2 = new Launcher();
    l2.getFactory().Package().getRootPackage().addPackage(l.getFactory().Package().get("instrumenting"));
    l2.addInputResource("src/test/java/fil/iagl/opl/cocospoon/samples/");
    l2.addProcessor(new WatcherProcessor());
    l2.getEnvironment().setShouldCompile(true);
    l2.run();

    // contract: the runtime works
    // the constructor has been instrumented and calls the Cocospoon method
    Object o = l2.getFactory().Class().get(MethodSample.class).newInstance();
  }

}
