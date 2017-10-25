package sem;

import ast.StructTypeDecl;

public class StructSymbol extends Symbol{
    StructTypeDecl sd;

    public boolean isVar() {
    	return false;
    }
    public boolean isProc() {
    	return false;
    }
    public boolean isStruc() {
    	return true;
    }
    
    
    public StructSymbol(StructTypeDecl sd){
   	 this.sd = sd;
   	 this.name = sd.stype.ident;
    }
}
