/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.apikit;

import org.mule.VoidMuleEvent;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.routing.filter.FilterUnacceptedException;
import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.CoreMessages;
import org.mule.module.apikit.exception.ApikitRuntimeException;
import org.mule.module.apikit.transform.ApikitResponseTransformer;
import org.mule.transport.NullPayload;

import java.util.List;

import org.raml.model.MimeType;

public class HttpRestRouterRequest extends HttpRestRequest
{

    private ApikitResponseTransformer responseTransformer;

    public HttpRestRouterRequest(MuleEvent event, AbstractConfiguration config)
    {
        super(event, config);
        if (!RuntimeCapabilities.supportsDinamicPipeline())
        {
            responseTransformer = new ApikitResponseTransformer();
            responseTransformer.setMuleContext(requestEvent.getMuleContext());
        }
    }

    @Override
    protected MuleEvent processResponse(MuleEvent responseEvent, List<MimeType> responseMimeTypes, String responseRepresentation) throws TransformerException, FilterUnacceptedException
    {
        if (responseEvent == null || VoidMuleEvent.getInstance().equals(responseEvent))
        {
            throw new FilterUnacceptedException(CoreMessages.messageRejectedByFilter(), requestEvent);
        }
        MuleMessage message = responseEvent.getMessage();

        if (responseTransformer != null)
        {
            if (responseRepresentation != null)
            {
                Object newPayload = responseTransformer.transformToExpectedContentType(message, responseRepresentation, responseMimeTypes);
                if (!message.getPayload().equals(newPayload))
                {
                    message.setPayload(newPayload);
                }
            }
            else
            {
                //sent empty response body when no response mime-type is defined
                message.setPayload(NullPayload.getInstance());
            }
        }

        //set success status
        if (message.getOutboundProperty("http.status") == null)
        {
            int status = getSuccessStatus();
            if (status == -1)
            {
                throw new ApikitRuntimeException("No success status defined for action: " + action);
            }
            message.setOutboundProperty("http.status", getSuccessStatus());
        }

        return responseEvent;
    }
}
