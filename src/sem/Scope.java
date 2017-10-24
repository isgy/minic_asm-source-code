package sem;

import java.util.HashMap;
import java.util.Map;

public class Scope {
	private Scope outer;
	private Map<String, Symbol> symbolTable;
	
	public Scope(Scope outer) { 
		this.outer = outer; 
		symbolTable = new HashMap<String, Symbol>();
	}
	
	public Scope() { 
		symbolTable = new HashMap<String, Symbol>();
	}
	
/*	public String getouter() {
		return "hasouter";
	} */
	public Symbol lookup(String name) {
		Symbol sym = null;
		if(symbolTable.containsKey(name)) {
			sym = symbolTable.get(name);
			return sym;
		}else {
		    if(outer != null) {
			sym = outer.lookup(name); }
		}
	    return sym;
	}
	
	public Symbol lookupCurrent(String name) {
		if(symbolTable == null) {
			System.out.println("notabl");
		}
		Symbol sym = null;
		if(symbolTable.get(name) != null) {
		   return sym;
		}else
			return null;
	}
	
	public void put(Symbol sym) {
		symbolTable.put(sym.name, sym);
	}
}
