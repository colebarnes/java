package org.colebarnes.gui.common;

import java.awt.Component;

import javax.swing.JOptionPane;

import org.colebarnes.common.logger.Logger;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;
import com.github.weisj.darklaf.theme.Theme;

public class GuiUtils {
	public static void installDarculaLaf() {
		GuiUtils.installLafTheme(new DarculaTheme());
	}

	public static void installLafTheme(Theme theme) {
		LafManager.setTheme(theme);
		LafManager.install();
	}

	public static void info(String message) {
		GuiUtils.info(null, message);
	}

	public static void info(Component parent, String message) {
		Logger.info("GUI message: %s", message);
		GuiUtils.prompt(parent, message, JOptionPane.INFORMATION_MESSAGE);
	}

	public static void message(String message) {
		GuiUtils.message(null, message);
	}

	public static void message(Component parent, String message) {
		Logger.info("GUI message: %s", message);
		GuiUtils.prompt(parent, message, JOptionPane.PLAIN_MESSAGE);
	}

	public static void warn(String message) {
		GuiUtils.warn(null, message);
	}

	public static void warn(Component parent, String message) {
		Logger.warn("GUI message: %s", message);
		GuiUtils.prompt(parent, message, JOptionPane.WARNING_MESSAGE);
	}

	public static void error(String message) {
		GuiUtils.error(null, message);
	}

	public static void error(Component parent, String message) {
		Logger.error("GUI message: %s", message);
		GuiUtils.prompt(parent, message, JOptionPane.ERROR_MESSAGE);
	}

	private static void prompt(Component parent, String message, int type) {
		JOptionPane.showMessageDialog(parent, message, ".:: org.colebarnes ::.", type);
	}
}
