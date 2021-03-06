/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.apikit.validation;

import org.mule.api.MuleEvent;

import org.mule.module.apikit.exception.BadRequestException;
import org.raml.model.Raml;

public interface RestSchemaValidator
{

    void validate(String configId, String schemaPath, MuleEvent muleEvent, Raml api) throws BadRequestException;
}
