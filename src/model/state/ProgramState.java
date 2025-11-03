package model.state;

/*Program state refers to the current state the program is in,
The commands that need to be executed, the variables that are yet
in use and the output resulted so far.
* */
/*
* Difference between a Record and a Class is that the record automatically
* creates the constructor and the getters, while in a class I should've done
* all of this by myself
* */
public record ProgramState
        (ExecutionStack executionStack, SymbolTable symbolTable, Out out){

    @Override
    public String toString() {
        return "ExeStack={" + executionStack + "}\n" +
                "SymbolTable=" + symbolTable + "\n" +
                "Output=" + out + "\n";
    }
}
