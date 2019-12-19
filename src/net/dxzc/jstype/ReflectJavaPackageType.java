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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.dxzc.util.Action;

/**
 * 一个java包类型.
 *
 * @author 823984418@qq.com
 */
public class ReflectJavaPackageType implements Type {

    /**
     * 现在最大包层级以避免无限产生包操作.
     */
    public static int MAX_LEVEL = 8;

    /**
     * 构建一个顶层包.
     *
     * @param loader 类加载器
     */
    public ReflectJavaPackageType(ClassLoader loader) {
        name = "";
        this.loader = loader;
        level = 0;
    }

    /**
     * 构建一个包封装.
     *
     * @param loader 类加载器
     * @param name 包名
     * @param l 当前层级
     */
    public ReflectJavaPackageType(ClassLoader loader, String name, int l) {
        this.name = name;
        this.loader = loader;
        level = l;
    }

    /**
     * 包名.
     */
    public final String name;

    /**
     * 包层级.
     */
    public final int level;

    /**
     * 类加载器.
     */
    public final ClassLoader loader;

    private final Map<String, ReflectJavaClassType> clas = new HashMap<>();

    /**
     * 获取类.
     *
     * @param m 名字
     * @return 类封装
     */
    protected ReflectJavaClassType getClassType(String m) {
        String n = name.isEmpty() ? m : name + "." + m;
        ReflectJavaClassType ct = clas.get(n);
        if (ct != null) {
            return ct;
        }
        try {
            Class c = loader.loadClass(n);
            ct = new ReflectJavaClassType(c);
            clas.put(n, ct);
            return ct;
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    private final Map<String, ReflectJavaPackageType> pkgs = new HashMap<>();

    /**
     * 获取包.
     *
     * @param m 名字
     * @return 包封装
     */
    protected ReflectJavaPackageType getPackageType(String m) {
        if (level == MAX_LEVEL) {
            return null;
        }
        String n = name.isEmpty() ? m : name + "." + m;
        ReflectJavaPackageType pt = pkgs.get(m);
        if (pt == null) {
            pt = new ReflectJavaPackageType(loader, n, level + 1);
            pkgs.put(m, pt);
        }
        return pt;
    }

    @Override
    public boolean addMemberAction(String m, Action<Type> action) {
        ReflectJavaPackageType pt = getPackageType(m);
        if (pt != null) {
            action.action(pt);
        }
        ReflectJavaClassType ct = getClassType(m);
        if (ct != null) {
            action.action(ct);
        }
        return true;
    }

    @Override
    public boolean putMember(String name, Type type) {
        return false;
    }

    @Override
    public Iterator<String> iterator() {
        Set<String> set = new HashSet<>();
        set.addAll(clas.keySet());
        set.addAll(pkgs.keySet());
        return set.iterator();
    }

    @Override
    public Iterator<String> iteratorAll() {
        return iterator();
    }

    @Override
    public Iterator<Type> getMemberType(String name) {
        Set<Type> set = new HashSet<>();
        ReflectJavaPackageType pt = getPackageType(name);
        if (pt != null) {
            set.add(pt);
        }
        ReflectJavaClassType ct = getClassType(name);
        if (ct != null) {
            set.add(ct);
        }
        return set.iterator();
    }

    @Override
    public String getDoc() {
        return name;
    }

    @Override
    public String toString() {
        if (name.isEmpty()) {
            return "Packages";
        }
        return "Packages." + name;
    }

    @Override
    public int hashCode() {
        return name.hashCode() ^ loader.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof ReflectJavaPackageType) {
            return name.equals(((ReflectJavaPackageType) obj).name)
                    && loader.equals(((ReflectJavaPackageType) obj).loader);
        }
        return false;
    }

}
