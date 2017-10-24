package ast;

public class ExprStmt extends Stmt {
 	public Expr exp;
  	public ExprStmt(Expr e){ exp = e; }
    public <T> T accept(ASTVisitor<T> v) {
         return v.visitExprStmt(this);
     }
}
