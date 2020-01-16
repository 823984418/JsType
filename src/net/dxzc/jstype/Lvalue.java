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

import net.dxzc.util.Action;
import net.dxzc.util.ActionSet;

/**
 * 一个左值.
 *
 * @author 823984418@qq.com
 */
public class Lvalue extends Rvalue {

    /**
     * 构造一个空的左值.
     */
    public Lvalue() {
        assigns.addAction(this::addType);
    }

    /**
     * 所有赋予的值.
     */
    private final ActionSet<Type> assigns = new ActionSet<>();

    /**
     * 向其赋值.
     *
     * @param type 值
     * @return 是否有改变
     */
    public boolean assign(Type type) {
        if (type == null) {
            return false;
        }
        return assigns.add(type);
    }

    /**
     * 对所有赋予的值.
     *
     * @param action 执行动作
     */
    public void forAssign(Action<Type> action) {
        assigns.addAction(action);
    }

}
