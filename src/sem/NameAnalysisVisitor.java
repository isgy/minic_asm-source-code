package sem;

import java.util.LinkedList;
import java.util.List;

import ast.*;

public class NameAnalysisVisitor extends BaseSemanticVisitor<Void> {
Scope scope;
    public NameAnalysisVisitor(Scope scope) {this.scope = scope;}
    public NameAnalysisVisitor(){this.scope = new Scope();}
    

	@Override
	public Void visitArrayAccessExpr(ArrayAccessExpr i) {
        i.array.accept(this);        
        i.index.accept(this);
		return null;
	}
	@Override
	public Void visitArrayType(ArrayType i) {
		i.tp.accept(this);
		return null;
	}
	@Override
	public Void visitAssign(Assign i) {
        i.ex.accept(this);
        i.isexp.accept(this);
		return null;
	}
	@Override
	public Void visitBaseType(BaseType bt) {
		return null;
	}
	@Override
	public Void visitBinOp(BinOp i) {
        i.lhs.accept(this);
        i.rhs.accept(this);
		return null;
	}
	@Override
	public Void visitChrLiteral(ChrLiteral i) {
		return null;
	}
	@Override
	public Void visitExprStmt(ExprStmt i) {
		i.exp.accept(this);
		return null;
	}
	@Override
	public Void visitFieldAccessExpr(FieldAccessExpr i) {
		i.structure.accept(this);
		return null;
	}
	@Override
	public Void visitIf(If i) {
	     i.ifexp.accept(this);
	     i.stm.accept(this);
	     if(i.else_stm != null) { 
	      	i.else_stm.accept(this); 
	     }
		return null;
	}
	@Override
	public Void visitIntLiteral(IntLiteral i) {
		return null;
	}
	@Override
	public Void visitOp(Op i) {
		return null;
	}
	@Override
	public Void visitPointerType(PointerType i) {
		i.ptype.accept(this);
		return null;
	}
	@Override
	public Void visitReturn(Return i) {
		if(i.ret != null) {
		i.ret.accept(this);
		}
		return null;
	}
	@Override
	public Void visitSizeOfExpr(SizeOfExpr i) {
		i.tp.accept(this);
		return null;
	}
	@Override
	public Void visitStrLiteral(StrLiteral i) {
		return null;
	}

	@Override
	public Void visitStructType(StructType i) {
	/*	Symbol ss = scope.lookup(i.ident);
		if(ss == null)
			error("not declared");
		else if(!ss.isStruc()) 
			error("declared but not as a struct");
		else 
			i.sd = ((StructSymbol) ss).sd; */
		return null; 
	}
	@Override
	public Void visitStructTypeDecl(StructTypeDecl i) {
		/*Symbol s = scope.lookupCurrent(i.stype.ident);
		if (s!= null) {
			error("struct is already defined");
		}
		else {
			scope.put(new StructSymbol(i));
			Scope oldscope = scope;
			scope = new Scope(oldscope);
			for(VarDecl v : i.vardecls) {
				v.accept(this);
			}
			scope = oldscope; 
		}*/
		Scope oldscope = scope;
		scope = new Scope();
		for(VarDecl v : i.vardecls) {
			v.accept(this);
		}
		scope = oldscope;
		
		return null;
	}
	@Override
	public Void visitTypecastExpr(TypecastExpr i) {
        i.cast.accept(this);
        i.exp.accept(this);
		return null;
	}
	@Override
	public Void visitValueAtExpr(ValueAtExpr i) {
		i.valueat.accept(this);
		return null;
	}

	@Override
	public Void visitWhile(While i) {
        i.exp.accept(this);
        i.stm.accept(this);
		return null;
	}

	@Override
	public Void visitBlock(Block b) {
		Scope oldscope = scope;
		scope = new Scope(oldscope);
		for(VarDecl v : b.vars) {
			v.accept(this);
		}
		for(Stmt s : b.stmts) {
			s.accept(this);
		}
		scope = oldscope;
		return null;
	}
	@Override
	public Void visitBlock(Block b, List<VarDecl> p) {
		Scope oldscope = scope;
		scope = new Scope(oldscope);
		for(VarDecl ps : p) {
			ps.accept(this);
		}
		for(VarDecl v : b.vars) {
			v.accept(this);
		}
		for(Stmt s : b.stmts) {
			s.accept(this);
		}
		scope = oldscope;
		return null;
	}
	
	@Override
	public Void visitProgram(Program p) {
		//scope = new Scope();
		LinkedList<VarDecl> v1 = new LinkedList<VarDecl>();
		LinkedList<VarDecl> v2 = new LinkedList<VarDecl>();
		LinkedList<VarDecl> v3 = new LinkedList<VarDecl>();
		LinkedList<VarDecl> v0 = new LinkedList<VarDecl>();
		LinkedList<VarDecl> v6 = new LinkedList<VarDecl>();

		v1.add(new VarDecl(new PointerType(BaseType.CHAR),"s"));
		FunDecl f1 = new FunDecl(BaseType.VOID,"print_s",v1,new Block());
		Symbol f1s = new ProcSymbol(f1);
		
		v2.add(new VarDecl(BaseType.INT,"i"));
		FunDecl f2 = new FunDecl(BaseType.VOID,"print_i",v2,new Block());
		Symbol f2s = new ProcSymbol(f2);
		
		v3.add(new VarDecl(BaseType.CHAR,"c"));
		FunDecl f3 = new FunDecl(BaseType.VOID,"print_c",v3,new Block());
		Symbol f3s = new ProcSymbol(f3);
		
		FunDecl f4 = new FunDecl(BaseType.CHAR,"read_c",v0,new Block());
		Symbol f4s = new ProcSymbol(f4);
		
		FunDecl f5 = new FunDecl(BaseType.INT,"read_i",v0,new Block());
		Symbol f5s = new ProcSymbol(f5);
		
		v6.add(new VarDecl(BaseType.INT,"size"));
		FunDecl f6 = new FunDecl(new PointerType(BaseType.VOID),"mcmalloc",v6,new Block());
		Symbol f6s = new ProcSymbol(f6);
		
		//System.out.println(scope.getlen());
		scope.put(f1s); scope.put(f2s); scope.put(f3s); scope.put(f4s); scope.put(f5s); scope.put(f6s);   //put builtin functions in the global scope
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
	public Void visitVarDecl(VarDecl vd) {
		if(scope.lookupCurrent(vd.varName) != null) {
			error(String.format("variable %s is already defined",vd.varName));
		}
		else {
			scope.put(new VarSymbol(vd));
		}
		return null;
	}

	@Override
	public Void visitVarExpr(VarExpr v) {
		Symbol vs = scope.lookup(v.name);
		if(vs == null)
			error("is not declared"+v.name);
		else if(!vs.isVar()) 
			error(v.name+"is not a var, is");
		else 
			v.vd = ((VarSymbol) vs).vd;
		return null;
	}
	@Override
	public Void visitFunDecl(FunDecl p) {
		Symbol f = scope.lookupCurrent(p.name);
		if (f!= null) {
			error("function is already defined"+p.name+f.name);
		}
		else {  
			scope.put(new ProcSymbol(p));
            //params have block scope
			p.block.accept(this,p.params);
		}
		return null;
	}

	@Override
	public Void visitFunCallExpr(FunCallExpr f) {
		Symbol fc = scope.lookup(f.name);
		if(fc == null)
			error("is not declared"+f.name);
		else if(!fc.isProc()) 
			error(f.name+"is not a function,");
		else 
			f.fd = ((ProcSymbol) fc).fd;
	    for(Expr a : f.args) {
	    	a.accept(this);
	    }
		return null;
	}
	@Override
	public Void visitProcType(ProcType procType) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	// To be completed...


}
