package com.company.controller;

import com.company.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Muhammad Shah
 * @version Java 16
 */
public class SimpleController {
    boolean clear = true, decimal, equal;
    String operator = "+";
    double num1 = 0.0, num2 = 0.0, result = 0.0;

    @FXML
    private Label lblExpression;

    @FXML
    private Label lblResult;

    private List<String> calculationHistory = new ArrayList<>();

    //handlers
    public void onMouseClicked(MouseEvent event) {
        var button = (Button) event.getSource();
        String buttonText = button.getText();

        switch (buttonText) {
            case "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" -> insertNumber(buttonText);
            case "." -> insertDecimal(buttonText);
            case "+", "-", "/", "x" -> insertOperator(buttonText);
            case "+/-" -> inverseSign();
            case "CLEAR" -> clearExpression();
            case "=" -> showResult();
            case "DELETE" -> deleteLast();
            case "HIST" -> openHistoryWindow();
        }
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
        String buttonText = code.getChar();

        switch (code) {
            case DIGIT0, NUMPAD0 -> insertNumber("0");
            case DIGIT1, NUMPAD1 -> insertNumber("1");
            case DIGIT2, NUMPAD2 -> insertNumber("2");
            case DIGIT3, NUMPAD3 -> insertNumber("3");
            case DIGIT4, NUMPAD4 -> insertNumber("4");
            case DIGIT5, NUMPAD5 -> insertNumber("5");
            case DIGIT6, NUMPAD6 -> insertNumber("6");
            case DIGIT7, NUMPAD7 -> insertNumber("7");
            case DIGIT8, NUMPAD8 -> insertNumber("8");
            case DIGIT9, NUMPAD9 -> insertNumber("9");

            case DECIMAL, PERIOD -> insertDecimal(".");

            case ADD, PLUS -> insertOperator("+");
            case SUBTRACT, MINUS -> insertOperator("-");
            case MULTIPLY -> insertOperator("x");
            case DIVIDE -> insertOperator("/");

            case F9 -> inverseSign();
            case DELETE -> clearExpression();
            case ENTER -> showResult();
        }
        System.out.println("name = " + code.getName() + ", code = " + code.getCode() + ", char = " + code.getChar());
    }

    public void inverseSign() {
        double d = -1 *  Double.parseDouble(lblResult.getText());
        lblResult.setText(getOutput(d));
    }

    public void insertNumber(String number) {
        if (clear) {
            lblResult.setText("");
            clear = false;
        }
        if (!equal) {
            lblExpression.setText("");
        }
        lblResult.setText(lblResult.getText() + number);
    }

    public void insertOperator(String operator) {
        this.operator = operator;
        num1 = Double.parseDouble(lblResult.getText());
        lblExpression.setText(lblResult.getText() + " " + operator + " ");
        clear = true;
        equal = true;
        decimal = false;
    }

    public void showResult() {
        if (lblResult.getText().equals("Infinity") | lblResult.getText().equals("NaN")) {
            clearExpression();
        }
        if (equal) {
            num2 = Double.parseDouble(lblResult.getText());
            equal = false;
        } else {
            num1 = Double.parseDouble(lblResult.getText());
        }
        String operand1 = getOutput(num1);
        String operand2 = getOutput(num2);
        String total = "";
        String output = "";

        result = calculation(num1, num2);

        if (operator.equals("/") && (num2 == 0)) {
            total = String.valueOf(result);
        } else {
            total = getOutput(result);
        }

        output = operand1 + " " + operator + " " + operand2 + " = ";
        lblExpression.setText(output);
        lblResult.setText(total);
        this.calculationHistory.add(output + total);
        System.out.println(output + total);
        clear = true;
        decimal = false;
        equal = false;
    }

    private double calculation(double num1, double num2) {
        return switch (operator) {
            case "+" -> result = num1 + num2;
            case "-" -> result = num1 - num2;
            case "x" -> result = num1 * num2;
            case "/" -> result = num1 / num2;
            default -> throw new IllegalStateException("Unexpected value: " + operator);
        };
    }

    private String getOutput(double d) {
        return (d == Math.ceil(d)) ? String.valueOf((int) d) : String.valueOf(d);
    }

    public void deleteLast() {
        if (!lblExpression.getText().isEmpty()) {
            lblExpression.setText("");
        }
        if (!clear) {
            var text = new StringBuilder(lblResult.getText());
            int lastIndex = text.length() - 1;
            if (lastIndex != 0) {
                text.deleteCharAt(lastIndex);
            } else {
                text.replace(0, 1, "0");
            }
            lblResult.setText(text.toString());
        }
    }

    public void clearExpression() {
        lblExpression.setText("");
        lblResult.setText("0");
        num1 = num2 = result = 0.0;
        operator = "+";
        clear = true;
        equal = false;
        decimal = false;
    }

    public void openHistoryWindow() {
        try {
            var loader = new FXMLLoader(getClass().getResource("/fxml/history.fxml"));
            Parent root = loader.load();
            Main.getHistoryStage().setScene(new Scene(root));

            HistoryController historyController = loader.getController();
            historyController.initializeCalculation(calculationHistory);
            Main.getHistoryStage().show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void insertDecimal(String buttonText) {
        if (!decimal) {
            if (lblResult.getText().equals("0")) {
                clear = false;
            }
            insertNumber(buttonText);
            if (lblResult.getText().equals(".")) {
                lblResult.setText("0.");
            }
            //equal = true;
            decimal = true;
        }
    }
}