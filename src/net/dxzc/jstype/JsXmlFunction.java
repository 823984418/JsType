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

import net.dxzc.util.Action;

/**
 * XML对象构造器. 以{@link EmptyType}作为XML实例类型
 *
 * @author 823984418@qq.com
 */
public class JsXmlFunction extends JsNativeFunction {

    /**
     * 在指定域中初始化XML.
     *
     * @param scope 域
     */
    public static void init(JsTopScope scope) {
        Type xml = new EmptyType(JsTopScope.XML);
        scope.putPrototypr(JsTopScope.XML, xml);
        scope.getScopeType().putMember("XML", new JsXmlFunction());
    }

    /**
     * 新建一个XML构造器.
     */
    public JsXmlFunction() {
        super("XML", new JsType("XML"), "xmlString");
    }

    @Override
    public boolean newInstance(Action<Type> r, Rvalue i, Rvalue... args) {
        r.action(new EmptyType("XML"));
        return true;
    }

}
