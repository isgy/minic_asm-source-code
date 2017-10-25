package sem;

import ast.FunDecl;

public class ProcSymbol extends Symbol{
    FunDecl fd;
    
    public boolean isVar() {
    	return false;
    }
    public boolean isProc() {
    	return true;
    }
    public boolean isStruc() {
    	return false;
    }
    public ProcSymbol(FunDecl fd){
   	 this.fd = fd;
   	 this.name = fd.name;
    }
}
