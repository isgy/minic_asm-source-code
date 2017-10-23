package ast;
import java.util.List;

public abstract class Expr implements ASTNode {

	// Expr       ::= IntLiteral | StrLiteral | ChrLiteral 
	//| VarExpr | FunCallExpr | BinOp | ArrayAccessExpr
	//| FieldAccessExpr | ValueAtExpr | SizeOfExpr | TypecastExpr
    public Type type; // to be filled in by the type analyser
  //  abstract String toStr();
  //  abstract int eval();
    public abstract <T> T accept(ASTVisitor<T> v);
}








