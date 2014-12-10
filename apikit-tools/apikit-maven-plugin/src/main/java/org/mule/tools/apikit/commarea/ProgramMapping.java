package org.mule.tools.apikit.commarea;

public class ProgramMapping {
	
	private String name;
	
	private String abstractJavaTransformer;
	
	private String jsonToObjectReturn;

	public ProgramMapping() {
		
	}
	
	public String getAbstractJavaTransformer() {
		return abstractJavaTransformer;
	}

	public void setAbstractJavaTransformer(String abstractJavaTransformer) {
		this.abstractJavaTransformer = abstractJavaTransformer;
	}

	public String getJsonToObjectReturn() {
		return jsonToObjectReturn;
	}

	public void setJsonToObjectReturn(String jsonToObjectReturn) {
		this.jsonToObjectReturn = jsonToObjectReturn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
