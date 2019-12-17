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
import net.dxzc.jstype.Rvalue;

/**
 * 形如{@code a + b}的表达式.
 *
 * @author 823984418@qq.com
 */
public class Add extends Rvalue {

    /**
     * 构建表达式.
     *
     * @param scope 用于获取{@link JsTopScope#NUMBER}和{@link JsTopScope#STRING}
     * @param a 左侧值
     * @param b 右侧值
     */
    public Add(JsTopScope scope, Rvalue a, Rvalue b) {
        a.forType(t -> {
            if (scope.getPrototype(JsTopScope.NUMBER).equals(t)) {
                l = true;
                if (l && r) {
                    addType(scope.getPrototype(JsTopScope.NUMBER));
                }
            } else {
                addType(scope.getPrototype(JsTopScope.STRING));
            }
        });
        b.forType(t -> {
            if (scope.getPrototype(JsTopScope.NUMBER).equals(t)) {
                r = true;
                if (l && r) {
                    addType(scope.getPrototype(JsTopScope.NUMBER));
                }
            } else {
                addType(scope.getPrototype(JsTopScope.STRING));
            }
        });
    }

    private boolean l, r;

}
