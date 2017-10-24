package ast;

public class While extends Stmt {
 	public Expr exp;
 	public Stmt stm;
  	public While(Expr e,Stmt s){ exp = e; stm = s; }
    public <T> T accept(ASTVisitor<T> v) {
         return v.visitWhile(this);
     }
}
