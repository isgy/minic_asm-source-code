package sem;

import ast.FunDecl;

public class ReturnSymbol extends Symbol{
    FunDecl fd;

    public boolean isVar() {
    	return false;
    }
    public boolean isProc() {
    	return false;
    }
    public boolean isStruc() {
    	return true;
    }
    
    
    public ReturnSymbol(FunDecl fd){
   	 this.fd = fd;
   	 this.name = fd.name;
    }
}

