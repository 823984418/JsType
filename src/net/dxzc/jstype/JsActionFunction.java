/*
 * Copyright (C) 2020 823984418@qq.com
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
 * 将动作使用接口代理. 注意,代理动作至多创造有限个[O(1)]新的原型且不得为函数原型,否则可能会导致无穷递归
 *
 * @author 823984418@qq.com
 */
public class JsActionFunction extends JsNativeFunction {

    /**
     * 动作代理者.
     */
    @FunctionalInterface
    public static interface TypeAction {

        /**
         * 动作.
         *
         * @param fun 代理者
         * @param invoked 调用还是实例化
         * @param r 类型操作
         * @param i 被调用者
         * @param args 参数
         * @return 是否支持
         * @see Type#invoke
         * @see Type#newInstance
         */
        public boolean action(JsActionFunction fun, boolean invoked, Action<Type> r, Rvalue i, Rvalue... args);

    }

    /**
     * 构造方法. 动作为{@code null}时执行默认动作.
     *
     * @param name 名字
     * @param prototype 对应原型
     * @param invokeAction 调用动作
     * @param newInstanceAction 实例化动作
     * @param args 形参表
     */
    public JsActionFunction(String name, JsType prototype, TypeAction invokeAction, TypeAction newInstanceAction, String... args) {
        super(name, prototype, args);
        this.invokeAction = invokeAction;
        this.newInstanceAction = newInstanceAction;
    }

    /**
     * 调用动作.
     */
    protected TypeAction invokeAction;

    /**
     * 实例化动作.
     */
    protected TypeAction newInstanceAction;

    @Override
    public boolean invoke(Action<Type> r, Rvalue i, Rvalue... args) {
        if (invokeAction != null) {
            return invokeAction.action(this, true, r, i, args);
        }
        return super.invoke(r, i, args);
    }

    @Override
    public boolean newInstance(Action<Type> r, Rvalue i, Rvalue... args) {
        if (newInstanceAction != null) {
            return newInstanceAction.action(this, false, r, i, args);
        }
        return super.newInstance(r, i, args);
    }

    /**
     * 默认的调用动作.
     *
     * @param r 返回类型操作
     * @param i 调用域
     * @param args 参数
     * @return 可否调用
     * @see Type#invoke
     */
    public final boolean defaultInvake(Action<Type> r, Rvalue i, Rvalue... args) {
        return super.invoke(r, i, args);
    }

    /**
     * 默认的实例化动作.
     *
     * @param r 返回类型操作
     * @param i 调用域
     * @param args 参数
     * @return 可否调用
     * @see Type#newInstance
     */
    public final boolean defaultNewInstance(Action<Type> r, Rvalue i, Rvalue... args) {
        return super.newInstance(r, i, args);
    }

}
