package ast;

public class Assign extends Stmt {
 	Expr ex;
 	Expr isexp;
  	public Assign(Expr e,Expr t){ ex = e; isexp = t; }
    public <T> T accept(ASTVisitor<T> v) {
         return v.visitAssign(this);
     }
}
