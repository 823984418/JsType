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

/**
 * {@code Array}原型需要特殊实现以禁止实例字段反向传播.
 *
 * @author 823984418@qq.com
 */
public class JsArrayFunction extends JsNativeFunction {

    /**
     * 构造一个{@code function Array(){}}对象.
     *
     * @param prototype 对应原型
     */
    public JsArrayFunction(JsType prototype) {
        super("Array", prototype);
    }

    @Override
    public boolean newInstance(Action<Type> r, Rvalue i, Rvalue... args) {
        JsArrayType array = new JsArrayType();
        addMemberAction(NEW, t -> array.extend(t));
        r.action(array);
        return true;
    }

}
