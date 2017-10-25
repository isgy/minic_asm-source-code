package sem;

public abstract class Symbol {
	public String name;
	
	abstract boolean isVar();
	abstract boolean isProc();
	abstract boolean isStruc();
//	public abstract <T> T accept(NameAnalysisVisitor<T> v);
	
	public Symbol(String name) {
		this.name = name;
	}
	public Symbol() {
		
	}
}
