/*
    Copyright 2012, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package com.strategicgains.restexpress.response;

import java.util.HashMap;
import java.util.Map;

import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.exception.BadRequestException;
import com.strategicgains.restexpress.util.Resolver;

/**
 * @author toddf
 * @since May 14, 2012
 */
public class ResponseProcessorResolver
implements Resolver<ResponseProcessor>
{
	private Map<String, ResponseProcessor> processors = new HashMap<String, ResponseProcessor>();
	private String defaultFormat;
	
	public ResponseProcessorResolver()
	{
		super();
	}
	
	public ResponseProcessorResolver(Map<String, ResponseProcessor> processors, String defaultFormat)
	{
		super();
		this.processors.putAll(processors);
		this.defaultFormat = defaultFormat;
	}
	
	public ResponseProcessor put(String format, ResponseProcessor processor)
	{
		return processors.put(format, processor);
	}
	
	public void setDefaultFormat(String format)
	{
		this.defaultFormat = format;
	}

	@Override
	public ResponseProcessor resolve(Request request)
	{
		ResponseProcessor processor = null;

		processor = resolveViaRequestFormat(request);
		
		if (processor != null)
		{
			return processor;
		}

		processor = getDefault();
		
		if (processor == null)
		{
			throw new BadRequestException("No response processor found for request.");
		}
		
		return processor;
	}

    public ResponseProcessor getDefault()
    {
		return resolveViaSpecifiedFormat(defaultFormat);
    }

	private ResponseProcessor resolveViaRequestFormat(Request request)
	{
		return resolveViaSpecifiedFormat(request.getFormat());
	}
	
	private ResponseProcessor resolveViaSpecifiedFormat(String format)
	{
		if (format == null || format.trim().isEmpty())
		{
			return null;
		}
		
		return processors.get(format);
	}
}
