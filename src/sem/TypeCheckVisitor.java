package sem;
import ast.*;

import java.util.List;

import ast.*;

public class TypeCheckVisitor extends BaseSemanticVisitor<Type>{
	Scope scope;
    public TypeCheckVisitor(Scope scope) {this.scope = scope;}
    public TypeCheckVisitor(){this.scope = new Scope();}
	@Override
	public Type visitArrayAccessExpr(ArrayAccessExpr i) {
       // if(i.array.type )
		return null;
	}
	@Override
	public Type visitArrayType(ArrayType i) {
		//if(i.tp.accept(this) )
        return null;
	}
	@Override
	public Type visitAssign(Assign i) {
		if(i.ex.accept(this) == BaseType.VOID) {
			error("void assignment");
		}
		if(i.ex instanceof Type) {
			//if(i.ex.type != ArrayType && i.ex.accept(this) == i.isexp.accept(this)) {
		}
			
		
        return null;
	}
	@Override
	public Type visitBaseType(BaseType bt) {
		return bt;
	}
	@Override
	public Type visitBinOp(BinOp i) {
        Type lhsT = i.lhs.accept(this);
        Type rhsT = i.rhs.accept(this);
        if(i.op == Op.ADD || i.op == Op.SUB || i.op == Op.MUL || i.op == Op.DIV || i.op == Op.MOD || i.op == Op.GT || i.op == Op.LT || i.op == Op.GE || i.op == Op.LE) {
        	if(lhsT == BaseType.INT && rhsT == BaseType.INT) {
        		i.type = BaseType.INT;
        		return BaseType.INT;
        	}
        	else error("binOp");
        }
        else {   //Op is NE or EQ
        	if(lhsT != BaseType.VOID && lhsT == rhsT) {
        		i.type = BaseType.INT;
        		return BaseType.INT;
        	}
        	else error("binOpEq");
        }
        return null;
	}
	@Override
	public Type visitChrLiteral(ChrLiteral i) {
        i.type = BaseType.CHAR;
		return BaseType.CHAR;
	}
	@Override
	public Type visitExprStmt(ExprStmt i) {
		return i.accept(this);
	}
	@Override
	public Type visitFieldAccessExpr(FieldAccessExpr i) {
		StructType s = null;
		try {
			s = (StructType) i.structure.accept(this);
		}
	    catch (Exception e) {
				error("fieldaccess");
			}
	    boolean c = false;
		if(s != null) {
			for(VarDecl v : s.sd.vardecls) {
				if(v.varName == i.fieldname) {
					i.type = v.type;
					c = true;
				}
			}
			if(c) 
				return null;
			else 
				error("no field in struct");
		}   
		return null;
	}
	@Override
	public Type visitIf(If i) {
	     i.ifexp.accept(this);
	     i.stm.accept(this);
	     if(i.else_stm != null) { 
	      	i.else_stm.accept(this); 
	     }
		return null;
	}
	@Override
	public Type visitIntLiteral(IntLiteral i) {
	    i.type = BaseType.INT;
		return BaseType.INT;
		
	}
	@Override
	public Type visitOp(Op i) {
		return null;
	}
	@Override
	public Type visitPointerType(PointerType i) {
		i.ptype.accept(this);
		return null;
	}
	@Override
	public Type visitReturn(Return i) {
		if(i.ret != null) {
		i.ret.accept(this);
		}
		return null;
	}
	@Override
	public Type visitSizeOfExpr(SizeOfExpr i) {
		i.type = BaseType.INT;
		return BaseType.INT;
	}
	@Override
	public Type visitStrLiteral(StrLiteral i) {
		char[] chars = i.str.toCharArray();
		int n = i.str.length() + 1;
        char[] ch = new char[n];
		for(int id = 0; id < i.str.length(); id++) {
			ch[id] = chars[id];
		}
		ch[ch.length - 1] = '\0';
//		i.type = ch;
        return null;
	}

	@Override
	public Type visitStructType(StructType i) {
		return i;
	}
	@Override
	public Type visitStructTypeDecl(StructTypeDecl i) {
		return null;
	}
	@Override
	public Type visitTypecastExpr(TypecastExpr i) {
		return null;
	}
	@Override
	public Type visitValueAtExpr(ValueAtExpr i) {
		return null;
	}

	@Override
	public Type visitWhile(While i) {
		return null;
	}

	@Override
	public Type visitBlock(Block b) {

		for(VarDecl v : b.vars) {
			v.accept(this);
		}
		for(Stmt s : b.stmts) {
			s.accept(this);
		}
        return null;
	}
	@Override
	public Type visitBlock(Block b, List<VarDecl> p) {
		for(VarDecl ps : p) {
			ps.accept(this);
		}
		for(VarDecl v : b.vars) {
			v.accept(this);
		}
		for(Stmt s : b.stmts) {
			s.accept(this);
		}
		return null;
	}
	
	@Override
	public Type visitProgram(Program p) {
		//scope = new Scope();
		//System.out.println("here");
        for (StructTypeDecl std : p.structTypeDecls) {
            std.accept(this);
        }
        for (VarDecl vd : p.varDecls) {
            vd.accept(this);
        }
        for (FunDecl fd : p.funDecls) {
            fd.accept(this);
        }
		return null;
	}

	@Override
	public Type visitVarDecl(VarDecl vd) {
        if(vd.type == BaseType.VOID) {
        	error("typerror void vardecl");      
        }
        return null;
	}

	@Override
	public Type visitVarExpr(VarExpr v) {
        v.type = v.vd.type;
		return v.vd.type;
	}
	@Override
	public Type visitFunDecl(FunDecl p) {
//already added decl in scope
        return null;
	}

	@Override
	public Type visitFunCallExpr(FunCallExpr f) {
		f.type = f.fd.type;
		return f.fd.type;
	}

}
