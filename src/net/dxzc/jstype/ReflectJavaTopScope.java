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

import net.dxzc.jstype.exp.Assign;
import net.dxzc.jstype.exp.Get;

/**
 * 具有基于反射的java类型包环境的顶层域.
 *
 * @author 823984418@qq.com
 */
public class ReflectJavaTopScope extends StandJsTopScope {

    private static void set(Rvalue v, String n, Rvalue e) {
        new Assign(new Get(v, n), e);
    }

    /**
     * 包字段.
     */
    public static final String PACKAGES = "Packages";

    /**
     * 以当前线程上下文类加载器构建.
     */
    public ReflectJavaTopScope() {
        this(new JsScopeType());
    }

    /**
     * 以指定类加载器构建.
     *
     * @param type
     */
    public ReflectJavaTopScope(JsScopeType type) {
        super(type);
        Type pkgs = new ReflectJavaPackageType(Thread.currentThread().getContextClassLoader());
        pkgs.addMemberAction("java", t -> type.putMember("java", t));
        pkgs.addMemberAction("org", t -> type.putMember("org", t));
        pkgs.addMemberAction("com", t -> type.putMember("com", t));
        pkgs.addMemberAction("net", t -> type.putMember("net", t));
        map.put(PACKAGES, pkgs);
    }

}
