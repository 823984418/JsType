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
 * 一个顶层域.可以查找指定的类型原型
 *
 * @author 823984418@qq.com
 */
public abstract class JsTopScope extends JsScope {

    public static final String NUMBER = "Number";

    public static final String STRING = "String";

    public static final String OBJECT = "Object";

    public static final String ARRAY = "Array";

    public static final String BOOLEAN = "Boolean";

    public static final String FUNCTION = "Function";
    
    public static final String MATH = "Math";

    public static final String REGEXP = "RegExp";

    /**
     * 构造一个顶层域.
     *
     * @param type 域类型
     */
    public JsTopScope(JsScopeType type) {
        super(type, null, true);
    }

    /**
     * 查找原型.
     *
     * @param name 原型名称
     * @return 结果
     */
    public abstract Type getPrototype(String name);

}
