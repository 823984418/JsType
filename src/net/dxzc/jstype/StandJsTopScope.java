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

import java.util.HashMap;
import java.util.Map;

/**
 * 一个标准js环境.
 *
 * @author 823984418@qq.com
 */
public class StandJsTopScope extends JsTopScope {

    /**
     * 以默认的域类型构造环境.
     */
    public StandJsTopScope() {
        this(new JsScopeType());
    }

    /**
     * 以指定的类型构造环境.
     *
     * @param type
     */
    public StandJsTopScope(JsScopeType type) {
        super(type);

        JsType fun = new JsType("Function");
        map.put(FUNCTION, fun);
        JsNativeFunction ff = new JsNativeFunction("Function", fun);
        ff.extend(fun);
        type.putMember(FUNCTION, ff);

        JsType obj = new JsType("Object");
        map.put(OBJECT, obj);
        JsObjectFunction of = new JsObjectFunction(obj);
        of.extend(fun);
        type.putMember(OBJECT, of);

        fun.extend(obj);

        JsType arr = new JsType("Array");
        arr.extend(obj);
        map.put(ARRAY, arr);
        JsArrayFunction af = new JsArrayFunction(arr);
        af.extend(fun);
        type.putMember(ARRAY, af);

        buildClass(STRING, obj);
        buildClass(NUMBER, obj);
        buildClass(BOOLEAN, obj);

        buildClass(REGEXP, obj, "regex");
    }

    /**
     * 原型映射.
     */
    protected final Map<String, Type> map = new HashMap<>();

    @Override
    public Type getPrototype(String name) {
        Type r = map.get(name);
        if (r == null) {
            throw new RuntimeException();
        }
        return r;
    }

    /**
     * 快捷地添加一个原生函数.
     *
     * @param name 函数名
     * @param su 原型继承
     * @param args 形参表
     * @return 实例原型
     */
    public JsType buildClass(String name, JsType su, String... args) {
        JsType pro = new JsType(name);
        pro.extend(su);
        map.put(name, pro);
        JsNativeFunction f = new JsNativeFunction(name, pro);
        f.extend(getPrototype(FUNCTION));
        getScopeType().putMember(name, f);
        return pro;
    }

}
