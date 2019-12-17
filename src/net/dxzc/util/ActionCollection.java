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

/**
 * 一个附带动作的只增容器.
 *
 * @param <E> 类型
 * @author 823984418@qq.com
 */
public class ActionCollection<E> extends IncreaseCollection<E> {

    /**
     * 构造一个空的容器.
     */
    public ActionCollection() {

    }

    /**
     * 此容器所拥有的动作.
     */
    protected final IncreaseCollection<Action<? super E>> actions = new IncreaseCollection<>();

    @Override
    public boolean add(E e) {
        if (super.add(e)) {
            for (Action<? super E> action : actions) {
                action.action(e);
            }
            return true;
        }
        return false;
    }

    /**
     * 增加附带的动作. 可以为{@code null}
     *
     * @param action 动作
     */
    public void addAction(Action<? super E> action) {
        if (action != null && actions.add(action)) {
            for (E e : this) {
                action.action(e);
            }
        }
    }

}
