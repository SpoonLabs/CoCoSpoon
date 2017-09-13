package fil.iagl.opl.cocospoon.processors;

import fil.iagl.opl.cocospoon.insert.Insertion;
import fil.iagl.opl.cocospoon.insert.impl.ForInsert;
import fil.iagl.opl.cocospoon.tools.ContainsSameElementFilter;
import org.fest.assertions.Assertions;
import org.junit.Test;
import spoon.Launcher;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.filter.NamedElementFilter;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ForInsertTest {

	@Test
	public void instrumentForTest() throws Exception {
		Launcher l = new Launcher();

		l.addInputResource("src/test/java/fil/iagl/opl/cocospoon/samples");
		l.buildModel();

		CtClass<?> sample = l.getFactory().Package().getRootPackage().getElements(new NamedElementFilter<>(CtClass.class, "ForSample")).get(0);

		int nbFor = 4;
		final int nbStatementToInsert = 4;
		final Insertion insertionStrategy = new ForInsert();
		CtStatement statementToInsert = l.getFactory().Code().createCodeSnippetStatement("TO BE INSERT");
	/*Assertions.assertThat(
      sample.getElements(new TypeFilter<CtElement>(CtElement.class))
        .stream().filter(insertionStrategy::match).collect(Collectors.toList()))
      .hasSize(nbFor);

    sample.getElements(new TypeFilter<CtElement>(CtElement.class))
      .stream().filter(insertionStrategy::match).forEach(element -> insertionStrategy.apply(element, statementToInsert));*/

		List<CtElement> elements = sample.getElements(new TypeFilter<CtElement>(CtElement.class) {
			@Override
			public boolean matches(CtElement element) {
				return insertionStrategy.match(element);
			}
		});

		assertEquals(nbFor, elements.size());

		for (CtElement element : elements) {
			insertionStrategy.apply(element, statementToInsert);
		}

		System.out.println(sample);
		Assertions.assertThat(
				sample.getElements(new ContainsSameElementFilter(statementToInsert)))
				.hasSize(nbStatementToInsert);
	}
}
