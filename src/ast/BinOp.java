package ast;

public class BinOp extends Expr{
	public Op op;
	public Expr lhs;
	public Expr rhs;
    public int eval() {
    	switch(op) {
    	case ADD:
			return lhs.eval() + rhs.eval();
		case MUL:
			return lhs.eval() * rhs.eval();
		case SUB:
			return lhs.eval() - rhs.eval();
		case DIV:
			return lhs.eval() / rhs.eval();
		case MOD:
			return lhs.eval() % rhs.eval();
		case GT:
			if(lhs.eval() > rhs.eval()){return 1;}else {return -1;}
		case LT:
			if(lhs.eval() < rhs.eval()){return 1;}else {return -1;}
		case GE:
			if(lhs.eval() >= rhs.eval()){return 1;}else {return -1;}
		case LE:
			if(lhs.eval() <= rhs.eval()){return 1;}else {return -1;}
		case NE:
			if(lhs.eval() != rhs.eval()){return 1;}else {return -1;}
		case EQ:
			if(lhs.eval() == rhs.eval()){return 1;}else {return -1;}
		case OR:
			if(lhs.eval() == 1 || rhs.eval() == 1){return 1;}else {return -1;}
		case AND:
			if(lhs.eval() == 1 && rhs.eval() == 1){return 1;}else {return -1;}
		}
    	return -1;
   
    }
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
