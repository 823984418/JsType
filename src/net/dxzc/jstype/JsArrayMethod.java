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
 * 返回数组的方法需要特殊实现以避免反向传播.
 *
 * @author 823984418@qq.com
 */
public class JsArrayMethod extends JsNativeFunction {

    /**
     * 构造方法.
     *
     * @param prototype 对应原型
     */
    public JsArrayMethod(String name, JsType prototype, Type arrayType, String... args) {
        super(name, prototype, args);
        this.arrayType = arrayType;
        putMember(RETURN, arrayType);
    }

    private final Type arrayType;

    @Override
    public void doInvoke(Action<Type> action) {
        JsArrayType t = new JsArrayType();
        t.extend(arrayType);
        action.action(t);
    }

}
