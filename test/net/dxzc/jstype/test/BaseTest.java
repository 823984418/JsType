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
package net.dxzc.jstype.test;

import net.dxzc.jstype.JsScope;
import net.dxzc.jstype.JsScopeFactory;
import net.dxzc.jstype.JsTopScope;
import net.dxzc.jstype.JsXmlFunction;
import net.dxzc.jstype.Rvalue;
import net.dxzc.jstype.StandJsTopScope;
import net.dxzc.jstype.refjava.JavaLoader;
import net.dxzc.jstype.rhino.AstFactory;
import net.dxzc.jstype.rhino.AstTransformer;
import org.mozilla.javascript.ast.AstRoot;

/**
 *
 * @author 823984418@qq.com
 */
public class BaseTest extends AstTransformer {

    public BaseTest(String s, String n, int i) {
        super(new JsScopeFactory() {
            JavaLoader loader = new JavaLoader();

            @Override
            public JsTopScope buildTopScope() {
                JsTopScope scope = new StandJsTopScope();
                loader.init(scope);
                JsXmlFunction.init(scope);
                return scope;
            }
        });
        index = i;
        AstFactory a = new AstFactory();
        AstRoot ast = a.build(s, n, errorCollector);
        //System.out.println(ast.debugPrint());
        JsTopScope scope = buildJsTopScope();
        transformScript(scope, ast);
        System.out.print(errorCollector);
    }

    public int index;

    @Override
    protected void onValue(Rvalue value, int offset, int length) {
        super.onValue(value, offset, length);
        if (offset > index || offset + length < index) {
            return;
        }
        if (offset + length == index && length <= vs) {
            vs = length;
            this.value = value;
        }
    }

    @Override
    protected void onScope(JsScope scope, int offset, int length) {
        super.onScope(scope, offset, length);
        if (offset > index || offset + length < index) {
            return;
        }
        if (length <= ss) {
            ss = length;
            this.scope = scope;
        }
    }

    int vs = Integer.MAX_VALUE;
    public Rvalue value;

    int ss = Integer.MAX_VALUE;
    public JsScope scope;

}
