package com.bl4ckbird.ebitm.pop;
/**
 *  
 * @author Matthew Herod, https://github.com/mherod/pop3server
 * 
 */
public class Response {

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

	public Response(Type type) {
		setResponseType(type);
	}

	public Response(Type type, String response) {
		setResponseType(type).setResponse(response);
	}

	public Response setResponseType(Type type) {
		this.type = type;
		return this;
	}

	public Response setResponse(String response) {
		this.response = response;
		return this;
	}

	public Response setTag(String tag) {
		this.tag = tag;
		return this;
	}

	public Response appendExtraLine(String line) {
		if (extra == null) {
			extra = "";
		}
		extra += line + EOL;
		return this;
	}

	public Response appendTerminationLine() {
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
