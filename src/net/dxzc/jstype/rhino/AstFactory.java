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
package net.dxzc.jstype.rhino;

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.ErrorCollector;

/**
 * 简单的AST构造封装.
 *
 * @author 823984418@qq.com
 */
public class AstFactory {

    /**
     * 以{@link CompilerEnvirons#ideEnvirons()}得到语法配置并且打开记录注释标记.
     */
    public AstFactory() {
        environs = CompilerEnvirons.ideEnvirons();
        environs.setRecordingComments(true);
        environs.setRecordingLocalJsDocComments(true);
    }

    /**
     * 语法配置.
     */
    public CompilerEnvirons environs;

    /**
     * 构建一个语法数.
     *
     * @param script 脚本
     * @param name 资源名
     * @param ec 警告信息收集器
     * @return 语法树
     */
    public AstRoot build(String script, String name, ErrorCollector ec) {
        Parser p = new Parser(environs, ec);
        return p.parse(script, name, 1);
    }

}
