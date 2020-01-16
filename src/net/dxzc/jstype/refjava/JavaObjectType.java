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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.dxzc.jstype.Type;
import net.dxzc.util.Action;

/**
 * 实例类型.
 *
 * @author 823984418@qq.com
 */
public class JavaObjectType implements Type {

    /**
     * 构建一个实例类型.
     *
     * @param javaClass 类
     */
    protected JavaObjectType(JavaClass javaClass) {
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

    /**
     * 包含类型.
     */
    protected JavaObjectType contain;

    @Override
    public boolean addMemberAction(String name, Action<Type> action) {
        javaClass.load();
        if (Type.CONTAIN.equals(name) && contain != null) {
            action.action(contain);
            return true;
        }
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
    public boolean isNumberType() {
        Class<?> c = javaClass.javaClass;
        if (c == char.class || c == short.class
                || c == int.class || c == long.class
                || c == float.class || c == double.class) {
            return true;
        }
        return false;
    }

    @Override
    public Iterator<Type> getMemberType(String name) {
        javaClass.load();
        if (Type.CONTAIN.equals(name) && contain != null) {
            return Arrays.<Type>asList(contain).iterator();
        }
        Set<Type> set = memberMap.get(name);
        if (set == null) {
            return null;
        }
        return set.iterator();
    }

    @Override
    public String getDoc() {
        Class<?> cl = javaClass.javaClass;
        if (cl.isArray()) {
            int dimensions = 0;
            while (cl.isArray()) {
                dimensions++;
                cl = cl.getComponentType();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(cl.getName());
            for (int i = 0; i < dimensions; i++) {
                sb.append("[]");
            }
            return sb.toString();
        }
        return cl.getName();
    }

    @Override
    public String toString() {
        return getDoc();
    }

    @Override
    public int hashCode() {
        return -javaClass.javaClass.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof JavaObjectType) {
            return javaClass.javaClass.equals(((JavaObjectType) obj).javaClass.javaClass);
        }
        return false;
    }

}
