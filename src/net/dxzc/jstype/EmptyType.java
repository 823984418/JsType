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

import java.util.Collections;
import java.util.Iterator;
import net.dxzc.util.Action;

/**
 * 空的类型.
 *
 * @author 823984418@qq.com
 */
public class EmptyType implements Type {

    /**
     * 一个完全空的类型.
     *
     * @param name 名字
     */
    public EmptyType(String name) {
        this.name = name;
    }

    protected String name;

    @Override
    public boolean addMemberAction(String name, Action<Type> action) {
        return false;
    }

    @Override
    public boolean putMember(String name, Type type) {
        return false;
    }

    @Override
    public Iterator<String> iterator() {
        return Collections.<String>emptyIterator();
    }

    @Override
    public Iterator<String> iteratorAll() {
        return Collections.<String>emptyIterator();
    }

    @Override
    public Iterator<Type> getMemberType(String name) {
        return Collections.<Type>emptyIterator();
    }

    @Override
    public String getDoc() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
