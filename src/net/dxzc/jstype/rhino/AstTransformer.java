/*
 * Copyright (C) 2019 823984418@qq.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.dxzc.jstype.rhino;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.dxzc.jstype.EmptyType;
import net.dxzc.jstype.JsArrayType;
import net.dxzc.jstype.JsFunction;
import net.dxzc.jstype.JsScope;
import net.dxzc.jstype.JsScopeFactory;
import net.dxzc.jstype.JsTopScope;
import net.dxzc.jstype.JsType;
import net.dxzc.jstype.Lvalue;
import net.dxzc.jstype.Rvalue;
import net.dxzc.jstype.Type;
import net.dxzc.jstype.exp.Add;
import net.dxzc.jstype.exp.ArrayGet;
import net.dxzc.jstype.exp.Assign;
import net.dxzc.jstype.exp.Get;
import net.dxzc.jstype.exp.Invoke;
import net.dxzc.jstype.exp.NewInstance;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.CatchClause;
import org.mozilla.javascript.ast.ConditionalExpression;
import org.mozilla.javascript.ast.DoLoop;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.ErrorCollector;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.ForInLoop;
import org.mozilla.javascript.ast.ForLoop;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.IfStatement;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.ObjectLiteral;
import org.mozilla.javascript.ast.ObjectProperty;
import org.mozilla.javascript.ast.ParenthesizedExpression;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.ReturnStatement;
import org.mozilla.javascript.ast.Scope;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.SwitchCase;
import org.mozilla.javascript.ast.SwitchStatement;
import org.mozilla.javascript.ast.Symbol;
import org.mozilla.javascript.ast.ThrowStatement;
import org.mozilla.javascript.ast.TryStatement;
import org.mozilla.javascript.ast.UnaryExpression;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;
import org.mozilla.javascript.ast.WhileLoop;
import org.mozilla.javascript.ast.WithStatement;

/**
 * 对语法树进行翻译以得到结果.
 *
 * @author 823984418@qq.com
 */
public class AstTransformer {

    /**
     * 具有默认的警告信息收集器和环境工厂的翻译器.
     *
     * @param scopeFactory 脚本域工厂
     */
    public AstTransformer(JsScopeFactory scopeFactory) {
        errorCollector = new ErrorCollector();
        this.scopeFactory = scopeFactory;
    }

    /**
     * 环境工厂.
     */
    protected final JsScopeFactory scopeFactory;

    /**
     * 警告信息收集器.
     */
    public ErrorCollector errorCollector;

    /**
     * 当出现表达式值时调用. 应当被子类覆盖
     *
     * @param value 值
     * @param offset 偏移
     * @param length 长度
     */
    protected void onValue(Rvalue value, int offset, int length) {

    }

    /**
     * 当出现环境域时调用. 应当被子类覆盖
     *
     * @param scope 域
     * @param offset 偏移
     * @param length 长度
     */
    protected void onScope(JsScope scope, int offset, int length) {

    }

    /**
     * 当函数结点没有名字时. 返回特定的名字
     *
     * @param node 函数结点
     * @return 名字
     */
    protected String lambdaFunctionName(FunctionNode node) {
        return "$lambda" + node.getAbsolutePosition();
    }

    /**
     * 列表表达式类型名称.
     *
     * @param node 结点
     * @return 名称
     */
    protected String objectLiteralName(ObjectLiteral node) {
        return "object@" + node.getAbsolutePosition();
    }

    private void onValueByNode(Rvalue value, AstNode node) {
        onValue(value, node.getAbsolutePosition(), node.getLength());
    }

    private void onScopeByNode(JsScope scope, AstNode node) {
        onScope(scope, node.getAbsolutePosition(), node.getLength());
    }

    /**
     * 得到一个顶层域.
     *
     * @return 顶层域
     */
    public JsTopScope buildJsTopScope() {
        return scopeFactory.buildTopScope();
    }

    /**
     * 翻译一个脚本的语法树.
     *
     * @param scope 作用域
     * @param node 语法树
     */
    public void transformScript(JsScope scope, AstRoot node) {
        initScope(scope, node);
        for (AstNode e : node.getStatements()) {
            transform(scope, e);
        }
    }

    private static void set(Rvalue v, String n, Rvalue e) {
        new Assign(new Get(v, n), e);
    }

    private void warning(String message, AstNode node) {
        errorCollector.warning(message, node.getAstRoot().getSourceName(), node.getAbsolutePosition(), node.getLength());
    }

    /**
     * 根据域结点初始化环境域.
     *
     * @param scope 环境
     * @param s 域结点
     */
    protected void initScope(JsScope scope, Scope s) {
        Map<String, Symbol> tab = s.getSymbolTable();
        if (tab != null) {
            for (String n : tab.keySet()) {
                scope.let(n);
            }
        }
        if (s instanceof AstRoot) {
            onScope(scope, 0, Integer.MAX_VALUE);//all script
        } else {
            onScopeByNode(scope, s);
        }
    }

    /**
     * 翻译一个语句.
     *
     * @param scope 环境
     * @param node 语句
     */
    protected void transform(JsScope scope, AstNode node) {
        int type = node.getType();
        if (node instanceof InfixExpression || node instanceof UnaryExpression) {
            exp(scope, node);
            return;
        }
        switch (type) {
            case Token.EMPTY:
            case Token.BREAK:
            case Token.CONTINUE:
                break;
            case Token.WITH: {
                WithStatement s = (WithStatement) node;
                exp(scope, s.getExpression());
                transform(scope, s.getStatement());
                break;
            }
            case Token.IF: {
                IfStatement s = (IfStatement) node;
                exp(scope, s.getCondition());
                if (s.getThenPart() != null) {
                    transform(scope, s.getThenPart());
                }
                if (s.getElsePart() != null) {
                    transform(scope, s.getElsePart());
                }
                break;
            }
            case Token.FOR: {
                if (node instanceof ForLoop) {
                    ForLoop s = (ForLoop) node;
                    JsScope ns = scopeFactory.buildBlock(scope);
                    initScope(ns, s);
                    transform(ns, s.getInitializer());
                    exp(ns, s.getCondition());
                    exp(ns, s.getIncrement());
                    transform(ns, s.getBody());
                } else if (node instanceof ForInLoop) {
                    ForInLoop s = (ForInLoop) node;
                    JsScope ns = scopeFactory.buildBlock(scope);
                    initScope(ns, s);
                    exp(scope, s.getIteratedObject());
                    AstNode var = s.getIterator();
                    if (var instanceof VariableDeclaration) {
                        List<VariableInitializer> vs = ((VariableDeclaration) var).getVariables();
                        if (vs.size() != 1) {
                            warning("Only one in for in loop", var);
                        }
                        VariableInitializer i = vs.get(0);
                        if (i.getInitializer() != null) {
                            warning("Init var at for in loop", i);
                        }
                        var = i.getTarget();
                    }
                    if (var instanceof Name) {
                        Get g = ((Get) exp(ns, var));
                        if (!s.isForEach()) {
                            g.assign(scope.getTopScope().getPrototype(JsTopScope.STRING));
                        }
                    } else {
                        warning("Unknow var at for in loop", var);
                    }
                    transform(ns, s.getBody());
                } else {
                    warning("Unknow for loop", node);
                }
                break;
            }
            case Token.WHILE: {
                WhileLoop s = (WhileLoop) node;
                JsScope ns = scopeFactory.buildBlock(scope);
                initScope(ns, s);
                exp(ns, s.getCondition());
                transform(ns, s.getBody());
                break;
            }
            case Token.DO: {
                DoLoop s = (DoLoop) node;
                JsScope ns = scopeFactory.buildBlock(scope);
                initScope(ns, s);
                transform(ns, s.getBody());
                exp(ns, s.getCondition());
                break;
            }
            case Token.SWITCH: {
                SwitchStatement s = (SwitchStatement) node;
                exp(scope, s.getExpression());
                for (SwitchCase c : s.getCases()) {
                    AstNode e = c.getExpression();
                    if (e != null) {
                        exp(scope, e);
                    }
                    List<AstNode> sl = c.getStatements();
                    if (sl != null) {
                        for (AstNode ns : sl) {
                            transform(scope, ns);
                        }
                    }
                }
                break;
            }
            case Token.TRY: {
                TryStatement s = (TryStatement) node;
                transform(scope, s.getTryBlock());
                for (CatchClause cc : s.getCatchClauses()) {
                    transform(scope, cc);
                }
                AstNode f = s.getFinallyBlock();
                if (f != null) {
                    transform(scope, f);
                }
                break;
            }
            case Token.CATCH: {
                CatchClause c = (CatchClause) node;
                AstNode ci = c.getCatchCondition();
                if (ci != null) {
                    exp(scope, ci);
                }
                transform(scope, c.getBody());
                break;
            }
            case Token.RETURN:
                set(scope.getScope(), Type.RETURN, exp(scope, ((ReturnStatement) node).getReturnValue()));
                break;
            case Token.VAR:
            case Token.CONST:
            case Token.LET: {
                VariableDeclaration v = (VariableDeclaration) node;
                for (VariableInitializer i : v.getVariables()) {
                    AstNode iv = i.getInitializer();
                    if (i.isDestructuring()) {
                        warning("Destructuring var/const/let", node);
                    } else {
                        String name = i.getTarget().getString();
                        Get t = new Get(scope.find(name), name);
                        onValueByNode(t, i.getTarget());
                        if (iv != null) {
                            Rvalue value = exp(scope, iv);
                            onValueByNode(value, iv);
                            onValueByNode(new Assign(t, value), i);
                        } else {
                            onValueByNode(t, i);
                        }
                    }
                }
                break;
            }
            case Token.BLOCK: {
                JsScope s;
                if (node instanceof Scope) {
                    s = scopeFactory.buildBlock(scope);
                    initScope(scope, (Scope) node);
                } else {
                    s = scope;
                }
                for (Node e : node) {
                    if (node instanceof AstNode) {
                        AstNode n = (AstNode) e;
                        transform(s, n);
                    } else {
                        warning("Error in block", node);
                    }
                }
                break;
            }
            case Token.EXPR_VOID:
            case Token.EXPR_RESULT:
                exp(scope, ((ExpressionStatement) node).getExpression());
                break;
            case Token.FUNCTION:
            case Token.LP:
            case Token.CALL:
                exp(scope, node);
                break;
            case Token.THROW:
                exp(scope, ((ThrowStatement) node).getExpression());
                break;
            case Token.ERROR:
                warning("Error token", node);
                break;
            default:
                warning("Unknow statement:" + Token.typeToName(type) + " " + node.getClass().getSimpleName(), node);
        }
    }

    /**
     * 翻译一个表达式.
     *
     * @param scope 环境
     * @param node 表达式
     * @return 值
     */
    protected Rvalue exp(JsScope scope, AstNode node) {
        int type = node.getType();
        Rvalue r = null;
        switch (type) {
            case Token.EMPTY:
                r = new Rvalue();
                break;
            case Token.IN: {
                InfixExpression e = (InfixExpression) node;
                exp(scope, e.getLeft());
                exp(scope, e.getRight());
                r = new Rvalue(scope.getTopScope().getPrototype(JsTopScope.BOOLEAN));
                break;
            }
            case Token.GETELEM: {
                ElementGet e = (ElementGet) node;
                AstNode g = e.getElement();
                Rvalue v = exp(scope, e.getTarget());
                Rvalue t = exp(scope, e.getElement());
                if (g.getType() == Token.STRING) {
                    String name = ((StringLiteral) g).getValue();
                    if (isName(name)) {
                        r = new Get(v, name);
                    }
                }
                if (r == null) {
                    r = new ArrayGet(scope.getTopScope(), v, t);
                }
                break;
            }
            case Token.NULL:
                r = new Rvalue();
                break;
            case Token.COMMA: {
                InfixExpression e = (InfixExpression) node;
                exp(scope, e.getLeft());
                r = exp(scope, e.getRight());
                break;
            }
            case Token.SHEQ:
            case Token.SHNE:
            case Token.EQ:
            case Token.NE:
            case Token.LT:
            case Token.GT:
            case Token.LE:
            case Token.GE: {
                InfixExpression e = (InfixExpression) node;
                exp(scope, e.getLeft());
                exp(scope, e.getRight());
                r = new Rvalue(scope.getTopScope().getPrototype(JsTopScope.BOOLEAN));
                break;
            }
            case Token.TYPEOF:
                exp(scope, ((UnaryExpression) node).getOperand());
                r = new Rvalue(scope.getTopScope().getPrototype(JsTopScope.STRING));
                break;
            case Token.INSTANCEOF: {
                InfixExpression e = (InfixExpression) node;
                exp(scope, e.getLeft());
                exp(scope, e.getRight());
                r = new Rvalue(scope.getTopScope().getPrototype(JsTopScope.BOOLEAN));
                break;
            }
            case Token.DELPROP:
                exp(scope, ((UnaryExpression) node).getOperand());
                r = new Rvalue(scope.getTopScope().getPrototype(JsTopScope.BOOLEAN));
                break;
            case Token.AND:
            case Token.OR: {
                InfixExpression e = (InfixExpression) node;
                r = new Rvalue();
                exp(scope, e.getLeft()).forType(r::addType);
                exp(scope, e.getRight()).forType(r::addType);
                break;
            }
            case Token.REGEXP:
                r = new Rvalue(scope.getTopScope().getPrototype(JsTopScope.REGEXP));
                break;
            case Token.NOT: {
                UnaryExpression e = (UnaryExpression) node;
                Rvalue v = exp(scope, e.getOperand());
                r = new Rvalue(scope.getTopScope().getPrototype(JsTopScope.BOOLEAN));
                break;
            }
            case Token.THIS:
                r = new Get(scope.getScope(), Type.THIS);
                break;
            case Token.ARRAYLIT: {
                ArrayLiteral l = (ArrayLiteral) node;
                JsArrayType t = new JsArrayType();
                t.extend(scope.getTopScope().getPrototype(JsTopScope.ARRAY));
                for (AstNode n : l.getElements()) {
                    exp(scope, n).forType(p -> t.putMember(Type.CONTAIN, p));
                }
                r = new Rvalue(t);
                break;
            }
            case Token.OBJECTLIT: {
                ObjectLiteral l = (ObjectLiteral) node;
                JsType t = new JsType(objectLiteralName(l));
                t.extend(scope.getTopScope().getPrototype(JsTopScope.OBJECT));
                for (ObjectProperty o : l.getElements()) {
                    if (o.getType() == Token.COLON) {
                        AstNode n = o.getLeft();
                        if (n instanceof Name) {
                            String k = n.getString();
                            exp(scope, o.getRight()).forType(v -> t.putMember(k, v));
                        }
                    }
                }
                r = new Rvalue(t);
                break;
            }
            case Token.HOOK: {
                ConditionalExpression e = (ConditionalExpression) node;
                r = new Rvalue();
                exp(scope, e.getTestExpression());
                exp(scope, e.getTrueExpression()).forType(r::addType);
                exp(scope, e.getFalseExpression()).forType(r::addType);
                break;
            }
            case Token.ASSIGN: {
                Assignment s = (Assignment) node;
                Rvalue lv = exp(scope, s.getLeft());
                Rvalue rv = exp(scope, s.getRight());
                String doc = node.getJsDoc();
                if (doc != null && s.getRight().getType() == Token.FUNCTION) {
                    rv.forType(t -> {
                        if (t instanceof JsType) {
                            ((JsType) t).addDoc(initDoc(doc));
                        }
                    });
                }
                if (lv instanceof Lvalue) {
                    r = new Assign((Lvalue) lv, rv);
                } else {
                    r = rv;
                    warning("No left value", node);
                }
                break;
            }
            case Token.NUMBER:
                r = new Rvalue(scope.getTopScope().getPrototype(JsTopScope.NUMBER));
                break;
            case Token.STRING:
                r = new Rvalue(scope.getTopScope().getPrototype(JsTopScope.STRING));
                break;
            case Token.LP:
                r = exp(scope, ((ParenthesizedExpression) node).getExpression());
                break;
            case Token.ADD: {
                InfixExpression n = (InfixExpression) node;
                r = new Add(scope.getTopScope(), exp(scope, n.getLeft()), exp(scope, n.getRight()));
                break;
            }
            case Token.SUB:
            case Token.MUL:
            case Token.DIV:
            case Token.MOD:
            case Token.BITAND:
            case Token.BITXOR:
            case Token.BITOR:
            case Token.LSH:
            case Token.RSH:
            case Token.URSH: {
                InfixExpression n = (InfixExpression) node;
                exp(scope, n.getLeft());
                exp(scope, n.getRight());
                r = new Rvalue(scope.getTopScope().getPrototype(JsTopScope.NUMBER));
                break;
            }
            case Token.BITNOT: {
                exp(scope, ((UnaryExpression) node).getOperand());
                r = new Rvalue(scope.getTopScope().getPrototype(JsTopScope.NUMBER));
                break;
            }
            case Token.INC:
            case Token.DEC: {
                AstNode n = ((UnaryExpression) node).getOperand();
                Rvalue v = exp(scope, n);
                if (v instanceof Lvalue) {
                    Type num = scope.getTopScope().getPrototype(JsTopScope.NUMBER);
                    ((Lvalue) v).assign(num);
                    r = new Rvalue(num);
                } else {
                    warning("Not left value", n);
                }
                break;
            }
            case Token.ASSIGN_ADD: {
                Assignment s = (Assignment) node;
                Rvalue target = exp(scope, s.getLeft());
                Rvalue v = exp(scope, s.getRight());
                if (target instanceof Lvalue) {
                    r = new Assign((Lvalue) target, new Add(scope.getTopScope(), target, v));
                } else {
                    warning("Not left value", s.getLeft());
                }
                break;
            }
            case Token.ASSIGN_SUB:
            case Token.ASSIGN_MUL:
            case Token.ASSIGN_DIV:
            case Token.ASSIGN_MOD:
            case Token.ASSIGN_BITXOR:
            case Token.ASSIGN_BITOR:
            case Token.ASSIGN_BITAND:
            case Token.ASSIGN_LSH:
            case Token.ASSIGN_RSH:
            case Token.ASSIGN_URSH: {
                Assignment s = (Assignment) node;
                Rvalue v = exp(scope, s.getLeft());
                exp(scope, s.getRight());
                if (v instanceof Lvalue) {
                    Type num = scope.getTopScope().getPrototype(JsTopScope.NUMBER);
                    ((Lvalue) v).assign(num);
                    r = new Rvalue(num);
                } else {
                    warning("Not left value", s.getLeft());
                }
                break;
            }
            case Token.POS:
            case Token.NEG:
                exp(scope, ((UnaryExpression) node).getOperand());
                r = new Rvalue(scope.getTopScope().getPrototype(JsTopScope.NUMBER));
                break;
            case Token.VOID:
                exp(scope, ((UnaryExpression) node).getOperand());
                r = new Rvalue();
                break;
            case Token.GETPROP: {
                PropertyGet n = (PropertyGet) node;
                r = new Get(exp(scope, n.getTarget()), n.getProperty().getString());
                break;
            }
            case Token.NEW: {
                FunctionCall n = (FunctionCall) node;
                List<AstNode> as = n.getArguments();
                Rvalue[] args = new Rvalue[as.size()];
                Iterator<AstNode> ai = as.iterator();
                for (int i = 0, l = args.length; i < l; i++) {
                    if (!ai.hasNext()) {
                        throw new RuntimeException();
                    }
                    args[i] = exp(scope, ai.next());
                }
                r = new NewInstance(exp(scope, n.getTarget()), args);
                break;
            }
            case Token.CALL: {
                FunctionCall n = (FunctionCall) node;
                List<AstNode> as = n.getArguments();
                Rvalue[] args = new Rvalue[as.size()];
                Iterator<AstNode> ai = as.iterator();
                for (int i = 0, l = args.length; i < l; i++) {
                    if (!ai.hasNext()) {
                        throw new RuntimeException();
                    }
                    args[i] = exp(scope, ai.next());
                }
                r = new Invoke(exp(scope, n.getTarget()), args);
                break;
            }
            case Token.FUNCTION:
                r = new Rvalue(function(scope, (FunctionNode) node));
                break;
            case Token.NAME: {
                String name = node.getString();
                r = new Get(scope.find(name), name);
                break;
            }
            case Token.XML:
                if (scope.getTopScope().getPrototype(JsTopScope.XML) != null) {
                    r = new Rvalue(new EmptyType(JsTopScope.XML));
                } else {
                    r = new Rvalue();
                }
                break;
            case Token.ERROR:
                warning("Error token", node);
                break;
            case Token.COMMENT:
                break;
            default:
                warning("Unknow expression:" + Token.typeToName(type) + " " + node.getClass().getSimpleName(), node);
        }
        if (r == null) {
            r = new Rvalue();
        }
        onValueByNode(r, node);
        return r;
    }

    /**
     * 翻译一个函数声明.
     *
     * @param scope 环境
     * @param node 函数结点
     * @return 函数类型
     */
    protected JsFunction function(JsScope scope, FunctionNode node) {
        int type = node.getFunctionType();
        String name = node.getName();
        if (name.isEmpty()) {
            name = lambdaFunctionName(node);
        }
        JsScope s = scopeFactory.buildScope(scope);
        initScope(s, node);
        s.setTypeName(name);
        List<AstNode> as = node.getParams();
        String[] args = new String[as.size()];
        Iterator<AstNode> ai = as.iterator();
        for (int i = 0, l = args.length; i < l; i++) {
            if (!ai.hasNext()) {
                throw new RuntimeException();
            }
            AstNode n = ai.next();
            if (n instanceof Name) {
                args[i] = n.getString();
            } else {
                args[i] = Type.NU_ENUM + "Error";
            }
        }
        JsType prototype = new JsType(name);
        prototype.extend(scope.getTopScope().getPrototype(JsTopScope.OBJECT));
        JsFunction fun = new JsFunction(name, s, prototype, args);
        fun.extend(scope.getTopScope().getPrototype(JsTopScope.FUNCTION));
        String doc = node.getJsDoc();
        if (doc != null) {
            fun.addDoc(initDoc(doc));
        }
        transform(s, node.getBody());
        if (type == FunctionNode.FUNCTION_STATEMENT) {
            scope.getScope().getScopeType().putMember(name, fun);
        }
        return fun;
    }

    /**
     * 移除文档注释中的*.
     *
     * @param s 注释内容
     * @return 文档内容
     */
    protected String initDoc(String s) {
        StringBuilder sb = new StringBuilder();
        boolean h = true;
        for (int i = 2, l = s.length() - 1; i < l; i++) {
            char c = s.charAt(i);
            if (h) {
                if (c == '*') {
                    h = false;
                    continue;
                }
                if (c == ' ') {
                    continue;
                }
            }
            sb.append(c);
            if (c == '\n') {
                h = true;
            }
        }
        return sb.toString();
    }

    /**
     * 检验名字是否合法.
     *
     * @param name 名字
     * @return 合法性
     */
    protected boolean isName(String name) {
        if (name.isEmpty()) {
            return false;
        }
        return name.charAt(0) != '#';
    }

}
