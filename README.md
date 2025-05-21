ALLCAPS is a custom low-level language created by me in Java.
Commands:
* ALLOC <int> <int> -> loads the top of the stack in that location of the memory map and pops the stack once
* RETRIEVE <int> <int> -> loads the item located at that location of the memory map to the stack without damaging the memory
* PRINT commands (pops all values it prints)
  * PRINT CHAR ALL -> prints all items in the stack as if they were ascii codes for chars; the top integer is printed first
  * PRINT INT ALL -> prints all items in the stack from top to bottom
  * PRINT CHAR <int> -> prints the latest n chars of the stack
  * PRINT INT <int> -> prints the latest n integers of the stack
* PUSH commands
  * PUSH INT <int> -> pushes an integer to the top of the stack
  * PUSH CHAR <char> -> pushes the ASCII value of a char to the top of the stack
  * PUSH STRING <string> -> pushes all chars of a string to the top of the stack, in reverse order
* POP -> pops the top of the stack
* DUPLICATE -> duplicates the top of the stack
* SWAP -> swaps the two top values of the stack
* ADD -> pops the top two values of the stack and pushes their sum
* INPUT commands
  * INPUT INT -> asks the user for an integer, and pushes that on the stack
  * INPUT CHAR -> asks the user for a character, and pushes that on the stack
  * INPUT LINE ON STACK -> asks the user for a line of texts, then pushes that on the stack in reverse order
* WAIT <int> -> Waits for n milliseconds
* RANDOM -> pushes a random number between 0 and 255 (inclusive) on the stack
* SUBTRACT -> pops the top two values of the stack and pushes the result of their subtraction
* CLEAR commands
  * CLEAR STACK -> empties the stack
  * CLEAR MEMORY -> sets all memory slots to their original state (0 but unusable because there's a BUG)
  * CLEAR CELL <int> <int> -> sets a cell in the memory to 0
* GOTO commands
  * GOTO LINE STACK -> goes to the line specified by the number on top of the stack, and pops the stack once
  * GOTO LINE <int> -> goes to line n
  * GOTO LABEL <string> -> goes to the line which starts with "LABEL <n>" and a space, with n being the input
* MULTIPLY -> pops the two values at the top of the stack and pushes their product
* IF commands
  * IF EXPRESSION <expr>:<command> -> pops the top two values of the stack, and executes the single-line command specified based on whether or not the expression is lawful for the two values. The expression can be one of:"==", ">", "<", ">=", and "<=".
  * IF STACK <command> -> executes the specified single-line command if the value at the top of the stack is not equal to 0, and pops the stack once
* DIVIDE -> pops the two values at the top of the stack, and pushes the integer division of those two
* ELSE -> Executes a command if the last if/else block was not performed
* DEBUG commands
  * DEBUG STACK -> prints the stack
  * DEBUG MEMORY -> prints all values of memory that have been touched, without any sort of specification which thing belongs to what
* NEWLINE -> prints the \n character
* SPACE -> prints the space character
* AND -> pops the top two values of the stack, and pushes one if they are both non-zero, pushes zero otherwise
* OR -> pops the top two values of the stack, and pushes one if one of them is non-zero, pushes zero otherwise
* INVERT -> pops the top value of the stack, pushes one if it was zero, pushes zero if it was any other integer
* SIZE -> pushes the size of the stack
* XOR -> pops the top two values of the stack, and pushes one if exactly one of them is non-zero, pushes zero otherwise
* PRINTLN -> executes a PRINT command with all extra arguments, and executes a NEWLINE command
* NEGATE -> pops the top of the stack <code>n</code>, and pushes <code>-n</code>
* FLIPSTACK -> reverses the stack order
* ERROR -> prints a generic error message and ends the program
* INCREMENT -> increments the top of the stack
* DECREMENT -> decrements the top of the stack
* END -> ends the program, required so you don't throw an exception
* LOAD commands
  * LOAD FILE <string> -> makes a reader for that file and pushes an integer key to the stack, which controls that reader
  * LOAD CHAR FROM <int> -> loads a single char from the file specified by the integer and pushes that on the stack
  * LOAD CHAR FROM STACK -> loads a single char from the file specified by the integer on the top of the stack, then pops that integer
  * CLOSE FILE <int> -> closes the file associated with the integer and removes all bindings
* EQUALS -> pops the two values on top of the stack, pushes 1 if they are equal, 0 otherwise
