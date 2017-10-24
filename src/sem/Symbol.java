package sem;

public abstract class Symbol {
	public String name;
	public char symtype;
	boolean isVar() {
       if(this.symtype == 'v') {
    	   return true;
       }else
    	   return false;
	}
	boolean isProc() {
		if(this.symtype == 'p') {
			return true;
		}else
			return false;
		
	}
	boolean isStruc() {
		if(this.symtype == 's') {
			return true;
		}else
			return false;
	}
	
//	public abstract <T> T accept(NameAnalysisVisitor<T> v);
	
	public Symbol(String name) {
		this.name = name;
	}
	public Symbol() {
		
	}
}
