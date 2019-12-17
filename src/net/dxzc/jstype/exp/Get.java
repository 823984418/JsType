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
import net.dxzc.jstype.Type;

/**
 * 形如{@code a.b}的表达式.
 *
 * @author 823984418@qq.com
 */
public class Get extends Lvalue {

    /**
     * 构造表达式.
     *
     * @param target 对象
     * @param name 字段
     */
    public Get(Rvalue target, String name) {
        this.target = target;
        this.name = name;
        target.forType(t -> {
            t.addMemberAction(name, this::addType);
            forAssign(p -> t.putMember(name, p));
        });
    }

    private final Rvalue target;

    private final String name;

    private boolean beCall;

    /**
     * 适用于{@code a.b()}的调用中用于绑定{@code b}的作用域{@code this}.
     */
    protected void call() {
        if (!beCall) {
            beCall = true;
            target.forType(
                    t -> t.addMemberAction(name,
                            f -> f.putMember(Type.THIS, t)));
        }
    }

}
