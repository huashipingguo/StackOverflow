package cn.edu.fudan.se.bean.ast;


public class Field extends ASTBasicDataType {

	private String className = "";
	private String dataType = "";

	/**
	 * @param name
	 * @param modifier
	 * @param className
	 * @param dataType
	 */
	public Field(String name, String modifier, String className,
			String dataType) {
		super(name, modifier);
		this.className = className;
		this.dataType = dataType;
	}

	/**
	 * 
	 */
	public Field() {
		super();
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * @param dataType
	 *            the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String toString() {
		String result = "";
		result += "\n[Field Declaration Start]";
		result += "\t\nModifier: \t" + this.getModifier();
		result += "\t\nClass Name: \t" + this.getClassName();
		result += "\t\nField Name: \t" + this.getName();
		result += "\t\nData Type: \t" + this.getDataType();
		result += "\t\n[Field Declaration End]\n\n";
		return result;
	}
}
