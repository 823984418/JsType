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

/**
 * 环境作用域工厂.
 *
 * @author 823984418@qq.com
 */
public class JsScopeFactory {

    /**
     * 构建一个顶层域.
     *
     * @return 顶层域
     */
    public JsTopScope buildTopScope() {
        return new StandJsTopScope();
    }

    /**
     * 构建一个函数域.
     *
     * @param scope 父域
     * @return 函数域
     */
    public JsScope buildScope(JsScope scope) {
        return new JsScope(new JsScopeType(), scope, true);
    }

    /**
     * 构建一个块域.
     *
     * @param scope 父域
     * @return 块域
     */
    public JsScope buildBlock(JsScope scope) {
        return new JsScope(new JsScopeType(), scope, false);
    }

}
