/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.apikit.output.scopes;


import org.jdom2.Element;
import org.mule.tools.apikit.output.GenerationModel;
import org.raml.model.parameter.Header;

import static org.mule.tools.apikit.output.MuleConfigGenerator.XMLNS_NAMESPACE;
import static org.mule.tools.apikit.output.MuleConfigGenerator.JSON_NAMESPACE;
import static org.mule.tools.apikit.output.MuleConfigGenerator.DOC_NAMESPACE;
import static org.mule.tools.apikit.output.MuleConfigGenerator.UNIKIX_NAMESPACE;

public class APIKitFlowScope implements Scope {
    private final Element flow;

    public APIKitFlowScope(GenerationModel flowEntry) {
        flow = new Element("flow", XMLNS_NAMESPACE.getNamespace());
        flow.setAttribute("name", flowEntry.getFlowName());

        if( flowEntry.getContentType() != null ) {
            Element setContentType = new Element("set-property", XMLNS_NAMESPACE.getNamespace());
            setContentType.setAttribute("propertyName", "Content-Type");
            setContentType.setAttribute("value", flowEntry.getContentType());
            flow.addContent(setContentType);
        }

        if ( flowEntry.getProgramMapping() != null ) {
        	
//          <json:json-to-object-transformer doc:name="JSON to Object" returnClass="com.gap.cobol.zz90com1.CaZz90PgmCommarea"/>
            
            Element jto = new Element("json-to-object-transformer", JSON_NAMESPACE.getNamespace());
            String className = flowEntry.getProgramMapping().getJsonToObjectReturn();
            if ( className == null ) {
            	className = "java.lang.Object";
            }
            jto.setAttribute("returnClass", className);
            flow.addContent(jto);

//          <message-properties-transformer doc:name="Message Properties">

            Element mpt = new Element("message-properties-transformer", XMLNS_NAMESPACE.getNamespace());
            mpt.setAttribute("name", "Message Properties", DOC_NAMESPACE.getNamespace());

//            <add-message-property key="AbstractJavaTransformer" value="com.gap.cobol.zz90com1.bind.CaZz90PgmCommareaJavaToHostTransformer"/>
            Element amp = new Element("add-message-property", XMLNS_NAMESPACE.getNamespace());
            String javaTransformerName = flowEntry.getProgramMapping().getAbstractJavaTransformer();
            if ( javaTransformerName == null ) {
            	javaTransformerName = "**TBD**";
            }
            amp.setAttribute("key", "AbstractJavaTransformer");
            amp.setAttribute("value", javaTransformerName);
            mpt.addContent(amp);
            
//            </message-properties-transformer>
            flow.addContent(mpt);

//            <component class="com.gap.seamless.transformers.CommareaToByteArray" doc:name="Commarea to Byte Array"/>
            Element comp = new Element("component", XMLNS_NAMESPACE.getNamespace());
            comp.setAttribute("class", "com.gap.seamless.transformers.CommareaToByteArray");
            comp.setAttribute("name", "Commarea to Byte Array", DOC_NAMESPACE.getNamespace());
            flow.addContent(comp);
            
            
//            <response>
            Element response1 = new Element("response", XMLNS_NAMESPACE.getNamespace());
            
//        	<json:object-to-json-transformer doc:name="Object to JSON"/>
            Element otjt = new Element("object-to-json-transformer", JSON_NAMESPACE.getNamespace());
            otjt.setAttribute("name", "Object to JSON", DOC_NAMESPACE.getNamespace());
            response1.addContent(otjt);

//          </response>
            flow.addContent(response1);
            

//          <response>
			Element response2 = new Element("response", XMLNS_NAMESPACE.getNamespace());
			  
			//   <component class="com.gap.seamless.transformers.ByteArrayToCommarea" doc:name="ByteArray to Commarea"/>
			Element comp2 = new Element("component", XMLNS_NAMESPACE.getNamespace());
			comp2.setAttribute("class", "com.gap.seamless.transformers.ByteArrayToCommarea");
			comp2.setAttribute("name", "ByteArray to Commarea", DOC_NAMESPACE.getNamespace());
			response2.addContent(comp2);
	
	//        </response>
			flow.addContent(response2);
          
            String programName = "XXXPROGRAM_NAMEXXX";
            Header programNameHeader = flowEntry.getAction().getHeaders().get("programName");
            if ( programNameHeader != null ) {
            	programName = programNameHeader.getDefaultValue();
            }

            //<unikix:invoke-cics-program config-ref="Unikix" doc:name="Unikix"/>

            Element unikix = new Element("invoke-cics-program", UNIKIX_NAMESPACE.getNamespace());
            unikix.setAttribute("config-ref", "Unikix");
            unikix.setAttribute("name", "Unikix", DOC_NAMESPACE.getNamespace());
            unikix.setAttribute("programname", programName);
			flow.addContent(unikix);

        } else {
          Element example = new Element("set-payload", XMLNS_NAMESPACE.getNamespace());
          example.setAttribute("value", flowEntry.getExample().trim());
          flow.addContent(example);
        }
        
    }

    @Override
    public Element generate() {
        return flow;
    }
}
