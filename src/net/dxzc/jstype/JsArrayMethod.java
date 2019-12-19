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
 * 返回数组的方法需要特殊实现以避免反向传播及类型复制.
 *
 * @author 823984418@qq.com
 */
public class JsArrayMethod extends JsNativeFunction {

    /**
     * 构造方法.
     *
     * @param name 名字
     * @param prototype 对应原型
     * @param arrayType 数组类型
     * @param args 形参表
     */
    public JsArrayMethod(String name, JsType prototype, Type arrayType, String... args) {
        super(name, prototype, args);
        this.arrayType = arrayType;
        putMember(RETURN, arrayType);
    }

    private final Type arrayType;

    @Override
    public boolean invoke(Action<Type> r, Rvalue i, Rvalue... args) {
        JsArrayType array = new JsArrayType();
        addMemberAction(RETURN, t -> array.extend(t));
        i.forType(t -> array.extend(t));
        r.action(array);
        return true;
    }

}
