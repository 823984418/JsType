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
     * @param type 域类型
     */
    public StandJsTopScope(JsScopeType type) {
        super(type);

        JsType fun = new JsType("Function");
        map.put(FUNCTION, fun);
        JsNativeFunction funf = new JsNativeFunction("Function", fun);
        funf.extend(fun);
        type.putMember(FUNCTION, funf);

        JsType obj = new JsType("Object");
        map.put(OBJECT, obj);
        JsObjectFunction objf = new JsObjectFunction(obj);
        objf.extend(fun);
        type.putMember(OBJECT, objf);

        fun.extend(obj);

        JsType arr = new JsType("Array");
        arr.extend(obj);
        map.put(ARRAY, arr);
        JsArrayFunction arrf = new JsArrayFunction(arr);
        arrf.extend(fun);
        type.putMember(ARRAY, arrf);

        JsNativeFunction strf = buildClass(STRING, obj, "value");
        Type str = getPrototype(STRING);
        JsNativeFunction numf = buildClass(NUMBER, obj, "value");
        Type num = getPrototype(NUMBER);
        JsNativeFunction boof = buildClass(BOOLEAN, obj, "value");
        Type boo = getPrototype(BOOLEAN);
        JsNativeFunction regf = buildClass(REGEXP, obj, "regex");
        Type reg = getPrototype(REGEXP);
        JsNativeFunction datef = buildClass(DATE, obj, "milliseconds");
        Type date = getPrototype(DATE);
        JsNativeFunction errf = buildClass(ERROR, obj, "message");
        Type err = getPrototype(ERROR);

        JsType math = new JsType("Math");
        map.put(MATH, math);
        type.putMember(MATH, math);

        method(fun, "apply", obj, "thisArg", "argArrayOpt");
        method(fun, "call", obj, "thisArg", "...args");
        method(fun, "toSource", str);
        method(fun, "toString", str);
        method(fun, "valueOf", str);

        method(arr, "pop", obj);
        method(arr, "push", num, "element");
        method(arr, "reverse", null);
        method(arr, "shift", obj);
        method(arr, "sort", null, "compareFunction");
        method(arr, "splice", null, "index", "howMany", "...element");
        method(arr, "unshift", num, "...element");
        method(arr, "concat", null, "...value");
        method(arr, "indexOf", num, "searchElement");
        method(arr, "join", str, "separatorOpt");
        method(arr, "lastIndexOf", num, "searchElement");
        arrayMethod(arr, "slice", "begin", "endOpt");
        method(arr, "toString", str);
        method(arr, "valueOf", str);
        arrayMethod(arr, "filter", "callback");
        method(arr, "forEach", null, "callback");
        method(arr, "every", boo, "callback");
        arrayMethod(arr, "map", "callback");
        method(arr, "some", boo, "callback");

        math.putMember("E", num);
        math.putMember("LN2", num);
        math.putMember("LN10", num);
        math.putMember("LOG2E", num);
        math.putMember("LOG10E", num);
        math.putMember("PI", num);
        math.putMember("SQRT1_2", num);
        math.putMember("SQRT2", num);
        method(math, "abs", num, "x");
        method(math, "acos", num, "x");
        method(math, "asin", num, "x");
        method(math, "atan", num, "x");
        method(math, "atan2", num, "x");
        method(math, "ceil", num, "x");
        method(math, "cos", num, "x");
        method(math, "exp", num, "x");
        method(math, "floor", num, "x");
        method(math, "log", num, "x");
        method(math, "max", num, "a", "b");
        method(math, "min", num, "a", "b");
        method(math, "pow", num, "base", "exponent");
        method(math, "random", num);
        method(math, "round", num, "x");
        method(math, "sin", num, "x");
        method(math, "sqrt", num, "x");
        method(math, "tan", num, "x");

        datef.putMember("now", date);
        method(date, "getDate", num);
        method(date, "getDay", num);
        method(date, "getFullYear", num);
        method(date, "getHours", num);
        method(date, "getMilliseconds", num);
        method(date, "getMinutes", num);
        method(date, "getMonth", num);
        method(date, "getSeconds", num);
        method(date, "getTime", num);
        method(date, "getTimezoneOffset", num);
        method(date, "getUTCDate", num);
        method(date, "getUTCDay", num);
        method(date, "getUTCFullYear", num);
        method(date, "getUTCHours", num);
        method(date, "getUTCMilliseconds", num);
        method(date, "getUTCMinutes", num);
        method(date, "getUTCMonth", num);
        method(date, "getUTCSeconds", num);
        method(date, "getYear", num);
        method(date, "setDate", null, "dayValue");
        method(date, "setFullYear", null, "yearValue", "monthValueOpt", "dayValueOpt");
        method(date, "setHours", null, "hoursValue", "minutesValueOpt", "secondsValueOpt", "msValueOpt");
        method(date, "setMilliseconds", null, "millisecondsValue");
        method(date, "setMinutes", null, "minutesValueOpt", "secondsValueOpt", "msValueOpt");
        method(date, "setMonth", null, "monthValue", "dayValueOpt");
        method(date, "setSeconds", null, "secondsValue", "msValueOpt");
        method(date, "setTime", null, "timeValue");
        method(date, "setUTCDate", null, "dayValue");
        method(date, "setUTCFullYear", null, "yearValueOpt", "monthValueOpt", "dayValueOpt");
        method(date, "setUTCHours", null, "hoursValueOpt", "minutesValueOpt", "secondsValueOpt", "msValueOpt");
        method(date, "setUTCMilliseconds", null, "millisecondsValue");
        method(date, "setUTCMinutes", null, "minutesValueOpt", "secondsValueOpt", "msValueOpt");
        method(date, "setUTCMonth", null, "monthValueOpt", "dayValueOpt");
        method(date, "setUTCSeconds", null, "secondsValueOpt", "msValueOpt");
        method(date, "setYear", null, "yearValue");
        method(date, "toGMTString", str);
        method(date, "toLocaleString", str);
        method(date, "toLocaleDateString", str);
        method(date, "toLocaleTimeString", str);
        method(date, "toSource", str);
        method(date, "toString", str);
        method(date, "toUTCString", str);
        method(date, "valueOf", num);

        err.putMember("description", str);
        err.putMember("fileName", str);
        err.putMember("lineNumber", num);
        err.putMember("message", str);
        err.putMember("name", str);
        err.putMember("number", str);
        err.putMember("stack", str);

        numf.putMember("MAX_VALUE", num);
        numf.putMember("MIN_VALUE", num);
        numf.putMember("NaN", num);
        numf.putMember("NEGATIVE_INFINITY", num);
        numf.putMember("POSITIVE_INFINITY", num);
        method(num, "toExponential", str);
        method(num, "toFixed", str);
        method(num, "toLocaleString", str);
        method(num, "toPrecision", str);
        method(num, "toSource", str);
        method(num, "toString", str);
        method(num, "valueOf", num);

        method(boo, "toSource", str);
        method(boo, "toString", str);
        method(boo, "valueOf", num);

        JsArrayType strarr = new JsArrayType();
        strarr.putMember(Type.CONTAIN, str);
        method(reg, "exec", strarr, "strOpt");
        method(reg, "test", boo, "strOpt");
        method(reg, "toSource", str);
        method(reg, "toString", str);

        str.putMember("length", num);
        method(str, "charAt", str, "index");
        method(str, "charCodeAt", num, "indexOpt");
        method(str, "concat", str, "...string");
        method(str, "indexOf", num, "searchValue", "fromIndexOpt");
        method(str, "lastIndexOf", num, "searchValue", "fromIndexOpt");
        method(str, "match", strarr, "regexp");
        method(str, "replace", str, "regexp", "newSubStr");
        method(str, "search", num, "regexp");
        method(str, "slice", str, "beginslice", "endSliceOpt");
        method(str, "split", strarr, "separatorOpt", "limitOpt");
        method(str, "substr", str, "start", "lengthOpt");
        method(str, "substring", str, "indexA", "indexBOpt");
        method(str, "toLowerCase", str);
        method(str, "toSource", str);
        method(str, "toString", str);
        method(str, "toUpperCase", str);
        method(str, "valueOf", num);

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
     * @return 函数
     */
    public JsNativeFunction buildClass(String name, JsType su, String... args) {
        JsType pro = new JsType(name);
        pro.extend(su);
        map.put(name, pro);
        JsNativeFunction f = new JsNativeFunction(name, pro);
        f.extend(getPrototype(FUNCTION));
        getScopeType().putMember(name, f);
        return f;
    }

    /**
     * 构建一个返回数组类型的方法.
     *
     * @param pro 方法拥有者
     * @param name 方法名
     * @param args 形参表
     */
    public void arrayMethod(Type pro, String name, String... args) {
        JsType p = new JsType();
        p.extend(getPrototype(OBJECT));
        Type arr = getPrototype(ARRAY);
        JsArrayMethod m = new JsArrayMethod(name, p, arr, args);
        m.extend(getPrototype(FUNCTION));
        pro.putMember(name, m);
    }

    /**
     * 构建一个方法.
     *
     * @param pro 方法拥有者
     * @param name 方法名
     * @param r 返回类型
     * @param args 形参表
     */
    public void method(Type pro, String name, Type r, String... args) {
        JsType p = new JsType();
        p.extend(getPrototype(OBJECT));
        JsNativeFunction m = new JsNativeFunction(name, p, args);
        m.extend(getPrototype(FUNCTION));
        if (r != null) {
            m.putMember(Type.RETURN, r);
        }
        pro.putMember(name, m);
    }

}
