package cn.edu.fudan.se.result;

public class IdValue {
	
	int id;
	float value;
	
	public IdValue(int id,float value)
	{
		this.id = id;
		this.value = value;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}

}
