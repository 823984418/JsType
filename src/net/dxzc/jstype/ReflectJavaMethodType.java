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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import net.dxzc.util.Action;

/**
 * 对方法的封装.
 *
 * @author 823984418@qq.com
 */
public class ReflectJavaMethodType implements Type {

    /**
     * 封装一个方法.
     *
     * @param m 方法
     */
    public ReflectJavaMethodType(Method m) {
        method = m;
    }

    /**
     * 封装的方法.
     */
    public final Method method;

    @Override
    public boolean addMemberAction(String name, Action<Type> action) {
        if (THIS.equals(name)) {
            action.action(new ReflectJavaObjectType(method.getDeclaringClass()));
            return true;
        }
        return false;
    }

    @Override
    public boolean putMember(String name, Type type) {
        if (THIS.equals(name)
                && type instanceof ReflectJavaObjectType
                && method.getDeclaringClass().isAssignableFrom(((ReflectJavaObjectType) type).javaClass)) {
            return true;
        }
        return false;
    }

    @Override
    public Iterator<String> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public Iterator<Type> getMemberType(String name) {
        if (THIS.equals(name)) {
            return Arrays.<Type>asList(new ReflectJavaObjectType(method.getDeclaringClass())).iterator();
        }
        return Collections.emptyIterator();
    }

    @Override
    public Iterator<Action<Type>> invoke(int length) {
        if (method.isVarArgs() && length >= method.getParameterCount() - 1) {
            return new AItr(length);
        }
        if (length == method.getParameterCount()) {
            return new AItr(length);
        }
        return null;
    }

    @Override
    public void doInvoke(Action<Type> action) {
        Class c = method.getReturnType();
        if (c != Void.TYPE) {
            action.action(new ReflectJavaObjectType(c));
        }
    }

    @Override
    public String getDoc() {
        return method.toGenericString();
    }

    private static class AItr implements Iterator<Action<Type>> {

        private AItr(int i) {
            this.i = i;
        }
        private int i;

        @Override
        public boolean hasNext() {
            return i > 0;
        }

        @Override
        public Action<Type> next() {
            i--;
            return null;
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(method.getName());
        sb.append("(");
        boolean f = true;
        for (Class<?> a : method.getParameterTypes()) {
            if (f) {
                f = false;
            } else {
                sb.append(",");
            }
            sb.append(a.getSimpleName());
        }
        sb.append(")");
        Class<?> r = method.getReturnType();
        if (r != Void.TYPE) {
            sb.append(r.getSimpleName());
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof ReflectJavaMethodType) {
            return method.equals(((ReflectJavaMethodType) obj).method);
        }
        return false;
    }

}
