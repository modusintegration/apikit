/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
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
