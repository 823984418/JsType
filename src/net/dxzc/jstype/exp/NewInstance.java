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

import net.dxzc.jstype.JsScope;
import net.dxzc.jstype.Rvalue;

/**
 * 形如{@code new a()}的表达式.
 *
 * @author 823984418@qq.com
 */
public class NewInstance extends Rvalue {

    /**
     * 构造表达式.
     *
     * @param scope 调用域
     * @param target 目标
     * @param args 实参表
     */
    public NewInstance(JsScope scope, Rvalue target, Rvalue... args) {
        int l = args.length;
//        if (target instanceof Get) {
//            ((Get) target).call();
//        }
        target.forType(t -> t.newInstance(this::addType, scope, args));
    }

}
