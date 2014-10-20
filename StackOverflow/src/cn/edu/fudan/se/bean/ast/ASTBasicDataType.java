package cn.edu.fudan.se.bean.ast;

public abstract class ASTBasicDataType {

	private String name = "";
	private String modifier = "";
	
	public ASTBasicDataType()
	{
		
	}
	/**
	 * @param name
	 * @param modifier
	 */
	public ASTBasicDataType(String name, String modifier) {
		this.name = name;
		this.modifier = modifier;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the modifier
	 */
	public String getModifier() {
		return modifier;
	}

	/**
	 * @param modifier
	 *            the modifier to set
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

}
