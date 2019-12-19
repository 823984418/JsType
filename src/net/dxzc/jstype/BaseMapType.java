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
import net.dxzc.util.ActionSet;

/**
 * 一个基于{@link HashMap}的{@link Type}实现.
 *
 * @author 823984418@qq.com
 */
public class BaseMapType implements Type {

    /**
     * 构造一个空的类型.
     */
    public BaseMapType() {

    }

    /**
     * 用于保存成员的映射.
     */
    protected final Map<String, ActionSet<Type>> members = new HashMap<>();

    /**
     * 获取某个成员具有的类型. 如果不存在就构建或者返回{@code null}
     *
     * @param name 成员名字
     * @return 成员具有的类型或者{@code null}
     */
    protected ActionSet<Type> getMember(String name) {
        ActionSet<Type> m = members.get(name);
        if (m == null) {
            m = new ActionSet();
            members.put(name, m);
        }
        return m;
    }

    /**
     * 指示某个成员是否可以被枚举. 默认以{@code #}开头为不可枚举
     *
     * @param name 成员
     * @return 是否可枚举
     */
    protected boolean canEnum(String name) {
        return !name.startsWith("#");
    }

    @Override
    public boolean addMemberAction(String name, Action<Type> action) {
        ActionSet<Type> m = getMember(name);
        if (m == null) {
            return false;
        }
        m.addAction(action);
        return true;
    }

    @Override
    public boolean putMember(String name, Type type) {
        ActionSet<Type> m = getMember(name);
        if (m == null) {
            return false;
        }
        m.add(type);
        return true;
    }

    @Override
    public Iterator<String> iterator() {
        Set<String> r = new HashSet<>(members.keySet());
        for (Iterator<String> it = r.iterator(); it.hasNext();) {
            String s = it.next();
            if (!canEnum(s)) {
                it.remove();
            }
        }
        return r.iterator();
    }

    @Override
    public Iterator<String> iteratorAll() {
        return members.keySet().iterator();
    }

    @Override
    public Iterator<Type> getMemberType(String name) {
        ActionSet<Type> ts = members.get(name);
        if (ts == null) {
            return null;
        }
        return ts.iterator();
    }

    /**
     * 添加一个文档描述.
     *
     * @param s 描述
     */
    public void addDoc(String s) {
        doc = s + doc;
    }

    private String doc = "";

    @Override
    public String getDoc() {
        return doc;
    }

}
