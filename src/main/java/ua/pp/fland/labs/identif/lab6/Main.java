package ua.pp.fland.labs.identif.lab6;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.pp.fland.labs.identif.lab6.gui.MainWindow;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Maxim Bondarenko
 * @version 1.0 11/7/11
 */

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InvocationTargetException, InterruptedException, ClassNotFoundException, UnsupportedLookAndFeelException, IllegalAccessException, InstantiationException {
        log.debug("App started...");

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                log.info("L&F changed from system to Nimbus");
                break;
            }

        }

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                new MainWindow();
            }
        });
    }
}
