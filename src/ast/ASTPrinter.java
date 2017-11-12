package ast;

import java.io.PrintWriter;
import java.util.List;

public class ASTPrinter implements ASTVisitor<Void> {

    private PrintWriter writer;

    public ASTPrinter(PrintWriter writer) {
            this.writer = writer;
    }
    @Override
    public Void visitBaseType(BaseType bt) {
        writer.print(bt.toString());
        return null;
    }
    @Override
    public Void visitOp(Op o) {
        writer.print(o.toString());
        return null;
    }
    @Override
    public Void visitPointerType(PointerType pt) {
    	writer.print("PointerType(");
        pt.ptype.accept(this);
        writer.print(")");
        return null;
    }
    @Override
    public Void visitStructType(StructType st) {
    	writer.print("StructType(");
        writer.print(st.ident);
        writer.print(")");
        return null;
    }
    @Override
    public Void visitArrayType(ArrayType at) {
    	writer.print("ArrayType(");
        at.tp.accept(this);
        writer.print(","+at.num_elems);
        writer.print(")");
        return null;
    }
    
    @Override
    public Void visitStructTypeDecl(StructTypeDecl st) {
        writer.println("StructTypeDecl(");
        st.stype.accept(this);
        String delimiter = "";
        for (VarDecl vd : st.vardecls) {
            writer.print(delimiter);
            delimiter = ",";
            vd.accept(this);
        }
        writer.print(")");
        return null;
    }
    
    @Override
    public Void visitIntLiteral(IntLiteral n) {
        writer.print("IntLiteral(");
        writer.print(n.i);
        writer.print(")");
        return null;
    }
    
    @Override
    public Void visitStrLiteral(StrLiteral st) {
        writer.print("StrLiteral(");
        writer.print(st.str);
        writer.print(")");
        return null;
    }
    
    @Override
    public Void visitChrLiteral(ChrLiteral ch) {
        writer.print("ChrLiteral(");
        writer.print(ch.c);
        writer.print(")");
        return null;
    }
    
    @Override
    public Void visitFunCallExpr(FunCallExpr fc) {
    	writer.print("FunCallExpr(");
        writer.print(fc.name);
        String delimiter = ",";
        for (Expr e : fc.args) {
            writer.print(delimiter);
            e.accept(this);
        }
        writer.print(")");
        return null;
    }
    
    @Override
    public Void visitBinOp(BinOp b) {
        writer.print("BinOp(");
        b.lhs.accept(this);
        writer.print(",");
        writer.print(b.op);
        writer.print(",");
        b.rhs.accept(this);
        writer.print(")");
        return null;
    }
    
    @Override
    public Void visitArrayAccessExpr(ArrayAccessExpr a) {
        writer.print("ArrayAccessExpr(");
        a.array.accept(this);
        writer.print(",");
        a.index.accept(this);
        writer.print(")");
        return null;
    }
    
    @Override
    public Void visitFieldAccessExpr(FieldAccessExpr f) {
        writer.print("FieldAccessExpr(");
        f.structure.accept(this);
        writer.print(","+f.fieldname);
        writer.print(")");
        return null;
    }
    
    
    @Override
    public Void visitValueAtExpr(ValueAtExpr v) {
        writer.print("ValueAtExpr(");
        v.valueat.accept(this);
        writer.print(")");
        return null;
    }
    
    @Override
    public Void visitSizeOfExpr(SizeOfExpr s) {
        writer.print("SizeOfExpr(");
        s.tp.accept(this);
        writer.print(")");
        return null;
    }
    
    @Override
    public Void visitTypecastExpr(TypecastExpr t) {
        writer.print("TypecastExpr(");
        t.cast.accept(this);
        writer.print(",");
        t.exp.accept(this);
        writer.print(")");
        return null;
    }
    
    @Override
    public Void visitExprStmt(ExprStmt es) {
        writer.print("ExprStmt(");
        es.exp.accept(this);
        writer.print(")");
        return null;
    }
    
    @Override
    public Void visitWhile(While wh) {
        writer.print("While(");
        wh.exp.accept(this);
        writer.print(",");
        wh.stm.accept(this);
        writer.print(")");
        return null;
    }
    
    @Override
    public Void visitIf(If i) {
        writer.print("If(");
        i.ifexp.accept(this);
        writer.print(",");
        i.stm.accept(this);
        
        if(i.else_stm != null) {
        	writer.print(",");
        	i.else_stm.accept(this); 
        }
        writer.print(")");
        return null;
    }
    
    @Override
    public Void visitAssign(Assign a) {
        writer.print("Assign(");
        a.ex.accept(this);
        writer.print(",");
        a.isexp.accept(this);
        writer.print(")");
        return null;
    }
    
    @Override
    public Void visitBlock(Block b, List<VarDecl> p) {
        b.accept(this);
        return null;
    }
    
    @Override
    public Void visitBlock(Block b) {
    	String delimiter = "";
        writer.print("Block(");
        for (VarDecl v : b.vars) {
        	writer.print(delimiter);
        	delimiter = ",";
            v.accept(this);
        }
        for (Stmt s : b.stmts) {
            writer.print(delimiter);
            delimiter = ",";
            s.accept(this);
        }

        writer.print(")");
        return null;
    }

    @Override
    public Void visitFunDecl(FunDecl fd) {
        writer.print("FunDecl(");
        fd.type.accept(this);
        writer.print(","+fd.name+",");
        for (VarDecl vd : fd.params) {
            vd.accept(this);
            writer.print(",");
        }
        fd.block.accept(this);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitProgram(Program p) {
        writer.print("Program(");
        String delimiter = "";
        for (StructTypeDecl std : p.structTypeDecls) {
            writer.print(delimiter);
            delimiter = ",";
            std.accept(this);
        }
        for (VarDecl vd : p.varDecls) {
            writer.print(delimiter);
            delimiter = ",";
            vd.accept(this);
        }
        for (FunDecl fd : p.funDecls) {
            writer.print(delimiter);
            delimiter = ",";
            fd.accept(this);
        }
        writer.print(")");
	    writer.flush();
        return null;
    }

    @Override
    public Void visitVarDecl(VarDecl vd){
        writer.print("VarDecl(");
        vd.type.accept(this);
        writer.print(","+vd.varName);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitVarExpr(VarExpr v) {
        writer.print("VarExpr(");
        writer.print(v.name);
        writer.print(")");
        return null;
    }

    @Override
    public Void visitReturn(Return r) {
        writer.print("Return(");
        if(r.ret != null) {
           r.ret.accept(this);       	   
        }
        writer.print(")");
        return null;
    }
	@Override
	public Void visitProcType(ProcType procType) {
		// TODO Auto-generated method stub
		return null;
	}


    // to complete ...
    
}
