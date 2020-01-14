/*
 * Copyright (C) 2020 823984418@qq.com
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
package net.dxzc.jstype.refjava;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import net.dxzc.jstype.Rvalue;
import net.dxzc.jstype.Type;
import net.dxzc.util.Action;

/**
 * 方法.
 *
 * @author 823984418@qq.com
 */
public class JavaMethodType implements Type {

    /**
     * 构造一个方法的类型.
     *
     * @param javaLoader 加载器
     * @param method 方法
     */
    protected JavaMethodType(JavaLoader javaLoader, Method method) {
        this.method = method;
        isVarArgs = method.isVarArgs();
        parameterCount = method.getParameterTypes().length;
        returnType = javaLoader.getObjectType(method.getReturnType());
    }

    /**
     * 方法.
     */
    public final Method method;

    /**
     * 变长参数.
     */
    public final boolean isVarArgs;

    /**
     * 参数个数.
     */
    public final int parameterCount;

    /**
     * 返回类型.
     */
    public final JavaObjectType returnType;

    @Override
    public boolean addMemberAction(String name, Action<Type> action) {
        return false;
    }

    @Override
    public boolean putMember(String name, Type type) {
        return false;
    }

    @Override
    public Iterator<String> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public Iterator<String> iteratorAll() {
        return Collections.emptyIterator();
    }

    @Override
    public Iterator<Type> getMemberType(String name) {
        return Collections.emptyIterator();
    }

    @Override
    public boolean invoke(Action<Type> r, Rvalue i, Rvalue... args) {
        int length = args.length;
        if (isVarArgs && length > parameterCount || length == parameterCount) {
            if (returnType != null) {
                r.action(returnType);
            }
            return true;
        }
        return false;
    }

    @Override
    public String getDoc() {
        return method.toGenericString();
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
        if (obj instanceof JavaMethodType) {
            return method.equals(((JavaMethodType) obj).method);
        }
        return false;
    }

}
