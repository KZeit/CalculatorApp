package application;
	
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application 
{
	private TextField display;
    private String currentInput = "";
    private boolean isNewInput = false;

    @Override
    public void start(Stage primaryStage) 
    {
        GridPane grid = createButtonGrid();
        HBox hbox = createTopPanel();

        javafx.scene.layout.VBox vbox = new javafx.scene.layout.VBox();
        vbox.getChildren().addAll(hbox, grid);

        Scene scene = new Scene(vbox, 600, 420);
        
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Calculator");
        primaryStage.show();
    }

    private GridPane createButtonGrid() 
    {
        GridPane grid = new GridPane();
        grid.setId("calculatorGrid");
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        display = new TextField();
        display.setEditable(false);
        display.setId("calculatorDisplay");
        
        grid.add(display, 0, 0, 6, 1);

        String[][] buttonLabels = 
        	{
                {"7", "8", "9", "/", "C", "CE"},
                {"4", "5", "6", "*", "sqrt", "x^2"},
                {"1", "2", "3", "-", "1/x", "sin"},
                {"0", ".", "+", "=", "cos", "tan"}
        };

        for (int row = 0; row < buttonLabels.length; row++) 
        {
            for (int col = 0; col < buttonLabels[row].length; col++) 
            {
                final String label = buttonLabels[row][col];
                Button button = new Button(label);
                button.getStyleClass().add("calculator-button");
                button.setOnAction(e -> handleInput(label));
                grid.add(button, col, row + 1);
            }
        }

        return grid;
    }

    private HBox createTopPanel() 
    {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);

        return hbox;
    }

    private void handleInput(String input) 
    {
        if (isNewInput) {
            currentInput = "";
            isNewInput = false;
        }

        switch (input) {
            case "=":
                evaluate();
                break;
            case "sqrt":
                calculateSquareRoot();
                break;
            case "x^2":
                calculateSquare();
                break;
            case "1/x":
                calculateReciprocal();
                break;
            case "sin":
                calculateSin();
                break;
            case "cos":
                calculateCos();
                break;
            case "tan":
                calculateTan();
                break;
            case "C":
                clearEntry();
                break;
            case "CE":
                clearAll();
                break;
            default:
                currentInput += input;
                display.setText(currentInput);
        }
    }

    private void evaluate() 
    {
        try {
            double result = evaluateExpression(currentInput);
            display.setText(Double.toString(result));
            isNewInput = true;
        } catch (ArithmeticException | IllegalArgumentException ex) 
        {
            display.setText("Error");
            isNewInput = true;
        }
    }

    private void calculateSquareRoot() 
    {
    	 try 
    	 {
    	        double operand = Double.parseDouble(currentInput);
    	        if (operand < 0) 
    	        {
    	            display.setText("Error");
    	        } else 
    	        {
    	            double result = Math.sqrt(operand);
    	            display.setText(formatResult(result));
    	            isNewInput = true;
    	        }
    	    } catch (NumberFormatException ex) 
    	 {
    	        display.setText("Error");
    	        isNewInput = true;
    	    }
    	}

    private void calculateSquare() 
    {
    	 try 
    	 {
    	        double operand = Double.parseDouble(currentInput);
    	        double result = Math.pow(operand, 2);
    	        display.setText(formatResult(result));
    	        isNewInput = true;
    	    } catch (NumberFormatException ex) 
    	 {
    	        display.setText("Error");
    	        isNewInput = true;
    	    }
    	}

    private void calculateReciprocal() 
    {
        try 
        {
            double operand = Double.parseDouble(currentInput);
            if (operand == 0) 
            {
                display.setText("Error");
            } else 
            {
                double result = 1 / operand;
                display.setText(formatResult(result));
                isNewInput = true;
            }
        } catch (NumberFormatException ex) 
        {
            display.setText("Error");
            isNewInput = true;
        }
    }

    private void calculateSin() 
    {
        try 
        {
            double radians = Math.toRadians(Double.parseDouble(currentInput));
            double result = Math.sin(radians);
            display.setText(formatResult(result));
            isNewInput = true;
        } catch (NumberFormatException ex) 
        {
            display.setText("Error");
            isNewInput = true;
        }
    }

    private void calculateCos() 
    {
        try 
        {
            double radians = Math.toRadians(Double.parseDouble(currentInput));
            double result = Math.cos(radians);
            display.setText(formatResult(result));
            isNewInput = true;
        } catch (NumberFormatException ex) 
        {
            display.setText("Error");
            isNewInput = true;
        }
    }

    private void calculateTan() 
    {
        try 
        {
            double radians = Math.toRadians(Double.parseDouble(currentInput));
            double result = Math.tan(radians);
            display.setText(formatResult(result));
            isNewInput = true;
        } catch (NumberFormatException ex) 
        {
            display.setText("Error");
            isNewInput = true;
        }
    }
    
    private String formatResult(double result) 
    {
        return result % 1 == 0 ? String.format("%.0f", result) : String.valueOf(result);
    }

    private void clearEntry() 
    {
    	currentInput = "";
        display.setText("");
    }

    private void clearAll() 
    {
        currentInput = "";
        isNewInput = false;
        display.setText("");
    }

    private double evaluateExpression(String expression) 
    {
    	try 
    	{
            return new Object() 
            {
                int pos = -1, ch;

                void nextChar() 
                {
                    ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
                }

                boolean isDigit() 
                {
                    return Character.isDigit(ch);
                }

                double parse() 
                {
                    nextChar();
                    double x = parseExpression();
                    if (pos < expression.length()) 
                    {
                        throw new IllegalArgumentException("Unexpected: " + (char) ch);
                    }
                    
                    return x;
                }

                double parseExpression() 
                {
                    double x = parseTerm();
                    for (; ; ) {
                        if (eat('+')) x += parseTerm();
                        else if (eat('-')) x -= parseTerm();
                        else return x;
                    }
                }

                double parseTerm() 
                {
                    double x = parseFactor();
                    for (; ; ) 
                    {
                        if (eat('*')) x *= parseFactor();
                        else if (eat('/')) x /= parseFactor();
                        else return x;
                    }
                }

                double parseFactor() 
                {
                    if (eat('+')) return parseFactor();
                    if (eat('-')) return -parseFactor();

                    double x;
                    int startPos = this.pos;
                    if (eat('(')) 
                    {
                        x = parseExpression();
                        eat(')');
                    } else if (isDigit() || ch == '.') 
                    {
                        while (isDigit() || ch == '.') nextChar();
                        x = Double.parseDouble(expression.substring(startPos, this.pos));
                    } else {
                        throw new IllegalArgumentException("Unexpected: " + (char) ch);
                    }

                    if (eat('^')) x = Math.pow(x, parseFactor());

                    return x;
                }

                boolean eat(int charToEat) 
                {
                    while (ch == ' ') nextChar();
                    if (ch == charToEat) 
                    {
                        nextChar();
                        return true;
                    }
                    return false;
                }
            }.parse();
        } catch (Exception e) 
    	{
            throw new IllegalArgumentException("Invalid expression");
        }
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}