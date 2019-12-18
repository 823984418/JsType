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
 * 一个动作.
 *
 * @param <E> 动作的对象
 * @author 823984418@qq.com
 */
@FunctionalInterface
public interface Action<E> {

    /**
     * 对元素执行此动作.
     *
     * @param obj 元素
     */
    public void action(E obj);

}
