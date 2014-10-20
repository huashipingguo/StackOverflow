package cn.edu.fudan.se.result;

import java.util.Comparator;

public class ComparatorIdValue implements Comparator<IdValue>{

	@Override
	public int compare(IdValue arg0, IdValue arg1) {
		// TODO Auto-generated method stub
		 if(arg0.getValue() > arg1.getValue())
			 return -1;
		 else if(arg0.getValue() == arg1.getValue())
			 return 0;
		 else
			 return 1;
	}


}
