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
import java.util.Map;
import net.dxzc.classfind.ClassFinder;
import net.dxzc.jstype.JsTopScope;
import net.dxzc.jstype.Type;

/**
 * javaAPI加载器.
 *
 * @author 823984418@qq.com
 */
public class JavaLoader {

    /**
     * 包字段.
     */
    public static final String PACKAGES = "Packages";

    /**
     * 以当前线程上下文类加载器构建.
     */
    public JavaLoader() {
        classLoader = Thread.currentThread().getContextClassLoader();
    }

    /**
     * 以指定的类加载器构建.
     *
     * @param loader 类加载器
     */
    public JavaLoader(ClassLoader loader) {
        classLoader = loader;
    }

    /**
     * 类加载器.
     */
    public final ClassLoader classLoader;

    /**
     * 创建的类.
     */
    protected final Map<Class<?>, JavaClass> classMap = new HashMap<>();

    /**
     * 得到实例.
     *
     * @param c 类
     * @return 类
     */
    protected JavaClass getJavaClass(Class<?> c) {
        if (c == Void.TYPE) {
            return null;
        }
        JavaClass r = classMap.get(c);
        if (r == null) {
            r = new JavaClass(this, c);
            classMap.put(c, r);
        }
        return r;
    }

    /**
     * 得到实例.
     *
     * @param name 类
     * @return 类
     */
    protected JavaClass getJavaClass(String name) {
        try {
            return getJavaClass(classLoader.loadClass(name));
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    /**
     * 得到实例.
     *
     * @param c 类
     * @return 类
     */
    protected JavaClassType getClassType(Class<?> c) {
        JavaClass r = getJavaClass(c);
        if (r == null) {
            return null;
        }
        return r.classType;
    }

    /**
     * 得到实例.
     *
     * @param name 类
     * @return 类
     */
    protected JavaClassType getClassType(String name) {
        JavaClass r = getJavaClass(name);
        if (r == null) {
            return null;
        }
        return r.classType;
    }

    /**
     * 得到实例.
     *
     * @param c 类
     * @return 类
     */
    protected JavaObjectType getObjectType(Class<?> c) {
        JavaClass r = getJavaClass(c);
        if (r == null) {
            return null;
        }
        return r.objectType;
    }

    /**
     * 得到实例.
     *
     * @param name 类
     * @return 类
     */
    protected JavaObjectType getObjectType(String name) {
        JavaClass r = getJavaClass(name);
        if (r == null) {
            return null;
        }
        return r.objectType;
    }

    /**
     * 初始化到指定的顶层域.
     *
     * @param scope 域
     */
    public void init(JsTopScope scope) {

        JavaPackageType pkgs = new JavaPackageType(this);

        for (Class<?> c : ClassFinder.BASE_FINDER) {
            if (!c.isMemberClass() && !c.isLocalClass() && !c.isAnonymousClass()) {
                pkgs.loadPackages(c.getName());
            }
        }

        Type type = scope.getScopeType();
        type.putMember(PACKAGES, pkgs);
        pkgs.addMemberAction("java", t -> type.putMember("java", t));
        pkgs.addMemberAction("javax", t -> type.putMember("javax", t));
        pkgs.addMemberAction("org", t -> type.putMember("org", t));
        pkgs.addMemberAction("com", t -> type.putMember("com", t));
        pkgs.addMemberAction("edu", t -> type.putMember("edu", t));
        pkgs.addMemberAction("net", t -> type.putMember("net", t));
        if ("Dalvik".equals(System.getProperty("java.vm.name"))) {
            pkgs.addMemberAction("android", t -> type.putMember("android", t));
        }
    }

}
