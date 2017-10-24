package ast;

public class BinOp extends Expr{
	public Op op;
	public Expr lhs;
	public Expr rhs;
	public BinOp (Op op , Expr lhs, Expr rhs) {
		this.op = op;
		this.lhs = lhs;
		this.rhs = rhs;
	}
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitBinOp(this);
    }
//	String toStr() {return lhs.toStr() + op.name() + rhs.toStr();}
/*	int eval() {
		switch(op) {
		  case ADD: return lhs.eval() + rhs.eval();
		  case SUB: return lhs.eval() - rhs.eval();
		  case MUL: return lhs.eval() * rhs.eval();
		  case DIV: return lhs.eval() / rhs.eval();
		  case MOD: return lhs.eval() % rhs.eval();
		  
		}
	}
	*/
}
