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
import net.dxzc.util.Action;
import net.dxzc.util.ActionSet;

/**
 * 一个右值.
 *
 * @author 823984418@qq.com
 */
public class Rvalue implements Iterable<Type> {

    /**
     * 构造一个空的右值.
     */
    public Rvalue() {

    }

    /**
     * 构造一个已经有指定类型的右值.
     *
     * @param ts 初始的类型
     */
    public Rvalue(Type... ts) {
        for (Type t : ts) {
            addType(t);
        }
    }

    /**
     * 所具有的类型.
     */
    private final ActionSet<Type> types = new ActionSet<>();

    /**
     * 对类型附加动作.
     *
     * @param action 动作
     */
    public void forType(Action<Type> action) {
        types.addAction(action);
    }

    /**
     * 添加一种类型.
     *
     * @param type 类型
     * @return 是否添加
     */
    public boolean addType(Type type) {
        if (type == null) {
            return false;
        }
        return types.add(type);
    }

    @Override
    public Iterator<Type> iterator() {
        return types.iterator();
    }

    @Override
    public String toString() {
        return Type.typesToString(types.iterator());
    }

    /**
     * 列出字段.
     *
     * @return 可能的字段
     */
    public Set<String> fields() {
        Set<String> s = new HashSet<>();
        for (Type t : types) {
            for (String n : t) {
                s.add(n);
            }
        }
        return s;
    }

}
