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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.dxzc.util.ActionSet;

/**
 * js中出现的类型.支持继承
 *
 * @author 823984418@qq.com
 */
public class JsType extends BaseMapType {

    /**
     * 构建一个类型原型.
     */
    public JsType() {
        this("Unknow@");
        name += Integer.toHexString(hashCode());
    }

    /**
     * 以指定名字构建类型原型.
     *
     * @param name 名字
     */
    public JsType(String name) {
        this.name = name;
        childs.addAction(c -> {
            for (Map.Entry<String, ActionSet<Type>> e : members.entrySet()) {
                String f = e.getKey();
                e.getValue().addAction(n -> c.putMember(f, n));
            }
        });
    }

    /**
     * 名字.
     */
    protected String name;

    /**
     * 所具有的子类型.
     */
    protected final ActionSet<JsType> childs = new ActionSet<>();

    /**
     * 所具有的父类型.
     */
    protected final Set<Type> superTypes = new HashSet<>();

    @Override
    protected ActionSet<Type> getMember(String name) {
        ActionSet<Type> r = members.get(name);
        if (r != null) {
            return r;
        }
        r = super.getMember(name);
        if (r != null) {
            for (Type t : childs) {
                r.addAction(n -> t.putMember(name, n));
            }
        }
        return r;
    }

    /**
     * 使此类型继承其他类型.
     *
     * @param type 要继承的类型
     */
    public void extend(Type type) {
        if (type != null && type != this && superTypes.add(type)) {
            if (type instanceof JsType) {
                ((JsType) type).childs.add(this);
            } else {
                for (Iterator<String> it = type.iteratorAll(); it.hasNext();) {
                    String n = it.next();
                    type.addMemberAction(n, t -> putMember(n, t));
                }
            }
        }

    }

    @Override
    public String toString() {
        return name;
    }

}
