package cn.edu.fudan.se.bean.ast;

public class Method extends ASTBasicDataType {
    
	private String name="";
	private String className = "";
	private String params = "";
	private String exceptions = "";
	private boolean isConstructor = false;
	private boolean isDestructor = false;
	private String returnType = "";
	private int loc = 0;
	private int nbd = 0;
	private String content = "";
	
	
	//lihongwei 2012-3-14 add  for new ast.db test 
	//name,javadoc,className,returnDataType,modifies,exceptionList,params,loc,nbd,isConstructor,content
	
	private String javadoc="";
	private byte[] PNGwordcloud=new byte[1024];

	//private String className="";
	private String returnDataType="";
	public byte[] getPNGwordcloud()
	{
		return PNGwordcloud;
	}

	public void setPNGwordcloud(byte[] pNGwordcloud)
	{
		PNGwordcloud = pNGwordcloud;
	}

	private String modifies="";
	private String exceptionList="";
	//private String params="";
	//private int loc=0;
	//private int nbd = 0;
	//private boolean isConstructor = false;
	
	public Method(String name,String javadoc,String className,String returnDataType,String modifies,String exceptionList,String params,int loc,int nbd,boolean isConstructor,String content) 
	{
		
		this.name=name;
		this.javadoc=javadoc;
		this.className = className;
		this.returnDataType=returnDataType;
		this.modifies=modifies;
		this.exceptionList=exceptionList;
		this.params = params;
		this.loc=loc;
		this.nbd=nbd;
		this.isConstructor = isConstructor;
		this.content = content;
	}
	
	/**
	 * @param name
	 * @param modifier
	 * @param className
	 * @param params
	 * @param exceptions
	 * @param isConstructor
	 * @param isDestructor
	 * @param returnType
	 */
	public Method(String name, String modifier, String className,
			String params, String exceptions, boolean isConstructor,
			boolean isDestructor, String returnType) {
		super(name, modifier);
		this.className = className;
		this.params = params;
		this.exceptions = exceptions;
		this.isConstructor = isConstructor;
		this.isDestructor = isDestructor;
		this.returnType = returnType;
	}

	/**
	 * @param name
	 * @param modifier
	 */
	public Method(String name, String modifier) {
		super(name, modifier);
	}

	/**
	 * 
	 */
	public Method() {
		super();
	}

	
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getJavadoc()
	{
		return javadoc;
	}

	public void setJavadoc(String javadoc)
	{
		this.javadoc = javadoc;
	}

	public String getReturnDataType()
	{
		return returnDataType;
	}

	public void setReturnDataType(String returnDataType)
	{
		this.returnDataType = returnDataType;
	}

	public String getModifies()
	{
		return modifies;
	}

	public void setModifies(String modifies)
	{
		this.modifies = modifies;
	}

	public String getExceptionList()
	{
		return exceptionList;
	}

	public void setExceptionList(String exceptionList)
	{
		this.exceptionList = exceptionList;
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
	 * @return the params
	 */
	public String getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(String params) {
		this.params = params;
	}

	public void addToParams(String params) {
		if (this.params.equals("")) {
			this.params = params;
		} else {
			this.params += ", " + params;
		}
	}

	/**
	 * @return the exceptions
	 */
	public String getExceptions() {
		return exceptions;
	}

	/**
	 * @param exceptions
	 *            the exceptions to set
	 */
	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}

	/**
	 * @return the isConstructor
	 */
	public boolean isConstructor() {
		return isConstructor;
	}

	/**
	 * @param isConstructor
	 *            the isConstructor to set
	 */
	public void setConstructor(boolean isConstructor) {
		this.isConstructor = isConstructor;
	}

	/**
	 * @return the isDestructor
	 */
	public boolean isDestructor() {
		return isDestructor;
	}

	/**
	 * @param isDestructor
	 *            the isDestructor to set
	 */
	public void setDestructor(boolean isDestructor) {
		this.isDestructor = isDestructor;
	}

	/**
	 * @return the returnType
	 */
	public String getReturnType() {
		return returnType;
	}

	/**
	 * @param returnType
	 *            the returnType to set
	 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	/**
	 * @return the loc
	 */
	public int getLoc() {
		return loc;
	}

	/**
	 * @param loc
	 *            the loc to set
	 */
	public void setLoc(int loc) {
		this.loc = loc;
	}

	/**
	 * @return the nbd
	 */
	public int getNbd() {
		return nbd;
	}

	/**
	 * @param nbd
	 *            the nbd to set
	 */
	public void setNbd(int nbd) {
		this.nbd = nbd;
	}

	public String toString() {
		String result = "";
		result += "\n[Method Declaration Start]";
		result += "\t\nClass Name: \t" + this.getClassName();
		result += "\t\nMethod Name: \t" + this.getName();
		result += "\t\nParamaters: \t" + this.getParams();
		result += "\t\nModifiers: \t" + this.getModifies();
		result += "\t\nReturn Type: \t" + this.getReturnType();
		result += "\t\nIs Constructor?\t" + this.isConstructor();
		result += "\t\nIs Destructor?\t" + this.isDestructor();
		result += "\t\nExceptions: \t" + this.getExceptions();
		result += "\t\nLines of Code: \t" + this.getLoc();
		result += "\t\nNested Block Depth: \t" + this.getNbd();
		//result += "\t\nContent: \t" + this.getContent();
		result += "\t\n[Method Declaration End]\n\n";
		return result;
	}

	public static void main(String args[]) {
		Method method = new Method();
		method.setClassName("cn.edu.fuan.se.ASTUtils");
		method.setName("getAST");
		method.setModifies("public");
		method.addToParams("IASTName name");
		method.setConstructor(false);
		method.setDestructor(true);
		method.setExceptions("");
		method.setReturnType("IASTTranslationUnit");
		System.out.println(method);
	}

	public void setContent(String content)
	{
		this.content = content;
		
	}
	
	public String getContent()
	{
		return this.content;
		
	}

}
