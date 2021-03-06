/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 2000 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "SOAP" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 2000, International
 * Business Machines, Inc., http://www.apache.org.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.soap.rpc;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.soap.util.xml.*;
import org.apache.soap.*;
import org.apache.soap.encoding.*;

/**
 * A <code>Response</code> object represents an <em>RPC</em> response. Both
 * the client and the server use <code>Response</code> objects to represent
 * the result of a method invocation.
 *
 * @author Matthew J. Duftler (duftler@us.ibm.com)
 * @author Sanjiva Weerawarana (sanjiva@watson.ibm.com)
 */
public class Response extends RPCMessage
{
  private Parameter returnValue;
  private Fault     fault;

  /**
   * Use this constructor when everything went well.
   */
  public Response(String targetObjectURI, String methodName,
                  Parameter returnValue, Vector params, Header header,
                  String encodingStyleURI)
  {
    super(targetObjectURI, methodName, params, header, encodingStyleURI);

    this.returnValue = returnValue;
  }

  /**
   * Use this constructor when things didn't go so well.
   */
  public Response(String targetObjectURI, String methodName,
                  Fault fault, Vector params, Header header,
                  String encodingStyleURI)
  {
    super(targetObjectURI, methodName, params, header, encodingStyleURI);

    this.fault = fault;
  }

  public void setReturnValue(Parameter returnValue)
  {
    this.returnValue = returnValue;
  }

  public Parameter getReturnValue()
  {
    return returnValue;
  }

  public void setFault(Fault fault)
  {
    this.fault = fault;
  }

  public Fault getFault()
  {
    return fault;
  }

  public boolean generatedFault()
  {
    return fault != null;
  }

  public Envelope buildEnvelope()
  {
    return super.buildEnvelope(true);
  }

  public static Response extractFromEnvelope(Envelope env,
                                             SOAPMappingRegistry smr)
    throws IllegalArgumentException
  {
    return (Response)RPCMessage.extractFromEnvelope(env, null, true, smr);
  }
}