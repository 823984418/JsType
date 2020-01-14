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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.dxzc.jstype.Rvalue;
import net.dxzc.jstype.Type;
import net.dxzc.util.Action;

/**
 * 类的类型.
 *
 * @author 823984418@qq.com
 */
public class JavaClassType implements Type {

    /**
     * 构建指定的类类型.
     *
     * @param javaClass 类
     */
    protected JavaClassType(JavaClass javaClass) {
        this.javaClass = javaClass;
    }

    /**
     * 类.
     */
    public final JavaClass javaClass;

    /**
     * 成员.
     */
    protected final Map<String, Set<Type>> memberMap = new HashMap<>();

    @Override
    public boolean addMemberAction(String name, Action<Type> action) {
        javaClass.load();
        Set<Type> set = memberMap.get(name);
        if (set == null) {
            return false;
        }
        for (Type t : set) {
            action.action(t);
        }
        return true;
    }

    @Override
    public boolean putMember(String name, Type type) {
        javaClass.load();
        Set<Type> set = memberMap.get(name);
        if (set == null) {
            return false;
        }
        for (Type t : set) {
            if (t instanceof JavaObjectType) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<String> iterator() {
        javaClass.load();
        return memberMap.keySet().iterator();
    }

    @Override
    public Iterator<String> iteratorAll() {
        javaClass.load();
        return memberMap.keySet().iterator();
    }

    @Override
    public Iterator<Type> getMemberType(String name) {
        javaClass.load();
        Set<Type> set = memberMap.get(name);
        if (set == null) {
            return null;
        }
        return set.iterator();
    }

    private int constructCount = -1;

    private int[] args;

    private boolean[] isVarArgs;

    @Override
    public boolean newInstance(Action<Type> r, Rvalue i, Rvalue... args) {
        Class<?> c = javaClass.javaClass;
        if (c.isEnum() || c.isAnnotation()) {
            return false;
        }
        if (constructCount == -1) {
            Constructor[] cs = c.getConstructors();
            int l = cs.length;
            constructCount = l;
            int[] argCount = this.args = new int[l];
            boolean[] isVars = this.isVarArgs = new boolean[l];
            for (int t = 0; t < l; t++) {
                argCount[t] = cs[t].getParameters().length;
                isVars[t] = cs[t].isVarArgs();
            }
        }
        int length = args.length;
        int[] argCount = this.args;
        boolean[] isVars = this.isVarArgs;
        for (int t = 0, l = constructCount; t < l; t++) {
            int needLength = argCount[t];
            if (isVars[t] && needLength < length || needLength == length) {
                r.action(javaClass.objectType);
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDoc() {
        return javaClass.javaClass.getTypeName() + ".class";
    }

    @Override
    public String toString() {
        return "class " + javaClass.javaClass.getName();
    }

    @Override
    public int hashCode() {
        return javaClass.javaClass.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof JavaClassType) {
            return javaClass.javaClass.equals(((JavaClassType) obj).javaClass.javaClass);
        }
        return false;
    }

}
