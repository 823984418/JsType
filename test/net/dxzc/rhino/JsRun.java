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
package net.dxzc.rhino;

import java.io.IOException;
import java.io.InputStream;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Kit;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.ScriptableObject;

/**
 *
 * @author 823984418@qq.com
 */
public class JsRun {

    public static void main(String[] args) {
        String script;
        try (InputStream input = JsRun.class.getResourceAsStream("test.js")) {
            script = new String(Kit.readStream(input, 1024 * 4));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Context cx = Context.enter();
        cx.setOptimizationLevel(-1);
        ScriptableObject scope = cx.initStandardObjects();
        try {
            cx.evaluateString(scope, script, "<test>", 1, null);
        } finally {
            Context.exit();
        }
    }

}
