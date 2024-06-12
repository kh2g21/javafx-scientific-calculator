package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.math.BigInteger;
import java.util.Stack;

public class CalculatorController {

    @FXML
    private TextField display;

    private double num1 = 0;
    private String operator = "";
    private boolean start = true;

    private Stack<String> expressionStack = new Stack<>();

    @FXML
    private CheckBox degreeCheckBox;

    @FXML
    private CheckBox radianCheckBox;

    @FXML
    public void initialize() {
        // Ensure only one checkbox can be checked at a time
        degreeCheckBox.setSelected(true);  // Default to Degrees
        radianCheckBox.setSelected(false);

    }

    @FXML
    private void handleDegreeCheckBoxAction(ActionEvent event) {
        if (degreeCheckBox.isSelected()) {
            radianCheckBox.setSelected(false);
        } else {
            degreeCheckBox.setSelected(true);  // Always keep one selected
        }
    }

    @FXML
    private void handleRadianCheckBoxAction(ActionEvent event) {
        if (radianCheckBox.isSelected()) {
            degreeCheckBox.setSelected(false);
        } else {
            radianCheckBox.setSelected(true);  // Always keep one selected
        }
    }

    @FXML
    private void processNumbers(ActionEvent event) {
        if (start) {
            display.setText("");
            start = false;
        }

        String value = ((Button) event.getSource()).getText();
        display.setText(display.getText() + value);
    }

    @FXML
    private void processOperators(ActionEvent event) {
        String value = ((Button) event.getSource()).getText();

        if ("log".equals(value) || "ln".equals(value) || "sin".equals(value) || "cos".equals(value) || "tan".equals(value) ||
                "√".equals(value) || "∛".equals(value) || "arcsin".equals(value) || "arccos".equals(value) || "arctan".equals(value)) {
            if (!display.getText().isEmpty()) {
                expressionStack.push(display.getText());
            }
            expressionStack.push(value);
            display.setText("");
            operator = value;
            return;
        }

        if (!"=".equals(value)) {
            if (!operator.isEmpty() && !"^".equals(operator) && !operator.equals("log") && !operator.equals("ln") && !operator.equals("sin") && !operator.equals("cos") && !operator.equals("tan") && !operator.equals("√") && !operator.equals("∛")
                    && !operator.equals("arcsin") && !operator.equals("arccos") && !operator.equals("arctan"))
                return;

            if (!display.getText().isEmpty()) {
                expressionStack.push(display.getText());
            }

            expressionStack.push(value);
            operator = value;
            display.setText("");

        } else {
            if (!display.getText().isEmpty()) {
                expressionStack.push(display.getText());
            }

            double result = evaluateExpression(expressionStack);
            display.setText(String.valueOf(result));
            start = true;
            expressionStack.clear();
        }
    }

    @FXML
    private void clearDisplay(ActionEvent event) {
        display.setText("");
        num1 = 0;
        operator = "";
        start = true;
        expressionStack.clear();
    }

    private double evaluateExpression(Stack<String> expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<String> operators = new Stack<>();

        while (!expression.isEmpty()) {
            String token = expression.remove(0);

            if (isNumeric(token)) {
                numbers.push(Double.parseDouble(token));
            } else if ("log".equals(token) || "ln".equals(token) || "sin".equals(token) || "cos".equals(token) || "tan".equals(token) ||
                    "√".equals(token) || "∛".equals(token) || "arcsin".equals(token) || "arccos".equals(token) || "arctan".equals(token)) {
                double num = Double.parseDouble(expression.remove(0));
                numbers.push(calculateUnary(num, token));
            } else {
                while (!operators.isEmpty() && hasPrecedence(token, operators.peek())) {
                    numbers.push(calculate(numbers.pop(), numbers.pop(), operators.pop()));
                }
                operators.push(token);
            }
        }

        while (!operators.isEmpty()) {
            numbers.push(calculate(numbers.pop(), numbers.pop(), operators.pop()));
        }

        return numbers.pop();
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean hasPrecedence(String op1, String op2) {
        if ((op1.equals("*") || op1.equals("/")) && (op2.equals("+") || op2.equals("-"))) {
            return false;
        } else if (op1.equals("^") && (op2.equals("*") || op2.equals("/"))) {
            return false;
        } else {
            return true;
        }
    }

    private double calculate(double num1, double num2, String operator) {
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 == 0) {
                    return 0; // handle division by zero
                }
                return num2 / num1;
            case "^":
                return Math.pow(num2, num1); // Note: num2 is the base and num1 is the exponent
            default:
                return 0;
        }
    }

    private double calculateUnary(double num, String operator) {
        boolean useDegrees = degreeCheckBox.isSelected();

        switch (operator) {
            case "log":
                return Math.log10(num);
            case "√":
                return Math.sqrt(num);
            case "∛":
                return Math.cbrt(num);
            case "ln":
                return Math.log(num);
            case "sin":
                return useDegrees ? Math.sin(Math.toRadians(num)) : Math.sin(num);
            case "cos":
                return useDegrees ? Math.cos(Math.toRadians(num)) : Math.cos(num);
            case "tan":
                return useDegrees ? Math.tan(Math.toRadians(num)) : Math.tan(num);
            case "arcsin":
                return useDegrees ? Math.toDegrees(Math.asin(num)) : Math.asin(num);
            case "arccos":
                return useDegrees ? Math.toDegrees(Math.acos(num)) : Math.acos(num);
            case "arctan":
                return useDegrees ? Math.toDegrees(Math.atan(num)) : Math.atan(num);
            default:
                return 0;
        }
    }

    @FXML
    private void convertToFraction(ActionEvent event) {
        String text = display.getText();
        if (isNumeric(text)) {
            double decimal = Double.parseDouble(text);
            String fraction = decimalToFraction(decimal);
            display.setText(fraction);
        }
    }

    private String decimalToFraction(double decimal) {
        if (decimal == (long) decimal) {
            return String.format("%d/1", (long) decimal);
        }

        final double tolerance = 1.0E-6;
        double h1 = 1, h2 = 0, k1 = 0, k2 = 1;
        double b = decimal;
        do {
            double a = Math.floor(b);
            double aux = h1;
            h1 = a * h1 + h2;
            h2 = aux;
            aux = k1;
            k1 = a * k1 + k2;
            k2 = aux;
            b = 1 / (b - a);
        } while (Math.abs(decimal - h1 / k1) > decimal * tolerance);

        BigInteger numerator = BigInteger.valueOf((long) h1);
        BigInteger denominator = BigInteger.valueOf((long) k1);
        BigInteger gcd = numerator.gcd(denominator);

        return numerator.divide(gcd) + "/" + denominator.divide(gcd);
    }
}

