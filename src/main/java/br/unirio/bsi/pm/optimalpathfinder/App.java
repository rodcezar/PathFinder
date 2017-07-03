package br.unirio.bsi.pm.optimalpathfinder;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import br.unirio.bsi.pm.optimalpathfinder.model.MapRenderer;

public class App {

	public static void main(String s[]) throws Exception {

		JFrame f = new JFrame("Paper racing swing");

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		MapRenderer mr = new MapRenderer();

		f.add("Center", mr);

		JPanel panel = new JPanel();
		panel.add(new JLabel("Find Path!"));
		panel.add(mr.getPFJbutton(), BorderLayout.EAST);
		panel.add(new JLabel("Moves"));
		panel.add(mr.getJbuttons(), BorderLayout.EAST);
		panel.add(new JLabel("Map"));
		panel.add(mr.getChoices());
		panel.add(new JLabel("Save As"));
		panel.add(mr.getJcbformats());
		panel.add(mr.getMovesLabel());

		f.add("South", panel);
		f.pack();
		f.setVisible(true);
	}
}
