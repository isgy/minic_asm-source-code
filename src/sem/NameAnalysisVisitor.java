package sem;

import java.util.LinkedList;

import ast.*;

public class NameAnalysisVisitor extends BaseSemanticVisitor<Void> {
Scope scope;
NameAnalysisVisitor(Scope scope) {this.scope = scope;}
NameAnalysisVisitor(){this.scope = new Scope();}
    

	@Override
	public Void visitArrayAccessExpr(ArrayAccessExpr i) {
		return null;
	}
	@Override
	public Void visitArrayType(ArrayType i) {
		return null;
	}
	@Override
	public Void visitAssign(Assign i) {
		return null;
	}
	@Override
	public Void visitBaseType(BaseType bt) {
		return null;
	}
	@Override
	public Void visitBinOp(BinOp i) {
		return null;
	}
	@Override
	public Void visitChrLiteral(ChrLiteral i) {
		return null;
	}
	@Override
	public Void visitExprStmt(ExprStmt i) {
		return null;
	}
	@Override
	public Void visitFieldAccessExpr(FieldAccessExpr i) {
		return null;
	}
	@Override
	public Void visitIf(If i) {
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
		return null;
	}
	@Override
	public Void visitReturn(Return i) {
		return null;
	}
	@Override
	public Void visitSizeOfExpr(SizeOfExpr i) {
		return null;
	}
	@Override
	public Void visitStrLiteral(StrLiteral i) {
		return null;
	}

	@Override
	public Void visitStructType(StructType i) {
		return null;
	}
	@Override
	public Void visitStructTypeDecl(StructTypeDecl i) {
		return null;
	}
	@Override
	public Void visitTypecastExpr(TypecastExpr i) {
		return null;
	}
	@Override
	public Void visitValueAtExpr(ValueAtExpr i) {
		return null;
	}

	@Override
	public Void visitWhile(While i) {
		return null;
	}


	@Override
	public Void visitBlock(Block b) {
		Scope oldscope = scope;
		scope = new Scope(oldscope);
		for(VarDecl v : b.vars) {
			visitVarDecl(v);
		}
		for(Stmt s : b.stmts) {
			
		}
		scope = oldscope;
		return null;
	}
	
	@Override
	public Void visitProgram(Program p) {
		
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
		
		scope.put(f1s); scope.put(f2s); scope.put(f3s); scope.put(f4s); scope.put(f5s); scope.put(f6s);
		return null;
	}

	@Override
	public Void visitVarDecl(VarDecl vd) {
		Symbol s = scope.lookupCurrent(vd.varName);
		if (s!= null) {
			error("variable is already defined");
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
			error("not declared");
		else if(!vs.isVar()) 
			error("declared but not as a var");
		else 
			v.vd = ((VarSymbol) vs).vd;
		return null;
	}
	@Override
	public Void visitFunDecl(FunDecl p) {
		Symbol f = scope.lookupCurrent(p.name);
		if (f!= null) {
			error("function is already defined");
		}
		else {
			scope.put(new ProcSymbol(p));
			for(VarDecl i : p.params) {
				scope.put(new VarSymbol(i));
			}
		}
		return null;
	}

	@Override
	public Void visitFunCallExpr(FunCallExpr f) {
		Symbol fc = scope.lookup(f.name);
		if(fc == null)
			error("not declared");
		else if(!fc.isProc()) 
			error("declared but not a function");
		else 
			f.fd = ((ProcSymbol) fc).fd;
		return null;
	}
	
	
	// To be completed...


}
