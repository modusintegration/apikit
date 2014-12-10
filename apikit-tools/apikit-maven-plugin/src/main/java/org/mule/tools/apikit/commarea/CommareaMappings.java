/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.apikit.commarea;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommareaMappings {
	
	private List<ProgramMapping> mappings = new ArrayList<>();
	
	public static CommareaMappings fromFile(File src) throws JsonParseException, JsonMappingException, IOException {
		if ( src == null || !src.exists())  {
			return new CommareaMappings();
		}
		ObjectMapper mapper = new ObjectMapper();
		CommareaMappings mappings = mapper.readValue(src, CommareaMappings.class);
		 
		return mappings;
	}
	
	
	public CommareaMappings(){
	}

	public List<ProgramMapping> getMappings() {
		return mappings;
	}

	public void setMappings(List<ProgramMapping> mappings) {
		this.mappings = mappings;
	}
	
	public ProgramMapping getProgramMappings(String name) {
		for (ProgramMapping programMapping : mappings) {
			if ( programMapping.getName().equalsIgnoreCase(name)) {
				return programMapping;
			}
		}
		return null;
	}


	public void addProgramMapping(ProgramMapping programMapping) {
		getMappings().add(programMapping);
	}
	
	

}
