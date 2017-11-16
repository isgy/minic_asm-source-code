package ast;
import java.util.List;

public abstract class Expr implements ASTNode {

	// Expr       ::= IntLiteral | StrLiteral | ChrLiteral 
	//| VarExpr | FunCallExpr | BinOp | ArrayAccessExpr
	//| FieldAccessExpr | ValueAtExpr | SizeOfExpr | TypecastExpr
    public Type type; // to be filled in by the type analyser
    public boolean isValatExp = false;
    public boolean isVarExp = false;
    public boolean isFieldAcc = false;
    public boolean isArrayAcc = false;
  //  abstract String toStr();
    public abstract int eval();
    public int eval;
    public abstract <T> T accept(ASTVisitor<T> v);
}








