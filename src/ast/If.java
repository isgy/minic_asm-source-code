package ast;

public class If extends Stmt {
 	public Expr ifexp;
 	public Stmt stm;
 	public Stmt else_stm;
 	public If(Expr ex,Stmt st){ ifexp = ex; stm = st; else_stm = null;}
  	public If(Expr ex,Stmt st,Stmt el){ ifexp = ex; stm = st; else_stm = el; }
    public <T> T accept(ASTVisitor<T> v) {
         return v.visitIf(this);
     }
}