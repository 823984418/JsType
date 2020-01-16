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
package net.dxzc.classfind;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.dxzc.util.ActionSet;

/**
 *
 * @author 823984418@qq.com
 */
public class ClassFinder extends ActionSet<Class<?>> {

    public static final ClassFinder BASE_FINDER = new ClassFinder();

    static {
        BASE_FINDER.find(Class.class);
    }

    public ClassFinder() {
        addAction(this::onFind);
    }

    public void find(Class<?> c) {
        if (c != null && !c.isArray() && !c.isPrimitive()) {
            add(c);
        }
    }

    protected void onFind(Class<?> c) {
        for (Class<?> cc : c.getClasses()) {
            find(cc);
        }
        for (Class<?> cc : c.getDeclaredClasses()) {
            find(cc);
        }
        for (Class<?> cc : c.getInterfaces()) {
            find(cc);
        }
        find(c.getSuperclass());
        find(c.getDeclaringClass());
        find(c.getEnclosingClass());
        for (Constructor<?> cc : c.getDeclaredConstructors()) {
            for (Class<?> ccc : cc.getParameterTypes()) {
                find(ccc);
            }
            for (Class<?> ccc : cc.getExceptionTypes()) {
                find(ccc);
            }
        }
        for (Field cc : c.getDeclaredFields()) {
            find(cc.getType());
        }
        for (Method cc : c.getDeclaredMethods()) {
            find(cc.getReturnType());
            for (Class<?> ccc : cc.getParameterTypes()) {
                find(ccc);
            }
            for (Class<?> ccc : cc.getExceptionTypes()) {
                find(ccc);
            }
        }
    }

}
