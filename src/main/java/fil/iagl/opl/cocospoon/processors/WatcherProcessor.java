package fil.iagl.opl.cocospoon.processors;

import fil.iagl.opl.cocospoon.insert.Insertion;
import fil.iagl.opl.cocospoon.insert.impl.*;
import fil.iagl.opl.cocospoon.tools.ContainsSameElementFilter;
import instrumenting._Instrumenting;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;

public class WatcherProcessor extends AbstractProcessor<CtClass<?>> {

  /*private static final Predicate<CtElement> needToBeInstrumented = (candidate) -> !(candidate instanceof CtUnaryOperator)
	&& !(candidate instanceof CtClass)
    && !(candidate instanceof CtBlock) && !candidate.isImplicit()
    && !(candidate instanceof CtLiteral)
    && !(candidate instanceof CtCodeSnippetStatement)
    && !(candidate instanceof CtTypeAccess)
    && !(candidate instanceof CtField)
    && !(candidate instanceof CtReference)
    && !(candidate instanceof CtConstructor)
    && !(candidate instanceof CtMethod)
    && !(candidate.getParent() instanceof CtLambda)
    && !isInsideIfForSwitchDoWhile(candidate);

  private Params params;

  public WatcherProcessor(Params params) {
    this.params = params;
  }*/

	public WatcherProcessor() {

	}

	public static final List<Insertion> filters = Arrays.asList(
			new ConstructorInsert(),
			new MethodInsert(),
			new TryInsert(),
			new CatchInsert(),
			new ForInsert(),
			new WhileInsert(),
			new DoInsert(),
			new SynchronizedInsert(),
			new IfInsert(),
			new SwitchInsert(),
			new CaseInsert(),
			new BeforeInsert(),
			new BasicInsert());

	@Override
	public boolean isToBeProcessed(CtClass<?> candidate) {
		return !candidate.isAnonymous() && candidate.getPackage() != null && !candidate.getPackage().getQualifiedName().startsWith("instrumenting")
				&& candidate.getParent(CtConstructor.class) == null;
	}

	@Override
	public void processingDone() {
		try {
//      CtClass<Object> instrumentClass = getFactory().Class().get(_Instrumenting.class);

			File tmpFile = File.createTempFile("opl_instrumented", "");
			FileOutputStream fout = new FileOutputStream(tmpFile);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(_Instrumenting.lines);
			oos.close();

//      instrumentClass.getField("TMP_FILE_NAME")
//        .setDefaultExpression(getFactory().Code().createCodeSnippetExpression("\"" + StringEscapeUtils.escapeJava(tmpFile.getAbsolutePath()) + "\""));
//      instrumentClass.getField("CURRENT_DIR")
//        .setDefaultExpression(getFactory().Code().createCodeSnippetExpression("\"" + StringEscapeUtils.escapeJava(params.getInputSource()) + "\""));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void process(CtClass<?> ctClass) {
		for (CtElement candidate : ctClass.getElements(new TypeFilter<CtElement>(CtElement.class))) {
			if (!(candidate instanceof CtUnaryOperator)
					&& !(candidate instanceof CtClass)
					&& !(candidate instanceof CtBlock) && !candidate.isImplicit()
					&& !(candidate instanceof CtLiteral)
					&& !(candidate instanceof CtCodeSnippetStatement)
					&& !(candidate instanceof CtTypeAccess)
					&& !(candidate instanceof CtField)
					&& !(candidate instanceof CtReference)
					&& !(candidate instanceof CtConstructor)
					&& !(candidate instanceof CtMethod)
					&& !(candidate.getParent() instanceof CtLambda)
					&& !isInsideIfForSwitchDoWhile(candidate)) {
				findMatcherAndApply(candidate);
			}
		}
//    ctClass.getElements(new TypeFilter<>(CtElement.class)).stream().filter(needToBeInstrumented).forEach(statement -> findMatcherAndApply(statement));
	}

	private void findMatcherAndApply(CtElement element) {
		for (Insertion filter : filters) {
			if (filter.match(element)) {
				instrumentLine(filter, element);
				break;
			}
		}
//		instrumentLine(filters.stream().filter(filter -> filter.match(element)).findFirst().orElse(new DoNotInsert()), element);
	}

	private void instrumentLine(Insertion filter, CtElement element) {
		String qualifiedName = element.getParent(CtClass.class).getQualifiedName();
		if (qualifiedName.contains("$")) {
			qualifiedName = qualifiedName.substring(0, qualifiedName.indexOf("$"));
		}

		_Instrumenting.addInstrumentedClass(qualifiedName);
		_Instrumenting.addInstrumentedStatement(qualifiedName, element.getPosition().getLine());
		CtCodeSnippetStatement statementToInsert = getFactory().Code()
				.createCodeSnippetStatement(
						"instrumenting._Instrumenting.isPassedThrough(\"" + qualifiedName + "\", " + element.getPosition().getLine() + ")");
		filter.apply(element, statementToInsert);
	}

	private static boolean isInsideIfForSwitchDoWhile(CtElement candidate) {
		boolean isInsideIfExpression = false;
		boolean isInsideForExpression = false;
		boolean isInsideForInit = false;
		boolean isInsideForUpdate = false;
		boolean isInsideSwitchInit = false;
		boolean isInsideDoExpression = false;
		boolean isInsideWhileExpression = false;
		if (candidate.getParent(CtIf.class) != null) {
			CtIf ctIf = candidate.getParent(CtIf.class);
			isInsideIfExpression = !ctIf.getCondition().getElements(new ContainsSameElementFilter(candidate)).isEmpty();
		}

		if (candidate.getParent(CtFor.class) != null) {
			CtFor ctFor = candidate.getParent(CtFor.class);
			isInsideForExpression = !(ctFor.getExpression() == null) && !ctFor.getExpression().getElements(new ContainsSameElementFilter(candidate)).isEmpty();

			isInsideForInit = !(ctFor.getForInit() == null);
			for (CtStatement statement : ctFor.getForInit()) {
				isInsideForInit &= !statement.getElements(new ContainsSameElementFilter(candidate)).isEmpty();
			}

			isInsideForUpdate = !(ctFor.getForUpdate() == null);
			for (CtStatement statement : ctFor.getForUpdate()) {
				isInsideForUpdate &= !statement.getElements(new ContainsSameElementFilter(candidate)).isEmpty();
			}

/*			isInsideForInit = !(ctFor.getForInit() == null)
					&& ctFor.getForInit().stream().anyMatch(statement -> !statement.getElements(new ContainsSameElementFilter(candidate)).isEmpty());
			isInsideForUpdate = !(ctFor.getForUpdate() == null)
					&& ctFor.getForUpdate().stream().anyMatch(statement -> !statement.getElements(new ContainsSameElementFilter(candidate)).isEmpty());*/
		}

		if (candidate.getParent(CtSwitch.class) != null) {
			CtSwitch<?> ctSwitch = candidate.getParent(CtSwitch.class);
			isInsideSwitchInit = !ctSwitch.getSelector().getElements(new ContainsSameElementFilter(candidate)).isEmpty();
		}

		if (candidate.getParent(CtDo.class) != null) {
			CtDo ctDo = candidate.getParent(CtDo.class);
			isInsideDoExpression = !ctDo.getLoopingExpression().getElements(new ContainsSameElementFilter(candidate)).isEmpty();
		}

		if (candidate.getParent(CtWhile.class) != null) {
			CtWhile ctWhile = candidate.getParent(CtWhile.class);
			isInsideDoExpression = !ctWhile.getLoopingExpression().getElements(new ContainsSameElementFilter(candidate)).isEmpty();
		}

		return isInsideIfExpression || isInsideDoExpression || isInsideForExpression || isInsideForInit || isInsideForUpdate || isInsideSwitchInit || isInsideWhileExpression;
	}

}
