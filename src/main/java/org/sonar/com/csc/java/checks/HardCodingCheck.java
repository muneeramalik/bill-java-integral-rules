package org.sonar.com.csc.java.checks;

import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.ForStatementTree;
import org.sonar.plugins.java.api.tree.StatementTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;
import org.sonar.plugins.java.api.tree.VariableTree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * @author mmalik25
 * 
 */

@Rule(key = "MM001")

public class HardCodingCheck extends IssuableSubscriptionVisitor {

	private static final Pattern CONSTANT_VARIABLE_PATTERN = Pattern.compile("(constant|ConstantCategory|FieldType)",
			Pattern.CASE_INSENSITIVE);

	private List<VariableTree> variables = Lists.newArrayList();

	@Override
	public List<Kind> nodesToVisit() {
		return ImmutableList.of(Tree.Kind.VARIABLE, Tree.Kind.FOR_STATEMENT, Tree.Kind.METHOD_INVOCATION);
	}

	@Override
	public void visitNode(Tree tree) {
		if (hasSemantic()) {
			if (tree.is(Tree.Kind.FOR_STATEMENT)) {
				ForStatementTree forStatementTree = (ForStatementTree) tree;
				addVariables(forStatementTree.initializer());
			} else if (tree.is(Tree.Kind.VARIABLE)) {
				VariableTree variableTree = (VariableTree) tree;
				handleVariable(variableTree);
				variables.clear();
			}
		}
	}

	private void handleVariable(VariableTree variableTree) {

		Type symbolType = variableTree.type().symbolType();
		boolean isIssue = false;
		for (VariableTree varTree : variables) {
			Symbol sym = varTree.symbol();
			Type symType = varTree.type().symbolType();

			if (sym.name().equals(variableTree.symbol().name()) && symType.name().equals(symbolType.name())) {
				isIssue = true;
			}
		}
		if (isRequiredLiteral(variableTree.initializer())
				&& !CONSTANT_VARIABLE_PATTERN.matcher(symbolType.name()).find() && !isIssue) {
			reportIssue(variableTree.simpleName(), "Hardcoding of variables must be avoided.");
		}
	}

	private static boolean argumentisLiteral(ExpressionTree expressionTree) {
		if (!expressionTree.is(Kind.INT_LITERAL, Kind.LONG_LITERAL, Kind.FLOAT_LITERAL, Kind.DOUBLE_LITERAL,
				Kind.BOOLEAN_LITERAL, Kind.CHAR_LITERAL, Kind.STRING_LITERAL)) {
			return false;
		}
		return true;
	}

	private static boolean isRequiredLiteral(@Nullable ExpressionTree initializer) {
		return initializer != null && argumentisLiteral(initializer);
	}

	private void addVariables(List<StatementTree> statementTrees) {
		for (StatementTree statementTree : statementTrees) {
			if (statementTree.is(Tree.Kind.VARIABLE)) {
				addVariable((VariableTree) statementTree);
			}
		}
	}

	private void addVariable(VariableTree variableTree) {
		variables.add(variableTree);
	}

}
