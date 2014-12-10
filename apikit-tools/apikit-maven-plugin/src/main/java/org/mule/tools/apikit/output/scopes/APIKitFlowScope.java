/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.apikit.output.scopes;


import org.jdom2.Element;

import org.mule.tools.apikit.output.GenerationModel;

import static org.mule.tools.apikit.output.MuleConfigGenerator.XMLNS_NAMESPACE;
import static org.mule.tools.apikit.output.MuleConfigGenerator.JSON_NAMESPACE;

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

        if ( flowEntry.getProgramMappings() != null ) {
            //TODO MAPPING add mapping elements
        	
//          <json:json-to-object-transformer doc:name="JSON to Object" returnClass="com.gap.cobol.zz90com1.CaZz90PgmCommarea"/>
            
            Element jto = new Element("json-to-object-transformer", JSON_NAMESPACE.getNamespace());
            String className = flowEntry.getProgramMappings().get("JSON to Object");
            if ( className == null ) {
            	className = "java.lang.Object";
            }
            jto.setAttribute("returnClass", className);
            flow.addContent(jto);

//            <message-properties-transformer doc:name="Message Properties">
            //FIXME  MAPPING check doc:name namespace

            Element mpt = new Element("message-properties-transformer");
            mpt.setAttribute("name", "Message Properties");
            flow.addContent(mpt);

//                <add-message-property key="AbstractJavaTransformer" value="com.gap.cobol.zz90com1.bind.CaZz90PgmCommareaJavaToHostTransformer"/>
            Element amp = new Element("add-message-property");
            String javaTransformerName = flowEntry.getProgramMappings().get("AbstractJavaTransformer");
            if ( javaTransformerName == null ) {
            	javaTransformerName = "**TBD**";
            }
            amp.setAttribute("AbstractJavaTransformer", javaTransformerName);
            mpt.addContent(amp);
            
//            </message-properties-transformer>

            //bla bla bla
            //FIXME MAPPING add these too
            /*
            <component class="com.gap.seamless.transformers.CommareaToByteArray" doc:name="Commarea to Byte Array"/>
            <response>
                <json:object-to-json-transformer doc:name="Object to JSON"/>
            </response>
            <response>
                <component class="com.gap.seamless.transformers.ByteArrayToCommarea" doc:name="ByteArray to Commarea"/>
            </response>
            <unikix:invoke-cics-program config-ref="Unikix" doc:name="Unikix"/>
            */
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
