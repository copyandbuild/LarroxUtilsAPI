package dev.larrox.larroxUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class MathUtils {
   public double evaluate(String expression) {
      expression = expression.replaceAll("\\s", "");
      List<String> postfix = this.toPostfix(expression);
      return this.evaluatePostfix(postfix);
   }

   public int convertToMinutes(String timeStr) {
      try {
         char unit = timeStr.charAt(timeStr.length() - 1);
         int value = Integer.parseInt(timeStr.substring(0, timeStr.length() - 1));
         int var10000;
         switch(unit) {
         case 'M':
            var10000 = value * 60 * 60 * 24 * 28;
            break;
         case 'd':
            var10000 = value * 60 * 60 * 24;
            break;
         case 'h':
            var10000 = value * 60 * 60;
            break;
         case 'm':
            var10000 = value * 60;
            break;
         case 's':
            var10000 = value;
            break;
         case 'w':
            var10000 = value * 60 * 60 * 24 * 7;
            break;
         case 'y':
            var10000 = value * 60 * 60 * 24 * 365;
            break;
         default:
            var10000 = 1;
         }

         return var10000;
      } catch (StringIndexOutOfBoundsException | NumberFormatException var4) {
         return 1;
      }
   }

   private List<String> toPostfix(String expression) {
      Stack<Character> operators = new Stack();
      List<String> postfix = new ArrayList();
      StringBuilder numberBuffer = new StringBuilder();

      for(int i = 0; i < expression.length(); ++i) {
         char c = expression.charAt(i);
         if (!Character.isDigit(c) && c != '.') {
            if (Character.isLetter(c)) {
               numberBuffer.append(c);
            } else {
               if (!numberBuffer.isEmpty()) {
                  postfix.add(numberBuffer.toString());
                  numberBuffer.setLength(0);
               }

               if (c == '(') {
                  operators.push(c);
               } else if (c == ')') {
                  while(!operators.isEmpty() && (Character)operators.peek() != '(') {
                     postfix.add(String.valueOf(operators.pop()));
                  }

                  if (operators.isEmpty()) {
                     throw new IllegalArgumentException("Mismatched parentheses");
                  }

                  operators.pop();
               } else {
                  if (!this.isOperator(c)) {
                     throw new IllegalArgumentException("Invalid character: " + c);
                  }

                  while(!operators.isEmpty() && (this.precedence(c) < this.precedence((Character)operators.peek()) || this.precedence(c) == this.precedence((Character)operators.peek()) && c != '^')) {
                     postfix.add(String.valueOf(operators.pop()));
                  }

                  operators.push(c);
               }
            }
         } else {
            numberBuffer.append(c);
         }
      }

      if (!numberBuffer.isEmpty()) {
         postfix.add(numberBuffer.toString());
      }

      while(!operators.isEmpty()) {
         char op = (Character)operators.pop();
         if (op == '(') {
            throw new IllegalArgumentException("Mismatched parentheses");
         }

         postfix.add(String.valueOf(op));
      }

      return postfix;
   }

   private double evaluatePostfix(List<String> postfix) {
      Stack<Double> stack = new Stack();
      Iterator var3 = postfix.iterator();

      while(var3.hasNext()) {
         String token = (String)var3.next();
         if (this.isNumeric(token)) {
            stack.push(Double.parseDouble(token));
         } else {
            double value;
            if (this.isOperator(token.charAt(0))) {
               if (stack.size() < 2) {
                  throw new IllegalArgumentException("Invalid expression");
               }

               value = (Double)stack.pop();
               double a = (Double)stack.pop();
               stack.push(this.applyOperation(token.charAt(0), value, a));
            } else if (this.isFunction(token)) {
               if (stack.isEmpty()) {
                  throw new IllegalArgumentException("Invalid expression");
               }

               value = (Double)stack.pop();
               stack.push(this.applyFunction(token, value));
            } else if (token.equalsIgnoreCase("pi")) {
               stack.push(3.141592653589793D);
            } else {
               if (!token.equalsIgnoreCase("e")) {
                  throw new IllegalArgumentException("Invalid token: " + token);
               }

               stack.push(2.718281828459045D);
            }
         }
      }

      if (stack.size() != 1) {
         throw new IllegalArgumentException("Invalid expression");
      } else {
         return (Double)stack.pop();
      }
   }

   private boolean isOperator(char c) {
      return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
   }

   private boolean isFunction(String token) {
      return token.equalsIgnoreCase("sin") || token.equalsIgnoreCase("cos") || token.equalsIgnoreCase("tan") || token.equalsIgnoreCase("sqrt") || token.equalsIgnoreCase("log") || token.equalsIgnoreCase("ln");
   }

   private int precedence(char operator) {
      byte var10000;
      switch(operator) {
      case '*':
      case '/':
         var10000 = 2;
         break;
      case '+':
      case '-':
         var10000 = 1;
         break;
      case '^':
         var10000 = 3;
         break;
      default:
         var10000 = -1;
      }

      return var10000;
   }

   private double applyOperation(char operator, double b, double a) {
      double var10000;
      switch(operator) {
      case '*':
         var10000 = a * b;
         break;
      case '+':
         var10000 = a + b;
         break;
      case '-':
         var10000 = a - b;
         break;
      case '/':
         if (b == 0.0D) {
            throw new ArithmeticException("Division by zero");
         }

         var10000 = a / b;
         break;
      case '^':
         var10000 = Math.pow(a, b);
         break;
      default:
         throw new IllegalArgumentException("Unknown operator: " + operator);
      }

      return var10000;
   }

   private double applyFunction(String function, double value) {
      String var4 = function.toLowerCase();
      byte var5 = -1;
      switch(var4.hashCode()) {
      case 3458:
         if (var4.equals("ln")) {
            var5 = 5;
         }
         break;
      case 98695:
         if (var4.equals("cos")) {
            var5 = 1;
         }
         break;
      case 107332:
         if (var4.equals("log")) {
            var5 = 4;
         }
         break;
      case 113880:
         if (var4.equals("sin")) {
            var5 = 0;
         }
         break;
      case 114593:
         if (var4.equals("tan")) {
            var5 = 2;
         }
         break;
      case 3538208:
         if (var4.equals("sqrt")) {
            var5 = 3;
         }
      }

      double var10000;
      switch(var5) {
      case 0:
         var10000 = Math.sin(value);
         break;
      case 1:
         var10000 = Math.cos(value);
         break;
      case 2:
         var10000 = Math.tan(value);
         break;
      case 3:
         if (value < 0.0D) {
            throw new ArithmeticException("Square root of negative number");
         }

         var10000 = Math.sqrt(value);
         break;
      case 4:
         if (value <= 0.0D) {
            throw new ArithmeticException("Logarithm of non-positive number");
         }

         var10000 = Math.log10(value);
         break;
      case 5:
         if (value <= 0.0D) {
            throw new ArithmeticException("Natural logarithm of non-positive number");
         }

         var10000 = Math.log(value);
         break;
      default:
         throw new IllegalArgumentException("Unknown function: " + function);
      }

      return var10000;
   }

   private boolean isNumeric(String str) {
      try {
         Double.parseDouble(str);
         return true;
      } catch (NumberFormatException var3) {
         return false;
      }
   }

   public double round(double value, short places) {
      if (places < 0) {
         throw new IllegalArgumentException();
      } else {
         long factor = (long)Math.pow(10.0D, (double)places);
         value *= (double)factor;
         long tmp = Math.round(value);
         return (double)tmp / (double)factor;
      }
   }
}
