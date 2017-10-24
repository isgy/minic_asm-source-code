package ast;

public class Assign extends Stmt {
 	public Expr ex;
 	public Expr isexp;
  	public Assign(Expr e,Expr t){ ex = e; isexp = t; }
    public <T> T accept(ASTVisitor<T> v) {
         return v.visitAssign(this);
     }
}
