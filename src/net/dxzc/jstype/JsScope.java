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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 一个环境作用域. 只能有作用域类型作为唯一一个类型
 *
 * @author 823984418@qq.com
 */
public class JsScope extends Rvalue {

    /**
     * 构造一个作用域.
     *
     * @param type 作用域类型
     * @param parent 父域
     * @param isScope 是否可为var对象
     */
    public JsScope(JsScopeType type, JsScope parent, boolean isScope) {
        super.addType(type);
        superScope = parent;
        if (parent != null) {
            topScope = parent.getTopScope();
        } else {
            topScope = (JsTopScope) this;
        }
        this.isScope = isScope;
    }

    /**
     * 归属的父域.
     */
    public final JsScope superScope;

    /**
     * 归属的顶层域.
     */
    public final JsTopScope topScope;

    /**
     * 是否为顶层域或者函数域(var的对象).
     */
    public final boolean isScope;

    @Override
    public boolean addType(Type type) {
        return false;
    }

    /**
     * 获取包含的类型.
     *
     * @return 包含的类型
     */
    public JsScopeType getScopeType() {
        Iterator<Type> t = iterator();
        if (!t.hasNext()) {
            throw new RuntimeException();
        }
        JsScopeType r = (JsScopeType) t.next();
        if (t.hasNext()) {
            throw new RuntimeException();
        }
        return r;
    }

    /**
     * 获取其可以作为var的对象.
     *
     * @return 可以作为var对象的域
     */
    public JsScope getScope() {
        if (isScope) {
            return this;
        }
        return superScope.getScope();
    }

    /**
     * 获取其顶层域.
     *
     * @return 顶层域
     */
    public JsTopScope getTopScope() {
        return topScope;
    }

    /**
     * 初始化此域的一个变量.
     *
     * @param name 变量名
     */
    public void let(String name) {
        getScopeType().let(name);
    }

    /**
     * 查找变量定义.
     *
     * @param name 变量名
     * @return 定义域
     */
    public JsScope find(String name) {
        if (superScope == null || getScopeType().has(name)) {
            return this;
        }
        return superScope.find(name);
    }

    /**
     * 设置器类型的名字.
     *
     * @param name 名字
     */
    public void setTypeName(String name) {
        getScopeType().name = name;
    }

    /**
     * 列出作用域链上的变量.
     *
     * @return 变量名集合
     */
    public Set<String> listVariable() {
        Set<String> set = new HashSet<>();
        for (JsScope s = this; s != null; s = s.superScope) {
            set.addAll(s.fields());
        }
        return set;
    }

}
