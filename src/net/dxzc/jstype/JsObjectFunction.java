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
 * {@code Object}原型需要特殊实现以禁止实例字段反向传播.
 *
 * @author 823984418@qq.com
 */
public class JsObjectFunction extends JsNativeFunction {

    /**
     * 构建{@code function Object(){}}对象.
     *
     * @param prototype 实例原型
     */
    public JsObjectFunction(JsType prototype) {
        super("Object", prototype);
    }

    @Override
    public void doNewInstance(Action<Type> action) {
        JsType t = new JsType("Object");
        t.extend(prototype);
        action.action(t);
    }

}
