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
package net.dxzc.jstype;

import java.util.Iterator;
import java.util.NoSuchElementException;
import net.dxzc.jstype.exp.Get;
import net.dxzc.util.Action;

/**
 * 函数类型.
 *
 * @author 823984418@qq.com
 */
public class JsFunction extends JsType {

    public static final String PROTOTYPE = "prototype";

    /**
     * 构造一个函数.
     *
     * @param name 函数名
     * @param runtime 环境作用域
     * @param prototype 原型
     * @param as 形参表
     */
    public JsFunction(String name, JsScope runtime, JsType prototype, String... as) {
        super(name);
        this.runtime = runtime;
        this.prototype = prototype;
        argNames = as.clone();
        super.putMember(PROTOTYPE, prototype);
        if (as.length > 0 && as[as.length - 1].startsWith("...")) {
            varArgs = true;
        } else {
            varArgs = false;
        }
        args = new Action[as.length];
        if (runtime != null) {
            runtime.getScopeType().addMemberAction(RETURN, t -> putMember(RETURN, t));
            addMemberAction(THIS, t -> runtime.getScopeType().putMember(THIS, t));
            for (int i = 0, l = args.length; i < l; i++) {
                runtime.let(as[i]);
                args[i] = new Get(runtime, argNames[i])::assign;
            }
        }
        if (varArgs) {
            //
        }
        putMember(NEW, prototype);
    }

    /**
     * 实例原型.
     */
    protected JsType prototype;

    /**
     * 函数作用域.
     */
    protected final JsScope runtime;

    /**
     * 是否变长参数.
     */
    protected final boolean varArgs;

    /**
     * 参数类型传递.
     */
    protected final Action<Type>[] args;

    /**
     * 各个参数名称.
     */
    protected final String[] argNames;

    @Override
    public boolean putMember(String name, Type type) {
        if (PROTOTYPE.equals(name)) {
            prototype.extend(type);
            return true;
        }
        return super.putMember(name, type);
    }

    @Override
    public Iterator<Action<Type>> invoke(int length) {
        return new AItr(varArgs, args, length);
    }

    @Override
    public Iterator<Action<Type>> newInstance(int length) {
        return new AItr(varArgs, args, length);
    }

    @Override
    public void doNewInstance(Action<Type> action) {
        super.doNewInstance(action);
        putMember(THIS, prototype);
    }

    @Override
    public String argsToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        String[] as = argNames;
        int l = as.length;
        if (l != 0) {
            int e = l - 1;
            for (int i = 0; i < e; i++) {
                sb.append(as[i]);
                sb.append(",");
            }
            if (varArgs) {
                sb.append("...");
            }
            sb.append(as[e]);
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String toString() {
        return name + functionToString();
    }

    private static class AItr implements Iterator<Action<Type>> {

        private AItr(boolean var, Action<Type>[] es, int length) {
            this.es = es;
            l = length;
            this.n = var;
        }

        private final int l;

        private final boolean n;

        private int i;

        private final Action<Type>[] es;

        @Override
        public boolean hasNext() {
            return i < l;
        }

        @Override
        public Action<Type> next() {
            if (i >= l) {
                throw new NoSuchElementException();
            }
            if (i < es.length) {
                return es[i++];
            }
            i++;
            if (n) {
                return es[es.length];
            }
            return null;
        }

    }

}
