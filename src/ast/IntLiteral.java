package ast;

public class IntLiteral extends Expr {
	public int i;
//	String toStr() {return ""+i;}
//	int eval() {return i;}
	public IntLiteral(int i){ this.i = i; }
	
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitIntLiteral(this);
    }
}