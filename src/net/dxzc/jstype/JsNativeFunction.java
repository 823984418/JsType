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
 * 不带环境作用域的函数用于原生函数.
 *
 * @author 823984418@qq.com
 */
public class JsNativeFunction extends JsFunction {

    /**
     * 构造原生函数.
     *
     * @param name 函数名
     * @param prototype 实例原型
     * @param as 形参表
     */
    public JsNativeFunction(String name, JsType prototype, String... as) {
        super(name, null, prototype, as);
    }

}
