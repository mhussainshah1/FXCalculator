package com.company.controller;

import com.company.Main;
import com.company.utils.EvaluateString;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExpressionController {
    @FXML
    private Label expression;

    @FXML
    private Label result;

    private List<String> calculationHistory = new ArrayList<>();

    //beans
    public Label getExpression() {
        return expression;
    }

    public Label getResult() {
        return result;
    }

    public void setResult(String newResult) {
        this.result.setText("= " + newResult);
    }

    //handlers
    public void onMouseClick(MouseEvent mouseEvent) {
        var button = (Button) mouseEvent.getSource();
        String buttonText = button.getText();

        switch (buttonText) {
            case "1", "2", "3", "4", "5", "6", "7", "8", "9", "0","." -> insertNumber(buttonText);
            case "+", "-", "/", "*" -> insertOperator(buttonText);
            case "CLEAR" -> clearExpression();
            case "=" -> showResult();
            case "ANS" -> insertAnswer(getResult().getText().substring(2));
            case "DELETE" -> deleteLast();
            case "HIST" -> openHistoryWindow();
        }
    }

    //utility methods
    public void insertNumber(String number) {
        expression.setText(expression.getText() + number);
    }

    public void insertOperator(String operator) {
        expression.setText(expression.getText() + " " + operator + " ");
    }

    public void clearExpression() {
        expression.setText("");
    }

    private void showResult() {
        int result = EvaluateString.evaluate(this.getExpression().getText());
        addCalculation(getExpression().getText(), String.valueOf(result));
        setResult(String.valueOf(result));
    }

    public void addCalculation(String expression, String result){
        this.calculationHistory.add(expression + " = " +result);
    }

    private void insertAnswer(String answer) {
        expression.setText(expression.getText() + answer);
    }

    private void deleteLast() {
        if(!getExpression().getText().isEmpty()){
            var text = new StringBuilder(getExpression().getText());
            text.deleteCharAt(text.length() - 1);
            getExpression().setText(text.toString());
        }
    }

    private void openHistoryWindow() {
        try {
            var loader = new FXMLLoader(getClass().getResource("/com/company/resources/history.fxml"));
            Parent root = loader.load();
            Main.getHistoryStage().setScene(new Scene(root));

            HistoryController historyController = loader.getController();
            historyController.initializeCalculation(calculationHistory);
            Main.getHistoryStage().show();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}