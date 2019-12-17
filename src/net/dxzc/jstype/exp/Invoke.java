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

import java.util.Iterator;
import net.dxzc.jstype.Lvalue;
import net.dxzc.jstype.Rvalue;
import net.dxzc.jstype.Type;
import net.dxzc.util.Action;

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
            ((Get) target).call();
        }
        target.forType(t -> {
            Iterator<Action<Type>> d = t.invoke(l);
            if (d != null) {
                t.doInvoke(this::addType);
                for (int i = 0; i < l; i++) {
                    if (!d.hasNext()) {
                        throw new RuntimeException();
                    }
                    args[i].forType(d.next());
                }
            }
        });
    }

}
