package gen;

import ast.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class CodeGenerator implements ASTVisitor<Register> {

    /*
     * Simple register allocator.
     */
	private LinkedList<StrLiteral> stlist = new LinkedList<StrLiteral>();
	private LinkedList<ChrLiteral> chlist = new LinkedList<ChrLiteral>();
	String indent = "       ";
    // contains all the free temporary registers
    private Stack<Register> freeRegs = new Stack<Register>();
    
    public CodeGenerator() {
        freeRegs.addAll(Register.tmpRegs);
    }

    private class RegisterAllocationError extends Error {}

    private Register getRegister() {
        try {
            return freeRegs.pop();
        } catch (EmptyStackException ese) {
            throw new RegisterAllocationError(); // no more free registers, bad luck!
        }
    }

    private void freeRegister(Register reg) {
        freeRegs.push(reg);
    }

    private boolean firstv = true;

    private boolean beforeData = true;

    private PrintWriter writer; // use this writer to output the assembly instructions


    public void emitProgram(Program program, File outputFile) throws FileNotFoundException {
        writer = new PrintWriter(outputFile);
        visitProgram(program);
        beforeData = false;
        visitProgram(program);
        writer.close();
    }

    @Override
    public Register visitBaseType(BaseType bt) {
        return null;
    }

    @Override
    public Register visitStructTypeDecl(StructTypeDecl st) {
    	if(beforeData) {
    		return null;
    	}
		int sizeof = 0;
        for(VarDecl vd : st.vardecls) {
        	sizeof = sizeof + 4;
        }
        writer.println(st.stype.ident+":");
        for(VarDecl vd: st.vardecls) {
        	if(vd.type.getClass() == ArrayType.class) {
        		
        		ArrayType at = (ArrayType) vd.type;
        		if(at.tp == BaseType.CHAR) {
        			writer.println(indent+".align  2");
        			writer.println(indent+vd.varName+":    .space  "+at.num_elems);
        		}
        		else
        			writer.println(indent+".align  2");
        			writer.println(indent+vd.varName+":     .word   0:"+at.num_elems);
        	}
        	else if(vd.type == BaseType.CHAR) {
        		writer.println(indent+vd.varName+":    .space  1");
            }
        	else if(vd.type == BaseType.INT) {
        		writer.println(indent+".align  2");
        		writer.println(indent+vd.varName+":    .word  1");
        	}
        	else if(vd.type.getClass() == PointerType.class) {
        		PointerType pt = (PointerType) vd.type;
        		writer.println(indent+".align  2");
        		writer.println(indent+vd.varName+":     .word 1");
        	}    	
        	writer.println();
        }
        return null;
    }

    @Override
    public Register visitBlock(Block b) {
    	if(beforeData) {
    		return null;
    	}
        // TODO: to complete
        return null;
    }

    @Override
    public Register visitFunDecl(FunDecl p) {
    	if(beforeData) {
    		return null;
    	}
    	//Register result = getRegister();
    	writer.println(p.name+":");
    	if(p.name == "main") {
            //for (VarDecl vd : p.params) {
              //  vd.accept(this);
            //}
            p.block.accept(this);
            writer.println(indent+"li   $v0, 10");        
            writer.println(indent+"syscall");	
            return null;
    	}
        


        else {
        int psize = p.psize;
        int lsize = p.lsize;
        int plsize = psize + lsize;
        writer.println(indent+"la $sp, -8($sp)");
        writer.println(indent+"sw $fp, 4($sp)"); 
        writer.println(indent+"sw $ra, 0($sp)");
        writer.println(indent+"la $fp, 0($sp)"); 
        writer.println(indent+"la $sp, −"+plsize+"($sp)"); 
        

        p.block.accept(this, p.params, p);
        
        
        writer.println(indent+"la $sp, 0($fp)"); 
        writer.println(indent+"lw $fp, 4($sp)");
        writer.println(indent+"lw $ra, 0($sp)");
        writer.println(indent+"la $sp, 8($sp)");
        writer.println("j $ra");
        }
        return null;
    }

    @Override
    public Register visitProgram(Program p) {
    	if(beforeData) {
        	for (StructTypeDecl std : p.structTypeDecls) {
                std.accept(this);
            }
            for (VarDecl vd : p.varDecls) {
                vd.accept(this);
            }
            
            firstv = false;
          
            for (FunDecl fd : p.funDecls) {
                fd.accept(this);
            }
    		return null;
    	}
    	writer.println(indent+".data");
    	for (StrLiteral s : stlist) {
    		writer.println(indent+".align 2");
    		writer.println(s.label+":    .asciiz "+s.str);
    	}
    	for (StructTypeDecl std : p.structTypeDecls) {
            std.accept(this);
        }
        for (VarDecl vd : p.varDecls) {
            vd.accept(this);
        }
        firstv = false;
        writer.println(indent+".align 2");
        writer.println(indent+".text");
        for (FunDecl fd : p.funDecls) {
            fd.accept(this);
        }
        writer.println("############################################################### Subroutines");
        
        writer.println("read_c:");
        writer.println(indent+"li   $v0, 12");
    	writer.println(indent+"syscall");
    	writer.println(indent+"j $ra");
        writer.println();
        writer.println(indent+"print_i:");
        writer.println(indent+"li   $v0, 1");
        writer.println(indent+"lw $a0, 0($sp)");
        writer.println(indent+"syscall");
        writer.println(indent+"j $ra");
        writer.println();;
        writer.println("print_s:");
        writer.println(indent+"li   $v0, 4");
        writer.println(indent+"lw $a0, 0($sp)");
        writer.println(indent+"syscall");
        writer.println(indent+"j $ra");
        writer.println();
        writer.println("print_c:");
        writer.println(indent+"li   $v0, 11");
        writer.println(indent+"lw $a0, 0($sp)");
        writer.println(indent+"syscall");
        writer.println(indent+"j $ra");
        writer.println();
        writer.println("read_i:");
        writer.println(indent+"li   $v0, 5");
        writer.println(indent+"syscall");
        writer.println(indent+"j $ra");
        writer.println();
        writer.println("read_c");
        writer.println("li   $v0, 12");
        writer.println(indent+"syscall");
        writer.println(indent+"j $ra");

		return null;
    }

    @Override
    public Register visitVarDecl(VarDecl vd) {
    	if(beforeData) {
    		return null;
    	}
        Register addrReg = getRegister();
        Register result = getRegister();
        if(firstv) {                           //global declarations                    
            	if(vd.type.getClass() == ArrayType.class) {
            		
            		ArrayType at = (ArrayType) vd.type;
            		if(at.tp == BaseType.CHAR) {
            			writer.println(indent+vd.varName+":    .space  "+at.num_elems);
            			writer.println(indent+".align  2");
            		}
            		else
            			writer.println(indent+vd.varName+":     .word   0:"+at.num_elems);
            	}
            	else if(vd.type == BaseType.CHAR) {
            		writer.println(indent+vd.varName+":    .space  1");
            		writer.println(indent+".align  2");
                }
            	else if(vd.type == BaseType.INT) {
            		writer.println(indent+vd.varName+":    .word  1");
            	}
            	else if(vd.type.getClass() == PointerType.class) {
            		//PointerType pt = (PointerType) vd.type;
            		writer.println(indent+vd.varName+":     .word 1");
            	}    	
            	writer.println();
            	return null;
        }
        else{ 
        //addiu $sp, $sp, -4     
       // sw $zero, ($sp)     
        writer.println("la addrReg "+vd.varName);
        writer.println("la result ");
        return null;
        }
    }

    @Override
    public Register visitVarExpr(VarExpr v) {
    	if(beforeData) {
    		return null;
    	}
    	
        if(v.vd.isLocal) {
          
        }else{
          if(v.type == BaseType.INT) {
              Register res = getRegister();
        	  writer.println("lw   "+res+", "+v.name);
        	  return res;
          }else if(v.type == BaseType.CHAR) {
        	  Register res = getRegister();
        	  writer.println("lb   "+res+", "+v.name);
        	  return res;
          }
  	
        }
        return null;
    }

	@Override
	public Register visitPointerType(PointerType p) {
    	if(beforeData) {
    		return null;
    	}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitStructType(StructType s) {
    	if(beforeData) {
    		return null;
    	}
		// TODO Auto-generated method stub
        return null;
	}

	@Override
	public Register visitArrayType(ArrayType p) {
    	if(beforeData) {
    		p.tp.accept(this);
    		return null;
    	}

		return null;
	}

	@Override
	public Register visitIntLiteral(IntLiteral e) {
    	if(beforeData) {
    		return null;
    	}
		Register result = getRegister();
		writer.println("li   "+result+", "+e.i);
		return result;
	}

	@Override
	public Register visitStrLiteral(StrLiteral e) {
    	if(beforeData) {
    		stlist.add(e);
    		return null;
    	}
		Register result = getRegister();
		Register addr = getRegister();
		writer.println("la "+addr+", "+e.label);
        writer.println("lb "+result+", 0("+addr+")");      
        freeRegister(addr);
		return result;
	}

	@Override
	public Register visitChrLiteral(ChrLiteral e) {
    	if(beforeData) {
    		chlist.add(e);
    		return null;
    	}
		// TODO Auto-generated method stub
		Register result = getRegister();
		Register addr = getRegister();
		writer.println(indent+"la "+addr+", "+e.clabel);
        writer.println(indent+"lb "+result+", "+addr);      
        freeRegister(addr);
		return result;
	}

	@Override
	public Register visitFunCallExpr(FunCallExpr e) {
    	if(beforeData) {
    		return null;
    	}
    	
        for(Expr v : e.args) {
        	Register tmp = getRegister();
        	Register arg = v.accept(this);   //returns start address if arg is an array
        	writer.println(indent+"la $sp, -4($sp)");  
        	writer.println(indent+"sw "+tmp+", 0($sp)");
        }
        writer.println("jal "+e.name);
        writer.println("la $sp, "+e.fd.psize+"($sp)");
        return null;
	}

	@Override
	public Register visitBinOp(BinOp e) {
    	if(beforeData) {
            e.lhs.accept(this);
            e.rhs.accept(this);
    		return null;
    	}
		Register lhsReg = e.lhs.accept(this);
		Register rhsReg = e.rhs.accept(this);
		Register result = getRegister();
		switch(e.op) {
		case ADD:
			writer.println(indent+"add "+result+", "+lhsReg+", "+rhsReg);
			break;
		case MUL:
			writer.println(indent+"mul "+result+", "+lhsReg+", "+rhsReg);
			break;
		case SUB:
			 //la  $t0, tmpval
			 // lw  $t1, 0($t0)  # $t1 == tmpval == 5
			writer.println(indent+"sub "+result+", "+lhsReg+", "+rhsReg);
			break;
		case DIV:
			writer.println(indent+"div "+lhsReg+", "+rhsReg);
			writer.println(indent+"mflo "+result);
			break;
		case MOD:
			writer.println(indent+"div "+lhsReg+", "+rhsReg);
			writer.println(indent+"mfhi "+result);
			break;
		case GT:
			writer.println(indent+"bgt "+lhsReg+", "+rhsReg+", gt");
			writer.println(indent+"li "+result+", 0");
			writer.println(indent+"j nextexp");
			writer.println("gt:");
			writer.println(indent+"li "+result+", 1");
			writer.println("nextexp:");
			break;
		case LT:
			writer.println(indent+"blt "+lhsReg+", "+rhsReg+", lt");
			writer.println(indent+"li "+result+", 0");
			writer.println(indent+"j nextexp");
			writer.println("lt:");
			writer.println(indent+"li "+result+", 1");
			writer.println("nextexp:");
			break;
		case GE:
		    writer.println(indent+"bge "+lhsReg+", "+rhsReg+", ge");
			writer.println(indent+"li "+result+", 0");
			writer.println(indent+"j nextexp");
			writer.println("ge:");
			writer.println(indent+"li "+result+", 1");
			writer.println("nextexp:");
		    break;
		case LE:
			writer.println(indent+"ble "+lhsReg+", "+rhsReg+", le");
			writer.println(indent+"li "+result+", 0");
			writer.println(indent+"j nextexp");
			writer.println("le:");
			writer.println(indent+"li "+result+", 1");
			writer.println("nextexp:");
			break;
		case NE:
			writer.println(indent+"bne "+lhsReg+", "+rhsReg+", ne");
			writer.println(indent+"li "+result+", 0");
			writer.println(indent+"j nextexp");
			writer.println("ne:");
			writer.println(indent+"li "+result+", 1");
			writer.println("nextexp:");
			break;
		case EQ:
			writer.println(indent+"beq "+lhsReg+", "+rhsReg+", eq");
			writer.println(indent+"li "+result+", 0");
			writer.println(indent+"j nextexp");
			writer.println("eq:");
			writer.println(indent+"li "+result+", 1");
			writer.println("nextexp:");
			break;
		case OR:
			writer.println(indent+"addi "+result+", $zero, $zero");
			writer.println(indent+"bnez "+lhsReg+", or");
			writer.println(indent+"bnez "+rhsReg+", or");
			writer.println(indent+"j endl");
			writer.println("or:");
			writer.println(indent+"addi "+result+", $zero, 1");
			writer.println("endl:");
		case AND:
			writer.println(indent+"addi "+result+", $zero, $zero");
			writer.println(indent+"beqz "+lhsReg+", endl");
			writer.println(indent+"beqz "+rhsReg+", endl");
			writer.println(indent+"addi "+result+", $zero, 1");
			writer.println("endl:");
			
		}
		freeRegister(lhsReg);
		freeRegister(rhsReg);
		return result;
	}

	@Override
	public Register visitArrayAccessExpr(ArrayAccessExpr e) {
		VarExpr va = (VarExpr) e.array;
    	if(beforeData) {
            e.array.accept(this);        
            e.index.accept(this);
    		return null;
    	}
    	if(e.isLocal) {
    		Register base = getRegister();
    		Register ind = getRegister();
    		Register result = getRegister();
            if(e.type == BaseType.CHAR) {
    	    writer.println(indent+"li "+ind+", "+e.index.eval());
    		writer.println(indent+"la "+base+", "+va.vd.offset+"($fp)"); 
    		writer.println(indent+"add "+base+", "+base+", "+ind);
    		writer.println(indent+"lb "+result+", ("+base+")");
    	    }else {
        	    writer.println(indent+"li "+ind+", "+e.index.eval());
        		writer.println(indent+"la "+base+", "+va.vd.offset+"($fp)"); 
        		writer.println(indent+"sll "+ind+", "+ind+", 2");
        		writer.println(indent+"add "+base+", "+base+", "+ind);
        		writer.println(indent+"lw "+result+", ("+base+")");
    	    }
    	}else {
    		Register base = getRegister();
    		Register ind = getRegister();
    		Register result = getRegister();
    		
            if(e.type == BaseType.CHAR) {
    	    writer.println(indent+"li "+ind+", "+e.index.eval());
    		writer.println(indent+"la "+base+", "+va.name); 
    		writer.println(indent+"add "+base+", "+base+", "+ind);
    		writer.println(indent+"lb "+result+", ("+base+")");
    	    }else {
        	    writer.println(indent+"li "+ind+", "+e.index.eval());
        		writer.println(indent+"la "+base+", "+va.name); 
        		writer.println(indent+"sll "+ind+", "+ind+", 2");
        		writer.println(indent+"add "+base+", "+base+", "+ind);
        		writer.println(indent+"lw "+result+", ("+base+")");
    	    }
    	}
    	 	
		return null;
	}

	@Override
	public Register visitFieldAccessExpr(FieldAccessExpr e) {
    	if(beforeData) {
    		e.structure.accept(this);
    		return null;
    	}
		/*Register result = getRegister();
		Register addr = getRegister();
		writer.println("la "+addr+", "+e.);
        writer.println("lb "+result+", 0("+addr+")");      
        freeRegister(addr);
		return result;*/
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitValueAtExpr(ValueAtExpr e) {
    	if(beforeData) {
    		return null;
    	}
    	
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitSizeOfExpr(SizeOfExpr e) {
	
    	if(beforeData) {
    		return null;
    	}
    	Register result = getRegister();
    	if(e.tp == BaseType.CHAR) {
    		writer.println("li   "+result+", "+1);
    	}else {
    		writer.println("li   "+result+", "+4);
    	}
    	
		return result;
	}

	@Override
	public Register visitTypecastExpr(TypecastExpr e) {
    	if(beforeData) {
    		return null;
    	}
		//         writer.print("TypecastExpr(");
      /*  t.cast.accept(this);
        writer.print(",");
        t.exp.accept(this);
        writer.print(")"); */
        
		return null;
	}

	@Override
	public Register visitOp(Op o) {
    	if(beforeData) {
    		return null;
    	}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Register visitExprStmt(ExprStmt e) {
    	if(beforeData) {
    		e.exp.accept(this);
    		return null;
    	}
        e.exp.accept(this);
		return null;
	}

	@Override
	public Register visitWhile(While w) {
    	if(beforeData) {
    		return null;
    	}
    	Register res = w.exp.accept(this);
     /*   wh.exp.accept(this);
        writer.print(",");
        wh.stm.accept(this);
        writer.print(")"); */
        return null;
	}

	@Override
	public Register visitIf(If i) {
    	if(beforeData) {
   	     i.ifexp.accept(this);
   	     i.stm.accept(this);
   	     if(i.else_stm != null) { 
   	      	i.else_stm.accept(this); 
   	     }
    		return null;
    	}
        Register res = i.ifexp.accept(this);
        Register result;
        if(i.else_stm != null) {
        writer.print(indent+"blez "+res+" Else"); 
        i.stm.accept(this);
        writer.print(indent+"j Endif");
        writer.print("Else: ");
        
        i.else_stm.accept(this);
        writer.print("Endif: ");
        }else {
        	 writer.print(indent+"blez "+res+" Endif"); 	
        	 i.stm.accept(this);
        }

		return null;
	}

	@Override
	public Register visitAssign(Assign a) {
    	if(beforeData) {
            a.ex.accept(this);
            a.isexp.accept(this);
    		return null;
    	}
    	Register to = a.ex.accept(this);
    	Register from = a.isexp.accept(this);
    	writer.println(indent+"la $sp, -4($sp)");   
    	writer.println(indent+"sw $zero, 0($sp)");    	
    	writer.println(indent+"addi "+to+", "+from+", $zero");     
    	writer.println(indent+"sw "+to+", 0($sp)");

		return to;
	}

	@Override
	public Register visitBlock(Block b, List<VarDecl> p, FunDecl f) {
    	if(beforeData) {
    		return null;
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
	public Register visitReturn(Return r) {
    	if(beforeData) {
    		return null;
    	}

        if(r.ret != null) {
        	r.ret.accept(this);
            writer.println(indent+"la $sp, 0($fp)"); 
            writer.println(indent+"lw $fp, 4($sp)");
            writer.println(indent+"lw $ra, 0($sp)");
            writer.println(indent+"la $sp, 8($sp)");
            writer.println("j $ra");
        }else {
            writer.println(indent+"la $sp, 0($fp)"); 
            writer.println(indent+"lw $fp, 4($sp)");
            writer.println(indent+"lw $ra, 0($sp)");
            writer.println(indent+"la $sp, 8($sp)");
            writer.println("j $ra");
        }
		return null;
	}

	@Override
	public Register visitProcType(ProcType procType) {
		// TODO Auto-generated method stub
		return null;
	}
}
