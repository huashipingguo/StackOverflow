package cn.edu.fudan.se.bean.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes.Name;

public class CLass extends ASTBasicDataType
{

	public static final String ANONYMOUSCLASS = "anonymous";
	public static final String ITSELF = "this";
	public static final String PARENT = "super";
	private List<Field> fields = new ArrayList<Field>();
	private String fileName = "";
	private String interfaces = "";
	private boolean isInterface = false;
	private boolean isSuperClass = false;
	private int loc = 0;
	private List<Method> methods = new ArrayList<Method>();
	private String superClass = "";
	private String content = "";

	public  CLass(String name, String modifier)
	{
		super(name, modifier);
	}
	public CLass()
	{
		
	}
	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}




	
	public void addToSuperClass(String superClass)
	{
		this.superClass += this.superClass.equals("") ? superClass : ", "
				+ superClass;
	}
	
	@Override
	public boolean equals(Object arg0)
	{
		// TODO Auto-generated method stub
		return this.getName().equals(((Class)arg0).getName());
	}

	/**
	 * @return the fields
	 */
	public List<Field> getFields()
	{
		return fields;
	}
	
	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @return the interfaces
	 */
	public String getInterfaces()
	{
		return interfaces;
	}

	/**
	 * @return the loc
	 */
	public int getLoc()
	{
		return loc;
	}

	/**
	 * @return the methods
	 */
	public List<Method> getMethods()
	{
		return methods;
	}

	public String getPackageName()
	{
		return getName().substring(0, getName().lastIndexOf("."));
	}

	/**
	 * @return the superClass
	 */
	public String getSuperClass()
	{
		return superClass;
	}

	@Override
	public int hashCode()
	{
		// TODO Auto-generated method stub
		return this.getName().hashCode();
	}

	/**
	 * @return the isInterface
	 */
	public boolean isInterface()
	{
		return isInterface;
	}

	/**
	 * @return the isSuperClass
	 */
	public boolean isSuperClass()
	{
		return isSuperClass;
	}

	/**
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(List<Field> fields)
	{
		this.fields = fields;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @param isInterface
	 *            the isInterface to set
	 */
	public void setInterface(boolean isInterface)
	{
		this.isInterface = isInterface;
	}

	/**
	 * @param interfaces
	 *            the interfaces to set
	 */
	public void setInterfaces(String interfaces)
	{
		this.interfaces = interfaces;
	}

	/**
	 * @param loc
	 *            the loc to set
	 */
	public void setLoc(int loc)
	{
		this.loc = loc;
	}

	/**
	 * @param methods
	 *            the methods to set
	 */
	public void setMethods(List<Method> methods)
	{
		this.methods = methods;
	}

	/**
	 * @param isSuperClass
	 *            the isSuperClass to set
	 */
	public void setSuperClass(boolean isSuperClass)
	{
		this.isSuperClass = isSuperClass;
	}

	/**
	 * @param superClass
	 *            the superClass to set
	 */
	public void setSuperClass(String superClass)
	{
		this.superClass = superClass;
	}

}
