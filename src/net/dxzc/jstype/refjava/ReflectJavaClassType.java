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
package net.dxzc.jstype.refjava;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import net.dxzc.jstype.Rvalue;
import net.dxzc.jstype.Type;
import net.dxzc.util.Action;

/**
 * 类的静态部分原型.
 *
 * @author 823984418@qq.com
 */
public class ReflectJavaClassType implements Type {

    /**
     * 封装一个类的静态部分.
     *
     * @param c 封装的java类
     */
    public ReflectJavaClassType(Class c) {
        javaClass = c;
    }

    public final Class javaClass;

    private Set<String> members;

    private Constructor[] cs;

    @Override
    public boolean addMemberAction(String name, Action<Type> action) {
        if (THIS.equals(name)
                && javaClass.isMemberClass()
                && !Modifier.isStatic(javaClass.getModifiers())) {
            action.action(new ReflectJavaObjectType(javaClass.getDeclaringClass()));
        }
        for (Method m : javaClass.getMethods()) {
            if (Modifier.isStatic(m.getModifiers()) && m.getName().equals(name)) {
                action.action(new ReflectJavaMethodType(m));
            }
        }
        for (Field f : javaClass.getFields()) {
            if (Modifier.isStatic(f.getModifiers()) && f.getName().equals(name)) {
                action.action(new ReflectJavaObjectType(f.getType()));
            }
        }
        for (Class c : javaClass.getClasses()) {
            if (Modifier.isStatic(c.getModifiers()) && c.getSimpleName().equals(name)) {
                action.action(new ReflectJavaClassType(c));
            }
        }
        return true;
    }

    @Override
    public boolean putMember(String name, Type type) {
        if (THIS.equals(name)
                && javaClass.isMemberClass()
                && !Modifier.isStatic(javaClass.getModifiers())
                && type instanceof ReflectJavaObjectType
                && javaClass.getDeclaringClass().isAssignableFrom(((ReflectJavaObjectType) type).javaClass)) {
            return true;
        }
        for (Field f : javaClass.getFields()) {
            if (Modifier.isStatic(f.getModifiers()) && f.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<String> iterator() {
        Set<String> set = members;
        if (set == null) {
            set = new HashSet<>();
            boolean isInterface = javaClass.isInterface();
            for (Method m : javaClass.getMethods()) {
                if (isInterface || Modifier.isStatic(m.getModifiers())) {
                    set.add(m.getName());
                }
            }
            for (Field f : javaClass.getFields()) {
                if (Modifier.isStatic(f.getModifiers())) {
                    set.add(f.getName());
                }
            }
            for (Class c : javaClass.getClasses()) {
                if (Modifier.isStatic(c.getModifiers())) {
                    set.add(c.getSimpleName());
                }
            }
            members = set;
        }
        return set.iterator();
    }

    @Override
    public Iterator<String> iteratorAll() {
        return iterator();
    }

    @Override
    public Iterator<Type> getMemberType(String name) {
        Set<Type> set = new HashSet<>();
        for (Method m : javaClass.getMethods()) {
            if (Modifier.isStatic(m.getModifiers()) && m.getName().equals(name)) {
                set.add(new ReflectJavaMethodType(m));
            }
        }
        for (Field f : javaClass.getFields()) {
            if (Modifier.isStatic(f.getModifiers()) && f.getName().equals(name)) {
                set.add(new ReflectJavaObjectType(f.getType()));
            }
        }
        for (Class c : javaClass.getClasses()) {
            if (Modifier.isStatic(c.getModifiers()) && c.getSimpleName().equals(name)) {
                set.add(new ReflectJavaClassType(c));
            }
        }
        return set.iterator();
    }

    private Constructor[] getArgs() {
        Constructor[] cc = cs;
        if (cs == null) {
            cc = javaClass.getConstructors();
            cs = cc;
        }
        return cc;
    }

    @Override
    public boolean newInstance(Action<Type> r, Rvalue i, Rvalue... args) {
        if (javaClass.isEnum() || javaClass.isAnnotation()) {
            return false;
        }
        int length = args.length;
        for (Constructor c : getArgs()) {
            if (c.isVarArgs() || c.getParameterCount() == length) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDoc() {
        return javaClass.getTypeName() + ".class";
    }

    @Override
    public String toString() {
        return "class " + javaClass.getName();
    }

    @Override
    public int hashCode() {
        return javaClass.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof ReflectJavaClassType) {
            return javaClass.equals(((ReflectJavaClassType) obj).javaClass);
        }
        return false;
    }

    private static class OItr<E> implements Iterator<E> {

        private OItr(E o, int s) {
            obj = o;
            c = s;
        }

        private final E obj;

        private int c;

        @Override
        public boolean hasNext() {
            return c > 0;
        }

        @Override
        public E next() {
            if (c-- <= 0) {
                throw new NoSuchElementException();
            }
            return obj;
        }

    }

}
