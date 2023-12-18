import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleCalculator {
    private JFrame frame;
    private JTextField display;
    private double currentNumber = 0;
    private String currentOperator = "";
    private boolean isNewNumber = true;

    public SimpleCalculator() {
        frame = new JFrame("Simple Calculator");
        frame.setSize(300, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        display = new JTextField();
        display.setEditable(false);
        frame.add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4));

        String[] buttonLabels = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }

        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String buttonText = source.getText();

            try {
                handleButtonClick(buttonText);
            } catch (ArithmeticException ex) {
                display.setText("Error: " + ex.getMessage());
                clearDisplayAfterDelay();
            }
        }

        private void handleButtonClick(String buttonText) {
            if (isNewNumber) {
                display.setText("");
                isNewNumber = false;
            }

            if (buttonText.matches("[0-9.]")) {
                display.setText(display.getText() + buttonText);
            } else if (buttonText.equals("=")) {
                performOperation();
            } else {
                setOperator(buttonText);
            }
        }

        private void setOperator(String operator) {
            if (!currentOperator.isEmpty()) {
                performOperation();
            }

            currentOperator = operator;
            currentNumber = Double.parseDouble(display.getText());
            isNewNumber = true;
        }

        private void performOperation() {
            double newNumber = Double.parseDouble(display.getText());

            switch (currentOperator) {
                case "+":
                    currentNumber += newNumber;
                    break;
                case "-":
                    currentNumber -= newNumber;
                    break;
                case "*":
                    currentNumber *= newNumber;
                    break;
                case "/":
                    if (newNumber == 0) {
                        throw new ArithmeticException("Cannot divide by zero");
                    }
                    currentNumber /= newNumber;
                    break;
            }

            display.setText(String.valueOf(currentNumber));
            currentOperator = "";
        }

        private void clearDisplayAfterDelay() {
            Timer timer = new Timer(5000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    display.setText("");
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimpleCalculator());
    }
}
