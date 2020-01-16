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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.dxzc.jstype.Type;
import net.dxzc.util.Action;

/**
 * java包.
 *
 * @author 823984418@qq.com
 */
public class JavaPackageType implements Type {

    /**
     * 限制最大包层级以避免无限产生包操作.
     */
    public static int MAX_LEVEL = 8;

    /**
     * 构建一个顶层包.
     *
     * @param loader 类加载器
     */
    public JavaPackageType(JavaLoader loader) {
        name = "";
        this.loader = loader;
        level = 0;
    }

    protected JavaPackageType(JavaLoader loader, String name, int l) {
        this.name = name;
        this.loader = loader;
        level = l;
    }

    protected final JavaLoader loader;

    /**
     * 包名.
     */
    public final String name;

    /**
     * 包层级.
     */
    public final int level;

    private final Map<String, JavaPackageType> childs = new HashMap<>();

    private final Set<String> classes = new HashSet<>();

    /**
     * 在顶层包上加载包路径. 只能调用于顶层包,即使传入的是类路径,也当作包对待而取得相似的行为
     *
     * @param ns 包列表
     */
    public void loadPackages(String... ns) {
        if (!name.isEmpty()) {
            throw new RuntimeException();
        }
        for (String n : ns) {
            JavaPackageType p = this;
            for (String f : n.split("\\.")) {
                p = p.getPackageType(f);
            }
        }
    }

    /**
     * 获取包.
     *
     * @param m 名字
     * @return 包封装
     */
    protected JavaPackageType getPackageType(String m) {
        if (level == MAX_LEVEL) {
            return null;
        }
        String n = name.isEmpty() ? m : name + "." + m;
        JavaPackageType pt = childs.get(m);
        if (pt == null) {
            pt = new JavaPackageType(loader, n, level + 1);
            childs.put(m, pt);
        }
        return pt;
    }

    @Override
    public boolean addMemberAction(String name, Action<Type> action) {
        JavaPackageType p = getPackageType(name);
        if (p != null) {
            action.action(p);
        }
        String n = this.name.isEmpty() ? name : this.name + "." + name;
        JavaClassType t = loader.getClassType(n);
        if (t != null) {
            classes.add(t.javaClass.javaClass.getSimpleName());
            action.action(t);
        }
        return true;
    }

    @Override
    public boolean putMember(String name, Type type) {
        return false;
    }

    @Override
    public Iterator<String> iterator() {
        Set<String> s = new HashSet<>();
        s.addAll(childs.keySet());
        s.addAll(classes);
        return s.iterator();
    }

    @Override
    public Iterator<String> iteratorAll() {
        return iterator();
    }

    @Override
    public Iterator<Type> getMemberType(String name) {
        Set<Type> s = new HashSet<>();
        JavaPackageType p = getPackageType(name);
        if (p != null) {
            s.add(p);
        }
        JavaClassType t = loader.getClassType(name);
        if (t != null) {
            classes.add(t.javaClass.javaClass.getSimpleName());
            s.add(t);
        }
        return s.iterator();
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
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof JavaPackageType) {
            return name.equals(((JavaPackageType) obj).name);
        }
        return false;
    }

}
