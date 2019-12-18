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

import java.util.Iterator;
import net.dxzc.util.Action;

/**
 * 一个原型.
 *
 * @author 823984418@qq.com
 */
public interface Type extends Iterable<String> {

    /**
     * 默认以此标志不可枚举.
     */
    public static final String NU_ENUM = "#";

    /**
     * 表述多个类型.
     *
     * @param types 多个类型
     * @return 表述字符串
     */
    public static String typesToString(Iterator<Type> types) {
        if (!types.hasNext()) {
            return "Undefined";
        }
        StringBuilder sb = new StringBuilder();
        for (;;) {
            Type t = types.next();
            sb.append(t);
            if (!types.hasNext()) {
                return sb.toString();
            }
            sb.append("|");
        }
    }

    /**
     * 被调用在哪些类型上.
     */
    public static final String THIS = NU_ENUM + "this";

    /**
     * 实例化类型.
     */
    public static final String NEW = NU_ENUM + "new";

    /**
     * 返回值类型
     */
    public static final String RETURN = NU_ENUM + "return";

    /**
     * 包含类型.
     */
    public static final String CONTAIN = NU_ENUM + "contain";

    /**
     * 获取成员类型. 实现对此动作也许会创建成员,也许会告知不可能具有此成员
     *
     * @param name 成员
     * @param action 对此的动做
     * @return 如果(将来也)不可能拥有此成员则返回{@code false}
     */
    public boolean addMemberAction(String name, Action<Type> action);

    /**
     * 尝试对此类型进行一次成员赋值.实现也许会为此新建一个成员,也许会拒绝此操作
     *
     * @param name 成员
     * @param type 新增的类型
     * @return 是否成功
     */
    public boolean putMember(String name, Type type);

    /**
     * 迭代所有的可枚举成员.
     *
     * @return 成员迭代器
     */
    @Override
    public Iterator<String> iterator();

    /**
     * 获取某个成员的所有类型.实现也许会返回{@code null}来表示不可能有此成员
     *
     * @param name 成员
     * @return 类型迭代器
     */
    public Iterator<Type> getMemberType(String name);

    /**
     * 获取文档信息.
     *
     * @return 文档信息
     */
    public String getDoc();

    /**
     * 尝试以指定参数调用.
     *
     * @param length 参数个数
     * @return 参数接入或者{@code null}
     */
    public default Iterator<Action<Type>> invoke(int length) {
        return null;
    }

    public default void doInvoke(Action<Type> action) {
        addMemberAction(Type.RETURN, action);
    }

    /**
     * 尝试以指定参数实例化.
     *
     * @param length 参数个数
     * @return 参数接入或者{@code null}
     */
    public default Iterator<Action<Type>> newInstance(int length) {
        return null;
    }

    public default void doNewInstance(Action<Type> action) {
        addMemberAction(NEW, action);
    }

    /**
     * 显示形参信息.
     *
     * @return 表述字符串
     */
    public default String argsToString() {
        return "()";
    }

    /**
     * 显示函数类型信息.
     *
     * @return 表述字符串
     */
    public default String functionToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(argsToString());
        sb.append(":");
        sb.append(typesToString(getMemberType(RETURN)));
        return sb.toString();
    }

    /**
     * 显示数组类型信息.
     *
     * @return 表述字符串
     */
    public default String containToString() {
        Iterator<Type> contain = getMemberType(CONTAIN);
        if (contain == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(typesToString(contain));
        sb.append("]");
        return sb.toString();
    }

    /**
     * 显示类型成员信息.
     *
     * @return 表述字符串
     */
    public default String membersToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (String m : this) {
            sb.append(m);
            sb.append(":");
            sb.append(typesToString(getMemberType(m)));
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString();

}
