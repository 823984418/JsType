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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.dxzc.util.Action;

/**
 * 封装一个类的实例部分.
 *
 * @author 823984418@qq.com
 */
public class ReflectJavaObjectType implements Type {

    /**
     * 封装一个类实例.
     *
     * @param c
     */
    public ReflectJavaObjectType(Class c) {
        javaClass = c;
    }

    /**
     * 封装的类.
     */
    public final Class javaClass;

    private Set<String> members;

    @Override
    public boolean addMemberAction(String name, Action<Type> action) {
        if (Type.CONTAIN.equals(name)) {
            if (javaClass.isArray()) {
                action.action(new ReflectJavaObjectType(javaClass.getComponentType()));
                return true;
            } else {
                return false;
            }
        }
        boolean isInterface = javaClass.isInterface();
        for (Method m : javaClass.getMethods()) {
            if ((isInterface || !(Modifier.isStatic(m.getModifiers()))) && m.getName().equals(name)) {
                action.action(new ReflectJavaMethodType(m));
            }
        }
        for (Field f : javaClass.getFields()) {
            if (!(Modifier.isStatic(f.getModifiers())) && f.getName().equals(name)) {
                action.action(new ReflectJavaObjectType(f.getType()));
            }
        }
        for (Class c : javaClass.getClasses()) {
            if (!(Modifier.isStatic(c.getModifiers())) && c.getSimpleName().equals(name)) {
                action.action(new ReflectJavaClassType(c));
            }
        }
        return true;
    }

    @Override
    public boolean putMember(String name, Type type) {
        return false;
    }

    @Override
    public Iterator<String> iterator() {
        Set<String> set = members;
        if (set == null) {
            set = new HashSet<>();
            boolean isInterface = javaClass.isInterface();
            for (Method m : javaClass.getMethods()) {
                if (isInterface || !Modifier.isStatic(m.getModifiers())) {
                    set.add(m.getName());
                }
            }
            for (Field f : javaClass.getFields()) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    set.add(f.getName());
                }
            }
            for (Class c : javaClass.getClasses()) {
                if (!Modifier.isStatic(c.getModifiers())) {
                    set.add(c.getSimpleName());
                }
            }
            members = set;
        }
        return set.iterator();
    }

    @Override
    public Iterator<Type> getMemberType(String name) {
        boolean isInterface = javaClass.isInterface();
        Set<Type> set = new HashSet<>();
        for (Method m : javaClass.getMethods()) {
            if ((isInterface || !Modifier.isStatic(m.getModifiers())) && m.getName().equals(name)) {
                set.add(new ReflectJavaMethodType(m));
            }
        }
        for (Field f : javaClass.getFields()) {
            if (!(Modifier.isStatic(f.getModifiers())) && f.getName().equals(name)) {
                set.add(new ReflectJavaObjectType(f.getType()));
            }
        }
        for (Class c : javaClass.getClasses()) {
            if (!(Modifier.isStatic(c.getModifiers())) && c.getSimpleName().equals(name)) {
                set.add(new ReflectJavaClassType(c));
            }
        }
        return set.iterator();
    }

    @Override
    public String toString() {
        return javaClass.getTypeName();
    }

    @Override
    public int hashCode() {
        return -javaClass.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof ReflectJavaObjectType) {
            return javaClass.equals(((ReflectJavaObjectType) obj).javaClass);
        }
        return false;
    }

}
