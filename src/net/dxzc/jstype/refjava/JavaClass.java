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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.dxzc.jstype.Type;

/**
 * 类和实例的成员加载者.
 *
 * @author 823984418@qq.com
 */
public class JavaClass {

    /**
     * 以指定的加载器构建指定的类.
     *
     * @param loader 加载器
     * @param c 类
     */
    protected JavaClass(JavaLoader loader, Class<?> c) {
        loaded = false;
        javaLoader = loader;
        javaClass = c;
        classType = new JavaClassType(this);
        objectType = new JavaObjectType(this);
    }

    /**
     * 加载器.
     */
    protected final JavaLoader javaLoader;

    /**
     * 类.
     */
    public final Class<?> javaClass;

    /**
     * 类类型.
     */
    public final JavaClassType classType;

    /**
     * 实例类型.
     */
    public final JavaObjectType objectType;

    private boolean loaded;

    private void addMember(String name, boolean isStatic, Type type) {
        Map<String, Set<Type>> map;
        Set<Type> set;
        if (isStatic) {
            map = classType.memberMap;
        } else {
            map = objectType.memberMap;
        }
        set = map.get(name);
        if (set == null) {
            set = new HashSet<>();
            map.put(name, set);
        }
        set.add(type);
    }

    /**
     * 加载成员.
     */
    protected void load() {
        if (loaded) {
            return;
        }
        loaded = true;
        boolean isInterface = javaClass.isInterface();
        for (Method m : javaClass.getMethods()) {
            String name = m.getName();
            boolean isStatic = Modifier.isStatic(m.getModifiers()) && !isInterface;
            if (isStatic) {
                addMember(name, isStatic, new JavaMethodType(javaLoader, m));
            } else {
                addMember(name, isStatic, new JavaMethodType(javaLoader, m));
            }
        }
        for (Field f : javaClass.getFields()) {
            String name = f.getName();
            boolean isStatic = Modifier.isStatic(f.getModifiers()) || isInterface;
            addMember(name, isStatic, javaLoader.getObjectType(f.getType()));
        }
        for (Class c : javaClass.getClasses()) {
            String name = c.getSimpleName();
            boolean isStatic = Modifier.isStatic(c.getModifiers()) || isInterface;
            addMember(name, isStatic, javaLoader.getClassType(c));
        }
    }

}
