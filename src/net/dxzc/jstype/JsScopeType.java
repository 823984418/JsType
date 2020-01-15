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
 * 环境域的类型.
 *
 * @author 823984418@qq.com
 */
public class JsScopeType extends BaseMapType {

    /**
     * 构建一个域类型.具有默认的{@code "Scope"}作为名称
     */
    public JsScopeType() {
        name = "Scope";
    }

    /**
     * 名称.
     */
    protected String name;

    /**
     * 初始化一个变量.
     *
     * @param name 变量名
     */
    public void let(String name) {
        putMember(name, null);
    }

    /**
     * 查找是否有指定变量.
     *
     * @param name 变量名
     * @return 结果
     */
    public boolean has(String name) {
        return members.containsKey(name);
    }

    @Override
    public String toString() {
        return name;
    }

}
