package io.github.darbycomputerclub.error;

/**
 * List of error codes.
 */
public enum Error {

	/**
	 * No config.properties file exists.
	 */
	UNKNOWN(1, "Unknown error.");

	/**
	 * Error code.
	 */
	private final int code;
	/**
	 * Description of error.
	 */
	private final String description;

	/**
	 * Error cause.
	 * 
	 * @param errorCode Error code
	 * @param errorDescription 
	 */
	Error(final int errorCode, final String errorDescription) {
		this.code = errorCode;
		this.description = errorDescription;
	}

	/**
	 * Gets the description of error.
	 * @return Description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * gets the error code.
	 * @return Code
	 */
	public int getCode() {
		return code;
	}

	@Override
	public String toString() {
		return "Error " + code + ": " + description;
	}
}