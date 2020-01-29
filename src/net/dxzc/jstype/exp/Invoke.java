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

import net.dxzc.jstype.Lvalue;
import net.dxzc.jstype.Rvalue;

/**
 * 形如{@code a()}的表达式.
 *
 * @author 823984418@qq.com
 */
public class Invoke extends Lvalue {

    /**
     * 构造表达式.
     *
     * @param target 目标
     * @param args 实参表
     */
    public Invoke(Rvalue target, Rvalue... args) {
        int l = args.length;
        if (target instanceof Get) {
            target.forType(t -> t.invoke(this::addType, ((Get) target).target, args));
//            Get g = (Get) target;
//            g.target.forType(t -> {
//                t.addMemberAction(g.name, c -> {
//                    c.invoke(this::addType, new Rvalue(t), args);
//                });
//            });//此写法会导致部分内联函数无穷递归
        } else {
            target.forType(t -> t.invoke(this::addType, null, args));
        }
    }

}
