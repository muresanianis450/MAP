package view;

import model.statement.*;
import model.expression.*;
import model.type.*;
import model.value.*;

import java.util.ArrayList;
import java.util.List;
import model.ADT.LockTable.ILockTable;

public class ExamplePrograms {

        //Small Container so it keeps count of each programs's description

        public static class ProgramExample{

            private final String description;
            private final Statement program;

            public ProgramExample(String description, Statement program) {
                this.description = description;
                this.program = program;
            }
            public String getDescription() {
                return description;
            }
            public Statement getProgram() {
                return program;
            }
        }

    public static Statement buildSumBody() {
        return new CompoundStatement(
                new VariableDeclarationStatement("v", new IntegerType()),
                new CompoundStatement(
                        new AssignmentStatement("v",
                                new ArithmeticExpression("+",
                                        new VariableExpression("a"),
                                        new VariableExpression("b")
                                )
                        ),
                        new PrintStatement(new VariableExpression("v"))
                )
        );
    }

    public static Statement buildProductBody() {
        return new CompoundStatement(
                new VariableDeclarationStatement("v", new IntegerType()),
                new CompoundStatement(
                        new AssignmentStatement("v",
                                new ArithmeticExpression("*",
                                        new VariableExpression("a"),
                                        new VariableExpression("b")
                                )
                        ),
                        new PrintStatement(new VariableExpression("v"))
                )
        );
    }
    public static List<ProgramExample> getPrograms() {
        List<ProgramExample> examples = new ArrayList<>();

        // --- Example 1 ---
        examples.add(new ProgramExample(
                "int v; v = 2; print v",
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new IntegerType()),
                        new CompoundStatement(
                                new AssignmentStatement("v", new ConstantExpression(new IntegerValue(2))),
                                new PrintStatement(new VariableExpression("v"))
                        )
                )
        ));

        // --- Example 2 ---
        examples.add(new ProgramExample(
                "int a, b; a = 2 + 3*5; b = a + 1; print b",
                new CompoundStatement(
                        new VariableDeclarationStatement("a", new IntegerType()),
                        new CompoundStatement(
                                new VariableDeclarationStatement("b", new IntegerType()),
                                new CompoundStatement(
                                        new AssignmentStatement("a",
                                                new BinaryExpression("+",
                                                        new ConstantExpression(new IntegerValue(2)),
                                                        new BinaryExpression("*",
                                                                new ConstantExpression(new IntegerValue(3)),
                                                                new ConstantExpression(new IntegerValue(5))
                                                        )
                                                )
                                        ),
                                        new CompoundStatement(
                                                new AssignmentStatement("b",
                                                        new BinaryExpression("+",
                                                                new VariableExpression("a"),
                                                                new ConstantExpression(new IntegerValue(1))
                                                        )
                                                ),
                                                new PrintStatement(new VariableExpression("b"))
                                        )
                                )
                        )
                )
        ));

        // --- Example 3 ---
        examples.add(new ProgramExample(
                "bool a; int v; a = true; if (a) then v = 2 else v = 3; print v",
                new CompoundStatement(
                        new VariableDeclarationStatement("a", new BooleanType()),
                        new CompoundStatement(
                                new VariableDeclarationStatement("v", new IntegerType()),
                                new CompoundStatement(
                                        new AssignmentStatement("a", new ConstantExpression(new BooleanValue(true))),
                                        new CompoundStatement(
                                                new IfStatement(
                                                        new VariableExpression("a"),
                                                        new AssignmentStatement("v", new ConstantExpression(new IntegerValue(2))),
                                                        new AssignmentStatement("v", new ConstantExpression(new IntegerValue(3)))
                                                ),
                                                new PrintStatement(new VariableExpression("v"))
                                        )
                                )
                        )
                )
        ));

        // --- Example 4 ---
        examples.add(new ProgramExample(
                "int x; openFile(\"src/numbers.txt\"); readFile(x); print x; readFile(x); print x; closeFile(\"src/numbers.txt\")",
                new CompoundStatement(
                        new VariableDeclarationStatement("x", new IntegerType()),
                        new CompoundStatement(
                                new OpenReadFile(new ConstantExpression(new StringValue("src/numbers.txt"))),
                                new CompoundStatement(
                                        new ReadFile(new ConstantExpression(new StringValue("src/numbers.txt")), "x"),
                                        new CompoundStatement(
                                                new PrintStatement(new VariableExpression("x")),
                                                new CompoundStatement(
                                                        new ReadFile(new ConstantExpression(new StringValue("src/numbers.txt")), "x"),
                                                        new CompoundStatement(
                                                                new PrintStatement(new VariableExpression("x")),
                                                                new CloseReadFile(new ConstantExpression(new StringValue("src/numbers.txt")))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        ));

        // --- Example 5 ---
        examples.add(new ProgramExample(
                "string s; s = \"Hello, World!\"; print s",
                new CompoundStatement(
                        new VariableDeclarationStatement("s", new StringType()),
                        new CompoundStatement(
                                new AssignmentStatement("s", new ConstantExpression(new StringValue("Hello, World!"))),
                                new PrintStatement(new VariableExpression("s"))
                        )
                )
        ));

        // --- Example 6 ---
        examples.add(new ProgramExample(
                "int x; x = 7; print (x > 5)",
                new CompoundStatement(
                        new VariableDeclarationStatement("x", new IntegerType()),
                        new CompoundStatement(
                                new AssignmentStatement("x", new ConstantExpression(new IntegerValue(7))),
                                new PrintStatement(
                                        new RelationalExpression(
                                                new VariableExpression("x"),
                                                new ConstantExpression(new IntegerValue(5)),
                                                ">"
                                        )
                                )
                        )
                )
        ));

        // --- Example 7 ---
        examples.add(new ProgramExample(
                "ref int v; new(v,20); print(*v); *v = 30; print(*v + 5)",
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new RefType(new IntegerType())),
                        new CompoundStatement(
                                new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                                new CompoundStatement(
                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                        new CompoundStatement(
                                                new WriteHeapStatement("v", new ConstantExpression(new IntegerValue(30))),
                                                new PrintStatement(
                                                        new ArithmeticExpression(
                                                                "+",
                                                                new ReadHeapExpression(new VariableExpression("v")),
                                                                new ConstantExpression(new IntegerValue(5))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        ));

        // --- Example 8 ---
        examples.add(new ProgramExample(
                "ref int v; new(v,42); ref ref int a; new(a,v); print(**a)",
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new RefType(new IntegerType())),
                        new CompoundStatement(
                                new NewStatement("v", new ConstantExpression(new IntegerValue(42))),
                                new CompoundStatement(
                                        new VariableDeclarationStatement("a", new RefType(new RefType(new IntegerType()))),
                                        new CompoundStatement(
                                                new NewStatement("a", new VariableExpression("v")),
                                                new PrintStatement(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))))
                                        )
                                )
                        )
                )
        ));

        // --- Example 9 ---
        examples.add(new ProgramExample(
                "int v; v = 4; while (v > 0) { print v; v = v - 1 }; print v",
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new IntegerType()),
                        new CompoundStatement(
                                new AssignmentStatement("v", new ConstantExpression(new IntegerValue(4))),
                                new CompoundStatement(
                                        new WhileStatement(
                                                new RelationalExpression(new VariableExpression("v"), new ConstantExpression(new IntegerValue(0)), ">"),
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("v")),
                                                        new AssignmentStatement("v",
                                                                new ArithmeticExpression("-", new VariableExpression("v"),
                                                                        new ConstantExpression(new IntegerValue(1))
                                                                )
                                                        )
                                                )
                                        ),
                                        new PrintStatement(new VariableExpression("v"))
                                )
                        )
                )
        ));

        // --- Example 10 ---
        examples.add(new ProgramExample(
                "int v; ref int a; v = 10; new(a,22); fork { *a = 30; v = 32; print v; print *a }; print v; print *a",
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new IntegerType()),
                        new CompoundStatement(
                                new VariableDeclarationStatement("a", new RefType(new IntegerType())),
                                new CompoundStatement(
                                        new AssignmentStatement("v", new ConstantExpression(new IntegerValue(10))),
                                        new CompoundStatement(
                                                new NewStatement("a", new ConstantExpression(new IntegerValue(22))),
                                                new CompoundStatement(
                                                        new ForkStatement(
                                                                new CompoundStatement(
                                                                        new WriteHeapStatement("a", new ConstantExpression(new IntegerValue(30))),
                                                                        new CompoundStatement(
                                                                                new AssignmentStatement("v", new ConstantExpression(new IntegerValue(32))),
                                                                                new CompoundStatement(
                                                                                        new PrintStatement(new VariableExpression("v")),
                                                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
                                                                                )
                                                                        )
                                                                )
                                                        ),
                                                        new CompoundStatement(
                                                                new PrintStatement(new VariableExpression("v")),
                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        ));
        examples.add(new ProgramExample(
                "int x; x=2; switch(x) case 1: print 111 case 2: print 222 default: print 333",
                new CompoundStatement(
                        new VariableDeclarationStatement("x", new IntegerType()),
                        new CompoundStatement(
                                new AssignmentStatement("x", new ConstantExpression(new IntegerValue(2))),
                                new SwitchStatement(
                                        new VariableExpression("x"),
                                        new ConstantExpression(new IntegerValue(1)),
                                        new PrintStatement(new ConstantExpression(new IntegerValue(111))),
                                        new ConstantExpression(new IntegerValue(2)),
                                        new PrintStatement(new ConstantExpression(new IntegerValue(222))),
                                        new PrintStatement(new ConstantExpression(new IntegerValue(333)))
                                )
                        )
                )
        ));
        examples.add(new ProgramExample(
                "switch(a*10) with cases (b*c) / 10 / default; final Out = {1,2,300}",
                new CompoundStatement(
                        new VariableDeclarationStatement("a", new IntegerType()),
                        new CompoundStatement(
                                new VariableDeclarationStatement("b", new IntegerType()),
                                new CompoundStatement(
                                        new VariableDeclarationStatement("c", new IntegerType()),
                                        new CompoundStatement(
                                                new AssignmentStatement("a", new ConstantExpression(new IntegerValue(1))),
                                                new CompoundStatement(
                                                        new AssignmentStatement("b", new ConstantExpression(new IntegerValue(2))),
                                                        new CompoundStatement(
                                                                new AssignmentStatement("c", new ConstantExpression(new IntegerValue(5))),
                                                                new CompoundStatement(
                                                                        new SwitchStatement(
                                                                                new ArithmeticExpression("*",
                                                                                        new VariableExpression("a"),
                                                                                        new ConstantExpression(new IntegerValue(10))
                                                                                ),
                                                                                new ArithmeticExpression("*",
                                                                                        new VariableExpression("b"),
                                                                                        new VariableExpression("c")
                                                                                ),
                                                                                new CompoundStatement(
                                                                                        new PrintStatement(new VariableExpression("a")),
                                                                                        new PrintStatement(new VariableExpression("b"))
                                                                                ),
                                                                                new ConstantExpression(new IntegerValue(10)),
                                                                                new CompoundStatement(
                                                                                        new PrintStatement(new ConstantExpression(new IntegerValue(100))),
                                                                                        new PrintStatement(new ConstantExpression(new IntegerValue(200)))
                                                                                ),
                                                                                new PrintStatement(new ConstantExpression(new IntegerValue(300)))
                                                                        ),
                                                                        new PrintStatement(new ConstantExpression(new IntegerValue(300)))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        ));
        examples.add(new ProgramExample(
                "ref int a; new(a,20); (for(v=0;v<3;v=v+1) fork(print(v); v=v*rh(a))); print(rh(a)) ; final Out = {0,1,2,20}",
                new CompoundStatement(
                        new VariableDeclarationStatement("a", new RefType(new IntegerType())),
                        new CompoundStatement(
                                new NewStatement("a", new ConstantExpression(new IntegerValue(20))),
                                new CompoundStatement(
                                        new ForStatement(
                                                "v",
                                                new ConstantExpression(new IntegerValue(0)),
                                                new ConstantExpression(new IntegerValue(3)),
                                                new ArithmeticExpression("+",
                                                        new VariableExpression("v"),
                                                        new ConstantExpression(new IntegerValue(1))
                                                ),
                                                new ForkStatement(
                                                        new CompoundStatement(
                                                                new PrintStatement(new VariableExpression("v")),
                                                                new AssignmentStatement(
                                                                        "v",
                                                                        new ArithmeticExpression("*",
                                                                                new VariableExpression("v"),
                                                                                new ReadHeapExpression(new VariableExpression("a"))
                                                                        )
                                                                )
                                                        )
                                                )
                                        ),
                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
                                )
                        )
                )
        ));

        examples.add(new ProgramExample(
                "Lock example (final Out should be {20,31} or {20,32})",
                new CompoundStatement(
                        new VariableDeclarationStatement("v1", new RefType(new IntegerType())),
                        new CompoundStatement(
                                new VariableDeclarationStatement("v2", new RefType(new IntegerType())),
                                new CompoundStatement(
                                        new VariableDeclarationStatement("x", new IntegerType()),
                                        new CompoundStatement(
                                                new NewStatement("v1", new ConstantExpression(new IntegerValue(20))),
                                                new CompoundStatement(
                                                        new NewStatement("v2", new ConstantExpression(new IntegerValue(30))),
                                                        new CompoundStatement(
                                                                new NewLockStatement("x"),
                                                                new CompoundStatement(

                                                                        // fork( fork(lock x; wh(v1, rh(v1)-1); unlock x); lock x; wh(v1, rh(v1)+1); unlock x )
                                                                        new ForkStatement(
                                                                                new CompoundStatement(
                                                                                        new ForkStatement(
                                                                                                new CompoundStatement(
                                                                                                        new LockStatement("x"),
                                                                                                        new CompoundStatement(
                                                                                                                new WriteHeapStatement(
                                                                                                                        "v1",
                                                                                                                        new ArithmeticExpression(
                                                                                                                                "-",
                                                                                                                                new ReadHeapExpression(new VariableExpression("v1")),
                                                                                                                                new ConstantExpression(new IntegerValue(1))
                                                                                                                        )
                                                                                                                ),
                                                                                                                new UnlockStatement("x")
                                                                                                        )
                                                                                                )
                                                                                        ),
                                                                                        new CompoundStatement(
                                                                                                new LockStatement("x"),
                                                                                                new CompoundStatement(
                                                                                                        new WriteHeapStatement(
                                                                                                                "v1",
                                                                                                                new ArithmeticExpression(
                                                                                                                        "+",
                                                                                                                        new ReadHeapExpression(new VariableExpression("v1")),
                                                                                                                        new ConstantExpression(new IntegerValue(1))
                                                                                                                )
                                                                                                        ),
                                                                                                        new UnlockStatement("x")
                                                                                                )
                                                                                        )
                                                                                )
                                                                        ),

                                                                        new CompoundStatement(
                                                                                // fork( fork( wh(v2, rh(v2)+1) ); wh(v2, rh(v2)+1); unlock(x) )
                                                                                new ForkStatement(
                                                                                        new CompoundStatement(
                                                                                                new ForkStatement(
                                                                                                        new WriteHeapStatement(
                                                                                                                "v2",
                                                                                                                new ArithmeticExpression(
                                                                                                                        "+",
                                                                                                                        new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                                        new ConstantExpression(new IntegerValue(1))
                                                                                                                )
                                                                                                        )
                                                                                                ),
                                                                                                new CompoundStatement(
                                                                                                        new WriteHeapStatement(
                                                                                                                "v2",
                                                                                                                new ArithmeticExpression(
                                                                                                                        "+",
                                                                                                                        new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                                        new ConstantExpression(new IntegerValue(1))
                                                                                                                )
                                                                                                        ),
                                                                                                        new UnlockStatement("x") // spec includes this, even if "x" may not be held here
                                                                                                )
                                                                                        )
                                                                                ),

                                                                                // skip x 9
                                                                                new CompoundStatement(new SkipStatement(),
                                                                                        new CompoundStatement(new SkipStatement(),
                                                                                                new CompoundStatement(new SkipStatement(),
                                                                                                        new CompoundStatement(new SkipStatement(),
                                                                                                                new CompoundStatement(new SkipStatement(),
                                                                                                                        new CompoundStatement(new SkipStatement(),
                                                                                                                                new CompoundStatement(new SkipStatement(),
                                                                                                                                        new CompoundStatement(new SkipStatement(),
                                                                                                                                                new CompoundStatement(new SkipStatement(),

                                                                                                                                                        // print(rh(v1)); print(rh(v2))
                                                                                                                                                        new CompoundStatement(
                                                                                                                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                                                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v2")))
                                                                                                                                                        )

                                                                                                                                                )))))))))
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        ));

        // ================= PROCEDURE DEMO =================

        Statement sumBody =
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new IntegerType()),
                        new CompoundStatement(
                                new AssignmentStatement("v",
                                        new ArithmeticExpression("+",
                                                new VariableExpression("a"),
                                                new VariableExpression("b")
                                        )
                                ),
                                new PrintStatement(new VariableExpression("v"))
                        )
                );

        Statement productBody =
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new IntegerType()),
                        new CompoundStatement(
                                new AssignmentStatement("v",
                                        new ArithmeticExpression("*",
                                                new VariableExpression("a"),
                                                new VariableExpression("b")
                                        )
                                ),
                                new PrintStatement(new VariableExpression("v"))
                        )
                );

        Statement procMain =
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new IntegerType()),
                        new CompoundStatement(
                                new VariableDeclarationStatement("w", new IntegerType()),
                                new CompoundStatement(
                                        new AssignmentStatement("v", new ConstantExpression(new IntegerValue(2))),
                                        new CompoundStatement(
                                                new AssignmentStatement("w", new ConstantExpression(new IntegerValue(5))),
                                                new CompoundStatement(
                                                        new CallStatement("sum", List.of(
                                                                new ArithmeticExpression("*",
                                                                        new VariableExpression("v"),
                                                                        new ConstantExpression(new IntegerValue(10))
                                                                ),
                                                                new VariableExpression("w")
                                                        )),
                                                        new CompoundStatement(
                                                                new PrintStatement(new VariableExpression("v")),
                                                                new CompoundStatement(
                                                                        new ForkStatement(
                                                                                new CallStatement("product",
                                                                                        List.of(new VariableExpression("v"),
                                                                                                new VariableExpression("w")))
                                                                        ),
                                                                        new ForkStatement(
                                                                                new CallStatement("sum",
                                                                                        List.of(new VariableExpression("v"),
                                                                                                new VariableExpression("w")))
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );

        examples.add(new ProgramExample(
                "Procedures demo (final Out should be {25,2,10,7})",
                procMain));


        examples.add(new ProgramExample(
                "Sleep demo: v=0; while(v<3){ fork(print(v); v=v+1); v=v+1 }; sleep(5); print(v*10)  // final Out {0,1,2,30}",
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new IntegerType()),
                        new CompoundStatement(
                                new AssignmentStatement("v", new ConstantExpression(new IntegerValue(0))),
                                new CompoundStatement(
                                        new WhileStatement(
                                                new RelationalExpression(
                                                        new VariableExpression("v"),
                                                        new ConstantExpression(new IntegerValue(3)),
                                                        "<"
                                                ),
                                                new CompoundStatement(
                                                        new ForkStatement(
                                                                new CompoundStatement(
                                                                        new PrintStatement(new VariableExpression("v")),
                                                                        new AssignmentStatement(
                                                                                "v",
                                                                                new ArithmeticExpression("+",
                                                                                        new VariableExpression("v"),
                                                                                        new ConstantExpression(new IntegerValue(1))
                                                                                )
                                                                        )
                                                                )
                                                        ),
                                                        new AssignmentStatement(
                                                                "v",
                                                                new ArithmeticExpression("+",
                                                                        new VariableExpression("v"),
                                                                        new ConstantExpression(new IntegerValue(1))
                                                                )
                                                        )
                                                )
                                        ),
                                        new CompoundStatement(
                                                new SleepStatement(5),
                                                new PrintStatement(
                                                        new ArithmeticExpression("*",
                                                                new VariableExpression("v"),
                                                                new ConstantExpression(new IntegerValue(10))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        ));

        examples.add(new ProgramExample(
                "Ref int v1; Ref int v2; int x; int q; " +
                        "new(v1,20); new(v2,30); newLock(x); " +
                        "fork(fork(lock(x); wh(v1,rh(v1)-1); unlock(x)); lock(x); wh(v1,rh(v1)*10); unlock(x)); " +
                        "newLock(q); " +
                        "fork(fork(lock(q); wh(v2,rh(v2)+5); unlock(q)); lock(q); wh(v2,rh(v2)*10); unlock(q)); " +
                        "nop;nop;nop;nop; " +
                        "lock(x); print(rh(v1)); unlock(x); " +
                        "lock(q); print(rh(v2)); unlock(q); " +
                        "Final Out = {190 or 199, 350 or 305}",
                new CompoundStatement(
                        new VariableDeclarationStatement("v1", new RefType(new IntegerType())),
                        new CompoundStatement(
                                new VariableDeclarationStatement("v2", new RefType(new IntegerType())),
                                new CompoundStatement(
                                        new VariableDeclarationStatement("x", new IntegerType()),
                                        new CompoundStatement(
                                                new VariableDeclarationStatement("q", new IntegerType()),
                                                new CompoundStatement(
                                                        new NewStatement("v1", new ConstantExpression(new IntegerValue(20))),
                                                        new CompoundStatement(
                                                                new NewStatement("v2", new ConstantExpression(new IntegerValue(30))),
                                                                new CompoundStatement(
                                                                        new NewLockStatement("x"),
                                                                        new CompoundStatement(
                                                                                new ForkStatement(
                                                                                        new CompoundStatement(
                                                                                                new ForkStatement(
                                                                                                        new CompoundStatement(
                                                                                                                new LockStatement("x"),
                                                                                                                new CompoundStatement(
                                                                                                                        new WriteHeapStatement(
                                                                                                                                "v1",
                                                                                                                                new ArithmeticExpression("-",
                                                                                                                                        new ReadHeapExpression(new VariableExpression("v1")),
                                                                                                                                        new ConstantExpression(new IntegerValue(1))
                                                                                                                                )
                                                                                                                        ),
                                                                                                                        new UnlockStatement("x")
                                                                                                                )
                                                                                                        )
                                                                                                ),
                                                                                                new CompoundStatement(
                                                                                                        new LockStatement("x"),
                                                                                                        new CompoundStatement(
                                                                                                                new WriteHeapStatement(
                                                                                                                        "v1",
                                                                                                                        new ArithmeticExpression("*",
                                                                                                                                new ReadHeapExpression(new VariableExpression("v1")),
                                                                                                                                new ConstantExpression(new IntegerValue(10))
                                                                                                                        )
                                                                                                                ),
                                                                                                                new UnlockStatement("x")
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                ),
                                                                                new CompoundStatement(
                                                                                        new NewLockStatement("q"),
                                                                                        new CompoundStatement(
                                                                                                new ForkStatement(
                                                                                                        new CompoundStatement(
                                                                                                                new ForkStatement(
                                                                                                                        new CompoundStatement(
                                                                                                                                new LockStatement("q"),
                                                                                                                                new CompoundStatement(
                                                                                                                                        new WriteHeapStatement(
                                                                                                                                                "v2",
                                                                                                                                                new ArithmeticExpression("+",
                                                                                                                                                        new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                                                                        new ConstantExpression(new IntegerValue(5))
                                                                                                                                                )
                                                                                                                                        ),
                                                                                                                                        new UnlockStatement("q")
                                                                                                                                )
                                                                                                                        )
                                                                                                                ),
                                                                                                                new CompoundStatement(
                                                                                                                        new LockStatement("q"),
                                                                                                                        new CompoundStatement(
                                                                                                                                new WriteHeapStatement(
                                                                                                                                        "v2",
                                                                                                                                        new ArithmeticExpression("*",
                                                                                                                                                new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                                                                new ConstantExpression(new IntegerValue(10))
                                                                                                                                        )
                                                                                                                                ),
                                                                                                                                new UnlockStatement("q")
                                                                                                                        )
                                                                                                                )
                                                                                                        )
                                                                                                ),
                                                                                                new CompoundStatement(
                                                                                                        new SkipStatement(),
                                                                                                        new CompoundStatement(
                                                                                                                new SkipStatement(),
                                                                                                                new CompoundStatement(
                                                                                                                        new SkipStatement(),
                                                                                                                        new CompoundStatement(
                                                                                                                                new SkipStatement(),
                                                                                                                                new CompoundStatement(
                                                                                                                                        new LockStatement("x"),
                                                                                                                                        new CompoundStatement(
                                                                                                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                                                                                new CompoundStatement(
                                                                                                                                                        new UnlockStatement("x"),
                                                                                                                                                        new CompoundStatement(
                                                                                                                                                                new LockStatement("q"),
                                                                                                                                                                new CompoundStatement(
                                                                                                                                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("v2"))),
                                                                                                                                                                        new UnlockStatement("q")
                                                                                                                                                                )
                                                                                                                                                        )
                                                                                                                                                )
                                                                                                                                        )
                                                                                                                                )
                                                                                                                        )
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        ));


        Statement exConditionalAssign =
                new CompoundStatement(
                        new VariableDeclarationStatement("a", new RefType(new IntegerType())),
                        new CompoundStatement(
                                new VariableDeclarationStatement("b", new RefType(new IntegerType())),
                                new CompoundStatement(
                                        new VariableDeclarationStatement("v", new IntegerType()),
                                        new CompoundStatement(
                                                new NewStatement("a", new ConstantExpression(new IntegerValue(0))),
                                                new CompoundStatement(
                                                        new NewStatement("b", new ConstantExpression(new IntegerValue(0))),
                                                        new CompoundStatement(
                                                                new WriteHeapStatement("a", new ConstantExpression(new IntegerValue(1))),
                                                                new CompoundStatement(
                                                                        new WriteHeapStatement("b", new ConstantExpression(new IntegerValue(2))),
                                                                        new CompoundStatement(
                                                                                new ConditionalAssignmentStatement(
                                                                                        "v",
                                                                                        new RelationalExpression(
                                                                                                new ReadHeapExpression(new VariableExpression("a")),
                                                                                                new ReadHeapExpression(new VariableExpression("b")),
                                                                                                "<"
                                                                                        ),
                                                                                        new ConstantExpression(new IntegerValue(100)),
                                                                                        new ConstantExpression(new IntegerValue(200))
                                                                                ),
                                                                                new CompoundStatement(
                                                                                        new PrintStatement(new VariableExpression("v")),
                                                                                        new CompoundStatement(
                                                                                                new ConditionalAssignmentStatement(
                                                                                                        "v",
                                                                                                        new RelationalExpression(
                                                                                                                new ArithmeticExpression(
                                                                                                                        "-",
                                                                                                                        new ReadHeapExpression(new VariableExpression("b")),
                                                                                                                        new ConstantExpression(new IntegerValue(2))
                                                                                                                ),
                                                                                                                new ReadHeapExpression(new VariableExpression("a")),
                                                                                                                ">"
                                                                                                        ),
                                                                                                        new ConstantExpression(new IntegerValue(100)),
                                                                                                        new ConstantExpression(new IntegerValue(200))
                                                                                                ),
                                                                                                new PrintStatement(new VariableExpression("v"))
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );

        examples.add(new ProgramExample(
                "CONDITIONAL VARIABLE: Ref int a, Ref int b, int v; new(a,0), new(b,0); wh(a,1), wh(b,2); v =(rh(a)<rh(b))?100:200; print(v);  v=((rh(b)-2)>rh(a))?100:200; print(v); // expected output: {100,200}",
                exConditionalAssign));

        examples.add(new ProgramExample(
                "Repeat-Until with Fork: int v; int x; int y; v=0; repeat (fork(print(v);v=v-1); v=v+1) until v==3; x=1; y=3; print(v*10)",
                new CompoundStatement(
                        new VariableDeclarationStatement("v", new IntegerType()),
                        new CompoundStatement(
                                new VariableDeclarationStatement("x", new IntegerType()),
                                new CompoundStatement(
                                        new VariableDeclarationStatement("y", new IntegerType()),
                                        new CompoundStatement(
                                                new AssignmentStatement(
                                                        "v",
                                                        new ConstantExpression(new IntegerValue(0))
                                                ),
                                                new CompoundStatement(
                                                        new RepeatUntilStatement(
                                                                // repeat body: (fork(print(v);v=v-1); v=v+1)
                                                                new CompoundStatement(
                                                                        new ForkStatement(
                                                                                new CompoundStatement(
                                                                                        new PrintStatement(new VariableExpression("v")),
                                                                                        new AssignmentStatement(
                                                                                                "v",
                                                                                                new ArithmeticExpression(
                                                                                                        "-",
                                                                                                        new VariableExpression("v"),
                                                                                                        new ConstantExpression(new IntegerValue(1))
                                                                                                )
                                                                                        )
                                                                                )
                                                                        ),
                                                                        new AssignmentStatement(
                                                                                "v",
                                                                                new ArithmeticExpression(
                                                                                        "+",
                                                                                        new VariableExpression("v"),
                                                                                        new ConstantExpression(new IntegerValue(1))
                                                                                )
                                                                        )
                                                                ),
                                                                // until condition: v == 3
                                                                new RelationalExpression(
                                                                        new VariableExpression("v"),
                                                                        new ConstantExpression(new IntegerValue(3)),
                                                                        "=="
                                                                )
                                                        ),
                                                        new CompoundStatement(
                                                                new AssignmentStatement(
                                                                        "x",
                                                                        new ConstantExpression(new IntegerValue(1))
                                                                ),
                                                                new CompoundStatement(
                                                                        new AssignmentStatement(
                                                                                "y",
                                                                                new ConstantExpression(new IntegerValue(3))
                                                                        ),
                                                                        new PrintStatement(
                                                                                new ArithmeticExpression(
                                                                                        "*",
                                                                                        new VariableExpression("v"),
                                                                                        new ConstantExpression(new IntegerValue(10))
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        ));


        // Ref int v1; Ref int v2; Ref int v3; int cnt;
// new(v1,2); new(v2,3); new(v3,4); newBarrier(cnt, rH(v2));
// fork( await(cnt); wh(v1,rh(v1)*10); print(rh(v1)) );
// fork( await(cnt); wh(v2,rh(v2)*10); wh(v2,rh(v2)*10); print(rh(v2)) );
// await(cnt);
// print(rH(v3))

        Statement exBarrier =
                new CompoundStatement(
                        new VariableDeclarationStatement("v1", new RefType(new IntegerType())),
                        new CompoundStatement(
                                new VariableDeclarationStatement("v2", new RefType(new IntegerType())),
                                new CompoundStatement(
                                        new VariableDeclarationStatement("v3", new RefType(new IntegerType())),
                                        new CompoundStatement(
                                                new VariableDeclarationStatement("cnt", new IntegerType()),
                                                new CompoundStatement(
                                                        new NewStatement("v1", new ConstantExpression(new IntegerValue(2))),
                                                        new CompoundStatement(
                                                                new NewStatement("v2", new ConstantExpression(new IntegerValue(3))),
                                                                new CompoundStatement(
                                                                        new NewStatement("v3", new ConstantExpression(new IntegerValue(4))),
                                                                        new CompoundStatement(
                                                                                new NewBarrierStatement(
                                                                                        "cnt",
                                                                                        new ReadHeapExpression(new VariableExpression("v2"))
                                                                                ),
                                                                                new CompoundStatement(
                                                                                        new ForkStatement(
                                                                                                new CompoundStatement(
                                                                                                        new AwaitStatement("cnt"),
                                                                                                        new CompoundStatement(
                                                                                                                new WriteHeapStatement("v1",
                                                                                                                        new ArithmeticExpression(
                                                                                                                                "*",
                                                                                                                                new ReadHeapExpression(new VariableExpression("v1")),
                                                                                                                                new ConstantExpression(new IntegerValue(10))
                                                                                                                        )
                                                                                                                ),
                                                                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v1")))
                                                                                                        )
                                                                                                )
                                                                                        ),
                                                                                        new CompoundStatement(
                                                                                                new ForkStatement(
                                                                                                        new CompoundStatement(
                                                                                                                new AwaitStatement("cnt"),
                                                                                                                new CompoundStatement(
                                                                                                                        new WriteHeapStatement("v2",
                                                                                                                                new ArithmeticExpression(
                                                                                                                                        "*",
                                                                                                                                        new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                                                        new ConstantExpression(new IntegerValue(10))
                                                                                                                                )
                                                                                                                        ),
                                                                                                                        new CompoundStatement(
                                                                                                                                new WriteHeapStatement("v2",
                                                                                                                                        new ArithmeticExpression(
                                                                                                                                                "*",
                                                                                                                                                new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                                                                new ConstantExpression(new IntegerValue(10))
                                                                                                                                        )
                                                                                                                                ),
                                                                                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v2")))
                                                                                                                        )
                                                                                                                )
                                                                                                        )
                                                                                                ),
                                                                                                new CompoundStatement(
                                                                                                        new AwaitStatement("cnt"),
                                                                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("v3")))
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );

        examples.add(new ProgramExample(
                "BARRIER: //expected output {4,20,300}",
                exBarrier));
        return examples;
    }




}
