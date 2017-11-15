package sem;
import ast.*;

import java.util.List;


public class TypeCheckVisitor extends BaseSemanticVisitor<Type>{
//	Scope scope;
//    public TypeCheckVisitor(Scope scope) {this.scope = scope;}
//    public TypeCheckVisitor(){this.scope = new Scope();}

	@Override
	public Type visitArrayAccessExpr(ArrayAccessExpr i) {
		if(i.array.accept(this).getClass() != ArrayType.class) {
			if(i.array.accept(this).getClass() == PointerType.class) {
				PointerType p = (PointerType) i.array.accept(this);
				if(i.index.accept(this) == BaseType.INT) {
					i.type = p.ptype;
					i.isArrayAcc= true;
					return p.ptype;
				}
			}
			else 
				error("arrayaccess not an array or pointer");
		}else {
			ArrayType a = (ArrayType) i.array.accept(this);
			if(i.index.accept(this) == BaseType.INT) {
				i.type = a.tp;
				i.isArrayAcc = true;
				return a.tp;
			}
		}
		return null;
	}
	@Override
	public Type visitArrayType(ArrayType i) {
		return i;
	}
	@Override
	public Type visitAssign(Assign i) {
		
		Type itype = i.ex.accept(this);
		if(itype == BaseType.VOID) {
			error("void assignment");
		}else if(!(i.ex.isValatExp || i.ex.isArrayAcc || i.ex.isFieldAcc || i.ex.isVarExp) ){
			error("cannot assign lhs");
		}else {
			if(i.isexp.accept(this) == itype) {
				return null;
			}
			else {
				error("assign types not equal");
			}
		}
		if(itype != null) {
		System.out.println(itype.toString());}
		return null;
	}
	@Override
	public Type visitBaseType(BaseType bt) {
		return bt;
	}
	@Override
	public Type visitBinOp(BinOp i) {
		
        Type lhsT = i.lhs.accept(this);
        System.out.println(lhsT.toString());
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
		return i.type;
	}
	@Override
	public Type visitExprStmt(ExprStmt i) {
		return i.exp.accept(this);
	}
	@Override
	public Type visitFieldAccessExpr(FieldAccessExpr i) {
		if(i.structure.accept(this).getClass() != StructType.class) {
			error("not a struct");
		}else {
			StructType t = (StructType) i.structure.accept(this);
			boolean c = false;
			for(VarDecl v : t.sd.vardecls) {
				if(v.varName == i.fieldname) {
					i.type = v.type;
					c = true;
					i.isFieldAcc = true;
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
		 Type ex = i.ifexp.accept(this);
	     if(ex != BaseType.INT) {
	    	 error("ifexp not an int");
	     }
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
        return i;
	}
	@Override
	public Type visitReturn(Return i) {
		if(i.ret != null) {
		   Type rt = i.ret.accept(this);
		   Type ft = i.rtype.accept(this);
		   if(rt != ft) {
			   error("return type does not match");
		   }
		/*   if(i.fun.accept(this) != rt) {
			   error("return type does not match");
		   } */
		}else if(i.rtype.accept(this) != BaseType.VOID) {
	        	error("not void");
	        }   //if null return, check function type is void
		
		return null;
	}
	@Override
	public Type visitSizeOfExpr(SizeOfExpr i) {
		i.tp.accept(this);
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
		i.chararray = ch;   
		i.type = new ArrayType(BaseType.CHAR,i.str.length() + 1);
        return i.type;
	}

	@Override
	public Type visitStructType(StructType i) {
		//if(i.sd.stype.ident == i.ident) {
		//return i.sd.stype;
		return i;
	//	}
	}
	@Override
	public Type visitStructTypeDecl(StructTypeDecl i) {
	        if(i.stype.accept(this).getClass() != StructType.class) {
	        	error("not a struct");
	        }
	        for (VarDecl vd : i.vardecls) {
	            vd.accept(this);
	        }
	        return null;
	}
	@Override
	public Type visitTypecastExpr(TypecastExpr i) {
		if(i.exp.accept(this) == BaseType.CHAR && i.cast == BaseType.INT) {
			i.type = BaseType.INT;
		if(i.exp.accept(this) == BaseType.INT && i.cast == BaseType.CHAR) {
				i.type = BaseType.INT;
		}else if(i.exp.accept(this).getClass() == ArrayType.class && i.cast.getClass() == PointerType.class) {
			i.type = new PointerType(i.cast);
		}else if(i.exp.accept(this).getClass() == PointerType.class && i.cast.getClass() == PointerType.class) {
			i.type = new PointerType(i.cast);
		}else
			error("cannot cast");
		}
		return i.type;
	}
	@Override
	public Type visitValueAtExpr(ValueAtExpr i) {
		if(i.valueat.accept(this).getClass() != PointerType.class) {
			error("not a pointer");
		}else {
			PointerType t = (PointerType) i.valueat.accept(this);
			i.type = t.ptype;
			i.isValatExp = true;
			return t.ptype;
		}
		return null;
	}

	@Override
	public Type visitWhile(While i) {
		Type ex = i.exp.accept(this);
		if(ex != BaseType.INT) {
			error("while_param_not_an_int");
		}
		i.stm.accept(this);
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
	public Type visitBlock(Block b, List<VarDecl> p, FunDecl f) {
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
        v.isVarExp = true;
		return v.vd.type;
	}
	@Override
	public Type visitFunDecl(FunDecl p) {
		for(VarDecl v : p.params) {
			v.accept(this);
		}
		p.block.accept(this);
        return null;
	}

	@Override
	public Type visitFunCallExpr(FunCallExpr f) {
		
		if(f.fd.params.size() != f.args.size()) {
			error("number of arguments do match params");
		}else {
			boolean c = true;
			for(int i = 0; i < f.args.size(); i++) {    //linkedlist implements deque
			 VarDecl v = f.fd.params.get(i);
			 Expr arg = f.args.get(i);
			 Type argtype = arg.accept(this);
			 if(v.type.getClass() == ArrayType.class && argtype.getClass() == ArrayType.class) {
				 ArrayType vt = (ArrayType) v.type;
				 ArrayType at = (ArrayType) argtype;
				 if(vt.num_elems != at.num_elems) {
					 error("arrays of args and params are not the same length");
					 c = false;
				 }
			 }
			 if(v.type != argtype) {
				 System.out.println(v.type.toString());
				 if(argtype != null) {
				 System.out.println(argtype.toString());
				 }
				 error("type of params do not match args");
				 c = false;
			 }
			}
			if(c) {
			f.type = f.fd.type;  //if this is reached, param types match arguments. set the type for the funcall
			return f.fd.type;
			
			}
		}
		
		return null;
	
	}
	@Override
	public Type visitProcType(ProcType procType) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
