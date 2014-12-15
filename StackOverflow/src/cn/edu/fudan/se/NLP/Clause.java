package cn.edu.fudan.se.NLP;

import java.util.List;

import cn.edu.fudan.se.Partern.Expression;
import cn.edu.fudan.se.domain.dictionary.Negative;

public class Clause {
	
	private String clause;
	
	private Subject subject;
	
	private Predicate predicate;
	
	private Object object;
	
	private String authority = "accept";
	
	public void init()
	{
		if(Expression.getInstance().Authority(clause))
		{
			authority = "deny";
		}
		List<WordProperty> wpList = predicate.getPredicateList();
		Negative n = new Negative();
		for(WordProperty wp:wpList)
		{
			if(wp.getProperty().contains("V"))
			{
				if(n.Authority(wp.getLemmaWord()))
				{
					authority = "deny";
				}
			}
		}
	}
	
	public String getAuthority()
	{
		return authority;
	}

	public String getClause() {
		return clause;
	}

	public void setClause(String clause) {
		this.clause = clause;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Predicate getPredicate() {
		return predicate;
	}

	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
	

}
