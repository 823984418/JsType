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
package net.dxzc.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 一个不可减少的容器.
 *
 * @param <E> 类型
 * @author 823984418@qq.com
 */
public class IncreaseCollection<E> implements Collection<E> {

    /**
     * 构造一个空的容器.
     */
    public IncreaseCollection() {
    }

    private int size;

    private Node<E> first;

    /**
     * 尝试添加一个元素.
     *
     * @param e 要添加的元素
     * @return 容器是否有改变
     */
    @Override
    public boolean add(E e) {
        first = new Node<>(e, first);
        size++;
        return true;
    }

    /**
     * 获取当前元素数.
     *
     * @return 当前元素数
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * 检查是否为空.
     *
     * @return 是否为空
     */
    @Override
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * 检查是否有某个元素.
     *
     * @param o 要检查的元素
     * @return 是否拥有
     */
    @Override
    public boolean contains(Object o) {
        if (o == null) {
            for (Node<E> n = first; n != null; n = n.next) {
                if (n.value == null) {
                    return true;
                }
            }
        } else {
            for (Node<E> n = first; n != null; n = n.next) {
                if (o.equals(n.value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 返回数组表述. 按照添加顺序输出
     *
     * @return 数组
     */
    @Override
    public Object[] toArray() {
        int i = size;
        Object[] array = new Object[i];
        for (Node<E> x = first; x != null; x = x.next) {
            array[--i] = x.value;
        }
        return array;
    }

    /**
     * 返回数组表述. 按照添加顺序输出 如果空间小于需要,自行构建数组,如果大于,则末尾置{@code null}
     *
     * @param <T> 类型
     * @param array 数组
     * @return 数组或重新构造的数组
     */
    @Override
    public <T> T[] toArray(T[] array) {
        int i = size;
        if (array.length < i) {
            array = (T[]) Array.newInstance(array.getClass().getComponentType(), i);
        }
        if (array.length > i) {
            array[i] = null;
        }
        for (Node<E> x = first; x != null; x = x.next) {
            array[--i] = (T) x.value;
        }
        return array;
    }

    /**
     * 迭代此容器. 迭代顺序为后进先出,迭代过程中添加不会影响迭代
     *
     * @return 迭代器
     */
    @Override
    public Iterator<E> iterator() {
        return new NodeIterator<>(first);
    }

    /**
     * 返回字符串表述. 等价于{@code Arrays.toString(toArray())}
     *
     * @return 字符串表述
     */
    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

    /**
     * 检查迭代项每个元素都出现于此容器中.
     *
     * @param c 容器
     * @return 是否是传入容器的超集
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 对每个迭代项调用{@link add}.
     *
     * @param c 容器
     * @return 是否有添加
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    /**
     * @deprecated 不支持的行为
     */
    @Override
    @Deprecated
    public boolean remove(Object o) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated 不支持的行为
     */
    @Override
    @Deprecated
    public boolean removeAll(Collection<?> c) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated 不支持的行为
     */
    @Override
    @Deprecated
    public boolean retainAll(Collection<?> c) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated 不支持的行为
     */
    @Override
    @Deprecated
    public void clear() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    private static final class NodeIterator<E> implements Iterator<E> {

        private NodeIterator(Node<E> first) {
            node = first;
        }

        private Node<E> node;

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public E next() {
            if (node == null) {
                throw new NoSuchElementException();
            }
            E v = node.value;
            node = node.next;
            return v;
        }

    }

    private static final class Node<E> {

        private Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }

        private final E value;

        private final Node<E> next;

    }

}
