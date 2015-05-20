/* 
 * The MIT License
 *
 * Copyright 2015 Lukas Kaupp 'Bl4ckM' <lukas.kaupp@stud.h-da.de>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.bl4ckbird.ebitm.pop;
/**
 *  
 * @author Matthew Herod, https://github.com/mherod/pop3server
 * 
 */
public class POP3Response {

	public static final char TERM = 46;

	public static final byte[] CRLF = { 13, 10 };
	public static final String EOL = new String(CRLF);

	private Type type = Type.ERR;
	private String response = null;
	private String tag = null;
	private String extra = null;

	public static enum Type {
		OK, ERR
	}

	public POP3Response(Type type) {
		setResponseType(type);
	}

	public POP3Response(Type type, String response) {
		setResponseType(type).setResponse(response);
	}

	public POP3Response setResponseType(Type type) {
		this.type = type;
		return this;
	}

	public POP3Response setResponse(String response) {
		this.response = response;
		return this;
	}

	public POP3Response setTag(String tag) {
		this.tag = tag;
		return this;
	}

	public POP3Response appendExtraLine(String line) {
		if (extra == null) {
			extra = "";
		}
		extra += line + EOL;
		return this;
	}

	public POP3Response appendTerminationLine() {
		if (extra == null) {
			extra = "";
		}
		extra += TERM + EOL;
		return this;
	}

	public String buildOutput() {
		String output = (type == Type.OK ? "+OK" : "-ERR")
				+ (response == null ? "" : " " + response)
				+ (tag == null ? EOL : " " + tag + EOL);
		if (extra != null) {
			output += extra;
		}
		return output;
	}

}
