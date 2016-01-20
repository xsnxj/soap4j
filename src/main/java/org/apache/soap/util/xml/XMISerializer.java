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

package org.apache.soap.util.xml;

import com.ibm.xmi.job.*;
import java.io.*;
import java.util.*;
import org.w3c.dom.*;

/**
 * An <code>XMISerializer</code> ...
 *
 * @author Francisco Curbera (curbera@us.ibm.com)
 */
public class XMISerializer implements Serializer
{
  public static int CONV_BSIZE=0x400;
  private static XercesParserLiaison xpl = null;

  public void marshall(String inScopeEncStyle, Class javaType, Object src,
                       Object context, Writer sink, NSStack nsStack,
                       XMLJavaMappingRegistry xjmr)
       throws IllegalArgumentException, IOException

  {

    //
    // not using namespaces - this is under the Job classes control
    // may need to check that the encoding style is in fact xmi...
    // ignoring context

    // special case for Strings - otherwise treated as uuids by the serializer
    
    if( xpl == null )
      xpl = new  XercesParserLiaison();
    
    if (src == null)
    {
      sink.write("<null type=\"" + javaType.getName() + "\"/>");
      return;
    }
    else if( javaType ==java.lang.String.class )
    {	
      sink.write("<java.lang.String value='" + src + "' />");
      return;
    }
    
    System.err.println(src);
    Vector olist = new Vector();
    olist.addElement(src);
    
    
    PipedOutputStream tmpout = new PipedOutputStream();
    PipedInputStream tmpin  = new PipedInputStream();
    
    tmpin.connect(tmpout);

    com.ibm.xmi.framework.WriterFactory.setInline(true);
    Job.writeObjects((List)olist, (OutputStream)tmpout);
               
    byte[] readinto = new byte[XMISerializer.CONV_BSIZE];
    int len, left = 0;
    
    while( (left = tmpin.available())> 0 )
      {
	len = tmpin.read(readinto, 0, Math.min(left, XMISerializer.CONV_BSIZE));
	String convert = new String(readinto, 0, len);
	sink.write(convert);
      }
    
    tmpout.close();
    tmpin.close();
  }
  
}
