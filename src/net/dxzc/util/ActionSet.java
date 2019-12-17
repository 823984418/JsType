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
package net.dxzc.util;

import java.util.HashSet;
import java.util.Set;

/**
 * 使用{@link Set}维护的附带动作只增集合.
 *
 * @param <E> 类型
 * @author 823984418@qq.com
 */
public class ActionSet<E> extends ActionCollection<E> implements Set<E> {

    /**
     * 构造一个空的集合
     */
    public ActionSet() {
        set = new HashSet<>();
    }

    /**
     * 以指定集合作为检查器和初始值构造集合.
     *
     * @param set 检查集合
     */
    public ActionSet(Set<E> set) {
        for (E e : set) {
            super.add(e);
        }
        this.set = set;
    }

    /**
     * 用于检查重复性的集合.
     */
    protected final Set<E> set;

    /**
     * 添加一个元素. 尝试向{@link set}添加,如果成功,则继续执行实际添加
     *
     * @param e 元素
     * @return 是否有改变
     */
    @Override
    public boolean add(E e) {
        if (set.add(e)) {
            return super.add(e);
        }
        return false;
    }

    /**
     * 使用{@link set}检查是否已经拥有指定元素.
     *
     * @param o 元素
     * @return 是否拥有
     */
    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

}
