package ast;

import java.util.ArrayList;
import java.util.List;

public class ProcType implements Type{
	private List<Type> pTable = new ArrayList<Type>();
	public Type rtype;
	public ProcType(Type rt, Type... ptypes) {
		for(Type ptype : ptypes) {
			pTable.add(ptype);
			rtype = rt;
		}
	}
    public <T> T accept(ASTVisitor<T> v) {
        return v.visitProcType(this);
    }
}
