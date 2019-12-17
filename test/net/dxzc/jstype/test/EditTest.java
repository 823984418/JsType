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

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import net.dxzc.jstype.Rvalue;

/**
 *
 * @author 823984418@qq.com
 */
public class EditTest extends JFrame {

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        javax.swing.SwingUtilities.invokeLater(EditTest::new);
    }

    public EditTest() {
        super("...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComboBox list = new JComboBox();
        add(list, BorderLayout.NORTH);
        JTextArea edit = new JTextArea();
        add(edit, BorderLayout.CENTER);
        JButton b = new JButton("updata");
        b.addActionListener(e -> {
            BaseTest t = new BaseTest(edit.getText(), "script", edit.getSelectionStart());
            list.removeAllItems();
            Rvalue v = t.value;
            if (v == null) {
                v = t.scope;
            }
            setTitle(v.toString());
            for (String i : v.fields()) {
                list.addItem(i);
            }
        });
        add(b, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

}
