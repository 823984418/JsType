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
package net.dxzc.jstype.exp;

import net.dxzc.jstype.JsTopScope;
import net.dxzc.jstype.Lvalue;
import net.dxzc.jstype.Rvalue;
import net.dxzc.jstype.Type;

/**
 * 以{@code a[b]}的形式访问的表达式.
 *
 * @author 823984418@qq.com
 */
public class ArrayGet extends Lvalue {

    /**
     * 构造一个{@code a[b]}表达式.
     *
     * @param scope 顶层域用于进行{@code Number}类型验证
     * @param target a
     * @param field b
     */
    public ArrayGet(JsTopScope scope, Rvalue target, Rvalue field) {
        field.forType(t -> {
            if (scope.getPrototype(JsTopScope.NUMBER).equals(t)) {
                target.forType(p -> p.addMemberAction(Type.CONTAIN, this::addType));
                forAssign(p -> target.forType(n -> n.putMember(Type.CONTAIN, p)));
            }
        });
    }

}
