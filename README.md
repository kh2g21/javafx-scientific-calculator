**Calculator Application**

**Overview**

This repository contains the source code for a simple scientific calculator application built using JavaFX. The application allows users to perform basic arithmetic operations as well as some scientific functions such as logarithms, square roots, and trigonometric functions.

**Project Structure**

- **calculator.css**: This file contains the CSS styles used to customize the appearance of the calculator's user interface. It defines the font, padding, colors, and other visual properties of the buttons, display area, and checkboxes.

- **Calculator.fxml**: This file is written in FXML (JavaFX's XML-based markup language) and defines the layout of the calculator's user interface. It specifies the arrangement of buttons, text fields, and checkboxes within the calculator window.

- **CalculatorApp.java**: This is the main entry point of the application. It contains the `main` method and is responsible for launching the JavaFX application. It loads the FXML file and initializes the scene and controller.

- **CalculatorController.java**: This class serves as the controller for the calculator application. It handles user interactions, such as button clicks and checkbox selections, and performs the corresponding calculations or updates the display accordingly. It also contains event handler methods for the different UI components.

**Running the Application**

To run the calculator application, you'll need to have Java and JavaFX installed on your system. You can compile and run the `CalculatorApp.java` file using your favorite IDE or build tool.

**Features**

- Basic arithmetic operations (addition, subtraction, multiplication, division)
- Scientific functions (logarithms, square roots, trigonometric functions)
- Option to switch between degrees and radians for trigonometric functions
- Conversion of decimal numbers to fractions
- Customizable appearance using CSS styles
