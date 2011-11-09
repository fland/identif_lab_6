package ua.pp.fland.labs.identif.lab6.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.pp.fland.labs.identif.lab6.gui.tools.BoxLayoutUtils;
import ua.pp.fland.labs.identif.lab6.gui.tools.ComponentUtils;
import ua.pp.fland.labs.identif.lab6.gui.tools.GUITools;
import ua.pp.fland.labs.identif.lab6.gui.tools.StandardBordersSizes;
import ua.pp.fland.labs.identif.lab6.model.ImplicitFiniteDifferenceMethod;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Maxim Bondarenko
 * @version 1.0 11/7/11
 */

public class MainWindow {
    private static final Logger log = LoggerFactory.getLogger(MainWindow.class);

    private final static ResourceBundle bundle = ResourceBundle.getBundle("lab6");

    private final static Dimension MAIN_FRAME_SIZE = new Dimension(Integer.parseInt(bundle.getString("window.width")),
            Integer.parseInt(bundle.getString("window.height")));

    private final static String PROCESS_BTN_TEXT = "Process";
    private final static String PROCESS_WEIGHTED_BTN_TEXT = "Process Weighted";
    private final static String EXIT_BTN_TEXT = "Exit";

    private final static String CAST_IRON_START_TEMPERATURE_LABEL = "Cast iron start temperature, C: ";
    private final static String CAST_IRON_MASS_DEVIATION_LABEL = "Cast iron mass deviation(tons): ";
    private final static String TIME_TRANSPORT_DEVIATION = "Transport time deviation:";
    private final static String TRANSPORT_TIME_TO_SLUG_REMOVAL_DEVIATION_LABEL = "To slug removal department(min): ";
    private final static String TRANSPORT_TIME_TO_MIXER_DEVIATION_LABEL = "To mixer department(min): ";
    private final static String DESULFURATION_COUNT = "Desulfurations count: ";
    private final static String LADLES_COUNT_DEVIATION = "Ladle count deviation: ";

    private final JFrame mainFrame;

    private final JTextField castIronStartTemperatureInput;
    private final JTextField castIronMassDeviationInput;
    private final JTextField slugRemovalDepTransTimeDeviationInput;
    private final JTextField mixerDepTransTimeDeviationInput;
    private final JTextField desulfurationCountInput;
    private final JTextField ladleCountDeviationInput;

    public MainWindow() {
        mainFrame = new JFrame("Lab 6");
        mainFrame.setSize(MAIN_FRAME_SIZE);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        castIronMassDeviationInput = new JTextField("10");
        GUITools.fixTextFieldSize(castIronMassDeviationInput);
        castIronMassDeviationInput.setCaretPosition(0);

        slugRemovalDepTransTimeDeviationInput = new JTextField("60");
        GUITools.fixTextFieldSize(slugRemovalDepTransTimeDeviationInput);
        slugRemovalDepTransTimeDeviationInput.setCaretPosition(0);

        mixerDepTransTimeDeviationInput = new JTextField("60");
        GUITools.fixTextFieldSize(mixerDepTransTimeDeviationInput);
        mixerDepTransTimeDeviationInput.setCaretPosition(0);

        desulfurationCountInput = new JTextField("1");
        GUITools.fixTextFieldSize(desulfurationCountInput);
        desulfurationCountInput.setCaretPosition(0);

        ladleCountDeviationInput = new JTextField("0");
        GUITools.fixTextFieldSize(ladleCountDeviationInput);
        ladleCountDeviationInput.setCaretPosition(0);

        castIronStartTemperatureInput = new JTextField("1450");
        GUITools.fixTextFieldSize(castIronStartTemperatureInput);
        castIronStartTemperatureInput.setCaretPosition(0);

        final JPanel mainPanel = BoxLayoutUtils.createVerticalPanel();
        mainPanel.setBorder(new EmptyBorder(StandardBordersSizes.MAIN_BORDER.getValue()));
        ComponentUtils.setSize(mainPanel, MAIN_FRAME_SIZE.width, MAIN_FRAME_SIZE.height);

        mainPanel.add(createInputPanels());
        mainPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));
        mainPanel.add(createButtonsPanel(mainFrame));

        mainFrame.add(mainPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private JPanel createButtonsPanel(final JFrame mainFrame) {
        JPanel buttonsPanel = BoxLayoutUtils.createHorizontalPanel();

        JButton processButton = new JButton(PROCESS_BTN_TEXT);
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                log.debug("Process btn pressed");
                Map<BigDecimal, Double> xStartTemp = new HashMap<BigDecimal, Double>();
                /*BigDecimal temp = new BigDecimal(-2.705882353);
                log.debug(temp.toPlainString());
                temp = temp.setScale(3, RoundingMode.UP);
                log.debug(temp.toPlainString());*/

                final int xValuesScale = 2;

                double xCoeff = -2.705882353d;
                double freeTerm = 3.905882353d;
                double startX = 0.16d;
                double endX = 1d;
                final double xStep = 0.01d;
                for(double currX = startX; currX <=endX; currX = currX + xStep){
                    BigDecimal temp = new BigDecimal(currX);
                    xStartTemp.put(temp.setScale(xValuesScale, RoundingMode.HALF_UP), (xCoeff * currX) + freeTerm);
                }

                xCoeff = 0.066666667d;
                freeTerm = 1.2d;
                startX = 0d;
                endX = 0.15d;
                for(double currX = startX; currX <=endX; currX = currX + xStep){
                    BigDecimal temp = new BigDecimal(currX);
                    xStartTemp.put(temp.setScale(xValuesScale, RoundingMode.HALF_UP), (xCoeff * currX) + freeTerm);
                }

                log.debug("Start data forming finished");
                final double timeStep = 0.01d;
                ImplicitFiniteDifferenceMethod implicitFiniteDifferenceMethod = new
                        ImplicitFiniteDifferenceMethod(xStartTemp, xStep, timeStep, 3.5d, xValuesScale);
                Map<Double, Map<BigDecimal, Double>> calculatedTemp = implicitFiniteDifferenceMethod.calculate();
            }
        });

        JButton exitButton = new JButton(EXIT_BTN_TEXT);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.debug("Exit btn pressed");
                shutdown();
            }
        });

        GUITools.createRecommendedMargin(processButton, exitButton);
        GUITools.makeSameSize(processButton, exitButton);

        buttonsPanel.add(processButton);
        buttonsPanel.add(Box.createRigidArea(StandardDimension.HOR_RIGID_AREA.getValue()));
        buttonsPanel.add(Box.createRigidArea(StandardDimension.HOR_RIGID_AREA.getValue()));
        buttonsPanel.add(exitButton);

        return buttonsPanel;
    }

    private JPanel createInputPanels() {
        JPanel inputsPanel = BoxLayoutUtils.createVerticalPanel();

        JPanel tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel castIronStartTempLabel = new JLabel(CAST_IRON_START_TEMPERATURE_LABEL);
        tempHorPanel.add(castIronStartTempLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(castIronStartTemperatureInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel castIronMassDeviationLabel = new JLabel(CAST_IRON_MASS_DEVIATION_LABEL);
        tempHorPanel.add(castIronMassDeviationLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(castIronMassDeviationInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel transportTimeDeviationLabel = new JLabel(TIME_TRANSPORT_DEVIATION);
        tempHorPanel.add(transportTimeDeviationLabel);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_HALF_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel slugRemovalDepDevLabel = new JLabel(TRANSPORT_TIME_TO_SLUG_REMOVAL_DEVIATION_LABEL);
        tempHorPanel.add(slugRemovalDepDevLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(slugRemovalDepTransTimeDeviationInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_HALF_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel mixerDepDevLabel = new JLabel(TRANSPORT_TIME_TO_MIXER_DEVIATION_LABEL);
        tempHorPanel.add(mixerDepDevLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(mixerDepTransTimeDeviationInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel desulfurationCountLabel = new JLabel(DESULFURATION_COUNT);
        tempHorPanel.add(desulfurationCountLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(desulfurationCountInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel ladlesCountDeviationLabel = new JLabel(LADLES_COUNT_DEVIATION);
        tempHorPanel.add(ladlesCountDeviationLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(ladleCountDeviationInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));

        GUITools.makeSameSize(castIronMassDeviationLabel, transportTimeDeviationLabel, slugRemovalDepDevLabel,
                mixerDepDevLabel, desulfurationCountLabel, ladlesCountDeviationLabel, castIronStartTempLabel);

        return inputsPanel;
    }

    private void shutdown() {
        mainFrame.setVisible(false);
        mainFrame.dispose();
    }
}
