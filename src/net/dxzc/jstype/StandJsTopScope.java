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

import java.util.Arrays;
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

        type.putMember(Type.THIS, type);

        JsType fun = new JsType("Function");
        map.put(FUNCTION, fun);
        JsNativeFunction funf = new JsNativeFunction("Function", fun);
        funf.putMember(Type.RETURN, fun);
        funf.extend(fun);
        type.putMember(FUNCTION, funf);

        JsType obj = new JsType("Object");
        map.put(OBJECT, obj);
        JsActionFunction.TypeAction obja = (f, invoked, r, i, as) -> {
            JsType o = new JsType("object");
            o.extend(obj);
            if (as.length == 1) {
                as[0].forType(o::extend);
            }
            r.action(o);
            return true;
        };
        JsActionFunction objf = new JsActionFunction(OBJECT, obj, obja, obja, "object");
        objf.putMember(Type.RETURN, obj);
        objf.extend(fun);
        type.putMember(OBJECT, objf);

        fun.extend(obj);

        JsType arr = new JsType("Array");
        arr.extend(obj);
        map.put(ARRAY, arr);
        JsActionFunction.TypeAction arra = (f, invoked, r, i, as) -> {
            JsArrayType array = new JsArrayType();
            array.extend(arr);
            if (as.length == 1) {
                as[0].forType(t -> {
                    if (!t.isNumberType()) {
                        array.putMember(Type.CONTAIN, t);
                    }
                });
            } else {
                for (Rvalue v : as) {
                    v.forType(t -> array.putMember(Type.CONTAIN, t));
                }
            }
            r.action(array);
            return true;
        };
        JsActionFunction arrf = new JsActionFunction(ARRAY, arr, arra, arra, "...element");
        arrf.extend(fun);
        type.putMember(ARRAY, arrf);

        JsNativeFunction strf = buildClass(STRING, obj, "value");
        Type str = getPrototype(STRING);
        JsNativeFunction numf = buildNumberClass(obj, "value");
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

        JsType json = new JsType("JSON");
        map.put(JSON, json);
        type.putMember(JSON, json);

        actionMethod(objf, "assign", obj, (f, invoked, r, i, as) -> {
            if (as.length == 0) {
                return false;
            }
            as[0].forType(r);
            if (as.length > 1) {
                as[0].forType(t -> {
                    if (t instanceof JsType) {
                        JsType tt = (JsType) t;
                        for (int j = 1, l = as.length; j < l; j++) {
                            as[j].forType(tt::extend);
                        }
                    }
                });
            }
            return true;
        }, "target", "...source");
        actionMethod(objf, "create", obj, (f, invoked, r, i, as) -> {
            JsType o = new JsType("createObject");
            o.extend(obj);
            if (as.length != 0) {
                as[0].forType(o::extend);
            }
            r.action(o);
            return true;
        }, "proto", "propertiesObject");
        firstMethod(objf, "defineProperty", obj, "obj", "prop", "descriptor");
        firstMethod(objf, "defineProperties", obj, "obj", "prop");
        actionMethod(objf, "entries", arr, (f, invoked, r, i, as) -> {
            if (as.length != 0) {
                JsArrayType at = new JsArrayType();
                at.extend(arr);
                JsArrayType ac = new JsArrayType();
                ac.extend(arr);
                JsType co = new JsType("entriesObject");
                co.extend(obj);
                at.putMember(Type.CONTAIN, ac);
                ac.putMember(Type.CONTAIN, str);
                ac.putMember(Type.CONTAIN, co);
                r.action(at);
                return true;
            }
            return false;
        }, "obj");
        firstMethod(objf, "freeze", obj, "obj");
        extendMethod(objf, "getOwnPropertyDescriptor", obj, "obj", "prop");
        JsArrayType strArr = new JsArrayType();
        strArr.extend(arr);
        strArr.putMember(Type.CONTAIN, str);
        extendArrayMethod(objf, "getOwnPropertyNames", strArr, "obj");
        method(objf, "is", boo, "value1", "value2");
        method(objf, "isExtensible", boo, "obj");
        method(objf, "isFrozen", boo, "obj");
        method(objf, "isSealed", boo, "obj");
        extendArrayMethod(objf, "keys", strArr, "obj");
        firstMethod(objf, "preventExtensions", obj, "obj");
        firstMethod(objf, "seal", obj, "obj");
        actionMethod(objf, "setPrototypeOf", obj, (f, invoked, r, i, as) -> {
            if (as.length == 0) {
                return false;
            }
            as[0].forType(r);
            if (as.length > 1) {
                as[0].forType(t -> {
                    if (t instanceof JsType) {
                        JsType tt = (JsType) t;
                        as[1].forType(tt::extend);
                    }
                });
            }
            return true;
        }, "obj", "prototype");
        JsArrayType objArr = new JsArrayType();
        objArr.extend(arr);
        objArr.putMember(Type.CONTAIN, str);
        extendArrayMethod(objf, "values", objArr, "obj");
        obj.putMember("constructor", fun);
        method(obj, "hasOwnProperty", boo, "prop");
        method(obj, "isPrototypeOf", boo, "object");
        method(obj, "propertyIsEnumerable", boo, "prop");

        actionMethod(fun, "apply", obj, (f, invoked, r, i, args) -> {
            if (i == null) {
                return false;
            }
            JsType o = new JsType("applyReturn");
            o.extend(obj);
            i.forType(t -> {
                t.addMemberAction(Type.RETURN, o::extend);
                if (args.length != 0) {
                    args[0].forType(n -> t.putMember(Type.THIS, n));
                }
            });
            r.action(o);
            return true;
        }, "thisArg", "argArrayOpt");
        actionMethod(fun, "call", obj, (f, invoked, r, i, args) -> {
            if (i == null) {
                return false;
            }
            if (args.length != 0) {
                Rvalue ti = args[0];
                Rvalue[] a = Arrays.copyOfRange(args, 1, args.length);
                i.forType(t -> t.invoke(r, ti, a));
            } else {
                i.forType(t -> t.invoke(r, null));
            }
            return true;
        }, "thisArg", "...args");
        method(fun, "toSource", str);
        method(fun, "toString", str);
        method(fun, "valueOf", str);

        containMethod(arr, "pop");
        actionMethod(arr, "push", num, (f, invoked, r, i, as) -> {
            if (i == null || as.length == 0) {
                return false;
            }
            i.forType(t -> {
                as[0].forType(n -> t.putMember(Type.CONTAIN, n));
            });
            return true;
        }, "element");
        method(arr, "reverse", null);
        containMethod(arr, "shift");
        method(arr, "sort", null, "compareFunction");
        method(arr, "splice", null, "index", "howMany", "...element");
        method(arr, "unshift", num, "...element");
        method(arr, "concat", null, "...value");
        method(arr, "indexOf", num, "searchElement");
        method(arr, "join", str, "separatorOpt");
        method(arr, "lastIndexOf", num, "searchElement");
        arrayCloneMethod(arr, "slice", "begin", "endOpt");
        method(arr, "toString", str);
        method(arr, "valueOf", str);
        arrayCloneMethod(arr, "filter", "callback");
        method(arr, "forEach", null, "callback");
        method(arr, "every", boo, "callback");
        arrayCloneMethod(arr, "map", "callback");
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

        extendMethod(json, "parse", obj, "text", "reviver");
        method(json, "stringify", str, "value", "replacer", "space");

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
        method(str, "anchor", str, "nameAttribute");
        method(str, "big", str);
        method(str, "blink", str);
        method(str, "bold", str);
        method(str, "fixed", str);
        method(str, "fontcolor", str, "color");
        method(str, "fontsize", str, "size");
        method(str, "italics", str);
        method(str, "link", str, "hrefAttribute");
        method(str, "small", str);
        method(str, "strike", str);
        method(str, "sub", str);
        method(str, "sup", str);

        method(type, "isFinite", num, "testValue");
        method(type, "isNaN", num, "testValue");
        method(type, "parseFloat", num, "string");
        method(type, "parseInt", num, "string");
        method(type, "eval", obj, "string");
        method(type, "decodeURI", str, "encodedURI");

        type.getMember("undefined");
        type.putMember("NaN", num);
        type.putMember("Infinity", num);

    }

    /**
     * 原型映射.
     */
    protected final Map<String, Type> map = new HashMap<>();

    @Override
    public Type getPrototype(String name) {
        return map.get(name);
    }

    @Override
    public void putPrototypr(String name, Type type) {
        map.put(name, type);
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
        JsNativeFunction f = new JsNativeFunction(name, pro, args);
        f.putMember(Type.RETURN, pro);
        f.extend(getPrototype(FUNCTION));
        getScopeType().putMember(name, f);
        return f;
    }

    private JsNativeFunction buildNumberClass(JsType su, String... args) {
        JsType pro = new JsNumberType();
        pro.extend(su);
        map.put(NUMBER, pro);
        JsNativeFunction f = new JsNativeFunction(NUMBER, pro, args);
        f.putMember(Type.RETURN, pro);
        f.extend(getPrototype(FUNCTION));
        getScopeType().putMember(NUMBER, f);
        return f;
    }

    /**
     * 返回值是第一个参数的方法.
     *
     * @param pro 方法拥有者
     * @param name 方法名
     * @param re 显示的返回类型
     * @param args 形参表
     */
    public void firstMethod(Type pro, String name, Type re, String... args) {
        actionMethod(pro, name, re, (fun, invoked, r, i, as) -> {
            if (as.length != 0) {
                as[0].forType(r);
                return true;
            }
            return false;
        }, args);
    }

    /**
     * 构建一个返回克隆继承this类型的方法.
     *
     * @param pro 方法拥有者
     * @param name 方法名
     * @param args 形参表
     */
    public void cloneMethod(Type pro, String name, String... args) {
        Type obj = getPrototype(OBJECT);
        actionMethod(pro, name, obj, (fun, invoked, r, i, as) -> {
            JsType o = new JsType("object");
            o.extend(obj);
            if (i != null) {
                i.forType(o::extend);
            }
            r.action(o);
            return true;
        }, args);
    }

    /**
     * 构建一个返回数组克隆类型的方法.
     *
     * @param pro 方法拥有者
     * @param name 方法名
     * @param args 形参表
     */
    public void arrayCloneMethod(Type pro, String name, String... args) {
        Type arr = getPrototype(ARRAY);
        actionMethod(pro, name, arr, (fun, invoked, r, i, as) -> {
            JsArrayType array = new JsArrayType();
            array.extend(arr);
            if (i != null) {
                i.forType(array::extend);
            }
            r.action(array);
            return true;
        }, args);
    }

    /**
     * 构建一个返回值为克隆继承this数组容纳类型的方法.
     *
     * @param pro 方法拥有者
     * @param name 方法名
     * @param args 形参表
     */
    public void containMethod(Type pro, String name, String... args) {
        actionMethod(pro, name, getPrototype(OBJECT), (fun, invoked, r, i, as) -> {
            if (this == null) {
                return false;
            }
            i.forType(t -> t.addMemberAction(Type.CONTAIN, r::action));
            return true;
        }, args);
    }

    /**
     * 构建一个返回值为this的方法.
     *
     * @param pro 方法拥有者
     * @param name 方法名
     * @param args 形参表
     */
    public void thisMethod(Type pro, String name, String... args) {
        actionMethod(pro, name, getPrototype(OBJECT), (fun, invoked, r, i, as) -> {
            if (this == null) {
                return false;
            }
            i.forType(r::action);
            return true;
        }, args);
    }

    /**
     * 构建一个返回值继承某类型的方法. 每个返回值是独立的原型
     *
     * @param pro 方法拥有者
     * @param name 方法名
     * @param r 返回类型
     * @param args 形参表
     */
    public void extendMethod(Type pro, String name, Type r, String... args) {
        actionMethod(pro, name, r, (fun, invoked, ra, i, as) -> {
            JsType o = new JsType("object");
            o.extend(r);
            ra.action(o);
            return true;
        }, args);
    }

    /**
     * 构建一个返回值继承某数组类型的方法. 每个返回值是独立的原型
     *
     * @param pro 方法拥有者
     * @param name 方法名
     * @param r 返回类型
     * @param args 形参表
     */
    public void extendArrayMethod(Type pro, String name, Type arrObj, String... args) {
        actionMethod(pro, name, arrObj, (fun, invoked, r, i, as) -> {
            JsArrayType array = new JsArrayType();
            array.extend(arrObj);
            r.action(array);
            return true;
        }, args);
    }

    /**
     * 构建一个特定行为的方法.
     *
     * @param pro 方法拥有者
     * @param name 方法名
     * @param r 返回类型
     * @param action 行为
     * @param args 形参表
     */
    public void actionMethod(Type pro, String name, Type r, JsActionFunction.TypeAction action, String... args) {
        if (r == null) {
            throw new RuntimeException();
        }
        JsType p = new JsType("object");
        p.extend(getPrototype(OBJECT));
        JsActionFunction m = new JsActionFunction(name, p, action, null, args);
        m.extend(getPrototype(FUNCTION));
        m.putMember(Type.RETURN, r);
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
        JsType p = new JsType("object");
        p.extend(getPrototype(OBJECT));
        JsNativeFunction m = new JsNativeFunction(name, p, args);
        m.extend(getPrototype(FUNCTION));
        if (r != null) {
            m.putMember(Type.RETURN, r);
        }
        pro.putMember(name, m);
    }

}
