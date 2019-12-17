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
 * 使类型以数组方式{@code [类型1|类型2]}呈现.
 *
 * @author 823984418@qq.com
 */
public class JsArrayType extends JsType {

    /**
     * 构造一个数组类型原型.
     */
    public JsArrayType() {
        super("Array");
    }

    @Override
    public String toString() {
        return containToString();
    }

}
