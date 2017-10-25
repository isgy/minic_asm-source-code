package sem;
import ast.*;
import java.util.List;

import java.util.LinkedList;

import ast.Assign;

public class EvalVisitor extends BaseSemanticVisitor<Void> {
//	Scope scope;
  //  public EvalVisitor(Scope scope) {this.scope = scope;}
    //public EvalVisitor (){this.scope = new Scope();}
    

	@Override
	public Void visitArrayAccessExpr(ArrayAccessExpr i) {
		//Symbol f = scope.lookupCurrent(i.array);
		return null;
	}
	@Override
	public Void visitArrayType(ArrayType i) {
		return null;
	}
	@Override
	public Void visitAssign(Assign i) {
		//if (i.ex)
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
		Symbol ss = scope.lookup(i.ident);
		if(ss == null)
			error("not declared");
		else if(!ss.isStruc()) 
			error("declared but not as a struct");
		else 
			i.sd = ((StructSymbol) ss).sd;
		return null;
	}
	@Override
	public Void visitStructTypeDecl(StructTypeDecl i) {
		Symbol s = scope.lookupCurrent(i.stype.ident);
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
		}
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
			error("not declared");
		else if(!vs.isVar()) 
			error("not a var");
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
            //params have block scope
			p.block.accept(this,p.params);
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
