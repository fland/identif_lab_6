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
    private final static String EXIT_BTN_TEXT = "Exit";

    private final static String X_START_VALUE_LABEL_TEXT = "x start value: ";
    private final static String X_END_VALUE_LABEL_TEXT = "x end value: ";
    private final static String X_COEFF_LABEL_TEXT = "x coeff: ";
    private final static String FREE_TERM_LABEL_TEXT = "Free term: ";
    private final static String FIRST_EQUATION_LABEL_TEXT = "First equation: ";
    private final static String SECOND_EQUATION_LABEL_TEXT = "Second equation: ";

    private final JFrame mainFrame;

    private final JTextField firstEqXStartValueInput;
    private final JTextField firstEqXEndValueInput;
    private final JTextField firstEqXCoeffInput;
    private final JTextField firstEqFreeTermInput;
    private final JTextField secondEqXStartValueInput;
    private final JTextField secondEqXEndValueInput;
    private final JTextField secondEqXCoeffInput;
    private final JTextField secondEqFreeTermInput;

    public MainWindow() {
        mainFrame = new JFrame("Lab 6");
        mainFrame.setSize(MAIN_FRAME_SIZE);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        firstEqXEndValueInput = new JTextField("0.15");
        GUITools.fixTextFieldSize(firstEqXEndValueInput);
        firstEqXEndValueInput.setCaretPosition(0);

        firstEqXCoeffInput = new JTextField("0.066666667");
        GUITools.fixTextFieldSize(firstEqXCoeffInput);
        firstEqXCoeffInput.setCaretPosition(0);

        firstEqFreeTermInput = new JTextField("1.2");
        GUITools.fixTextFieldSize(firstEqFreeTermInput);
        firstEqFreeTermInput.setCaretPosition(0);

        firstEqXStartValueInput = new JTextField("0.0");
        GUITools.fixTextFieldSize(firstEqXStartValueInput);
        firstEqXStartValueInput.setCaretPosition(0);

        secondEqXEndValueInput = new JTextField("0.16");
        GUITools.fixTextFieldSize(secondEqXEndValueInput);
        secondEqXEndValueInput.setCaretPosition(0);

        secondEqXCoeffInput = new JTextField("-2.705882353");
        GUITools.fixTextFieldSize(secondEqXCoeffInput);
        secondEqXCoeffInput.setCaretPosition(0);

        secondEqFreeTermInput = new JTextField("3.905882353");
        GUITools.fixTextFieldSize(secondEqFreeTermInput);
        secondEqFreeTermInput.setCaretPosition(0);

        secondEqXStartValueInput = new JTextField("1.0");
        GUITools.fixTextFieldSize(secondEqXStartValueInput);
        secondEqXStartValueInput.setCaretPosition(0);

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
        JLabel firstEquationLabel = new JLabel(FIRST_EQUATION_LABEL_TEXT);
        tempHorPanel.add(firstEquationLabel);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_HALF_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel firstEqXStartValueLabel = new JLabel(X_START_VALUE_LABEL_TEXT);
        tempHorPanel.add(firstEqXStartValueLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(firstEqXStartValueInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel firstEqXEndValueLabel = new JLabel(X_END_VALUE_LABEL_TEXT);
        tempHorPanel.add(firstEqXEndValueLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(firstEqXEndValueInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel firstEqXCoeffLabel = new JLabel(X_COEFF_LABEL_TEXT);
        tempHorPanel.add(firstEqXCoeffLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(firstEqXCoeffInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_HALF_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel firstEqFreeTermLabel = new JLabel(FREE_TERM_LABEL_TEXT);
        tempHorPanel.add(firstEqFreeTermLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(firstEqFreeTermInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));

        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel secondEquationLabel = new JLabel(SECOND_EQUATION_LABEL_TEXT);
        tempHorPanel.add(secondEquationLabel);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_HALF_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel secondEqXStartValueLabel = new JLabel(X_START_VALUE_LABEL_TEXT);
        tempHorPanel.add(secondEqXStartValueLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(secondEqXStartValueInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel secondEqXEndValueLabel = new JLabel(X_END_VALUE_LABEL_TEXT);
        tempHorPanel.add(secondEqXEndValueLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(secondEqXEndValueInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel secondEqXCoeffLabel = new JLabel(X_COEFF_LABEL_TEXT);
        tempHorPanel.add(secondEqXCoeffLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(secondEqXCoeffInput);
        inputsPanel.add(tempHorPanel);
        inputsPanel.add(Box.createRigidArea(StandardDimension.VER_HALF_RIGID_AREA.getValue()));

        tempHorPanel = BoxLayoutUtils.createHorizontalPanel();
        JLabel secondEqFreeTermLabel = new JLabel(FREE_TERM_LABEL_TEXT);
        tempHorPanel.add(secondEqFreeTermLabel);
        tempHorPanel.add(Box.createRigidArea(StandardDimension.HOR_HALF_RIGID_AREA.getValue()));
        tempHorPanel.add(secondEqFreeTermInput);
        inputsPanel.add(tempHorPanel);

        GUITools.makeSameSize(firstEqXEndValueLabel, firstEqXCoeffLabel, firstEqFreeTermLabel, firstEqXStartValueLabel,
                secondEqFreeTermLabel, secondEqXCoeffLabel, secondEqXEndValueLabel, secondEqXStartValueLabel);

        return inputsPanel;
    }

    private void shutdown() {
        mainFrame.setVisible(false);
        mainFrame.dispose();
    }
}
