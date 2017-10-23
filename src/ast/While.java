package ast;

public class While extends Stmt {
 	Expr exp;
 	Stmt stm;
  	public While(Expr e,Stmt s){ exp = e; stm = s; }
    public <T> T accept(ASTVisitor<T> v) {
         return v.visitWhile(this);
     }
}
