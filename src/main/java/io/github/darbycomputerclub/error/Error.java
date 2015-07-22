package io.github.darbycomputerclub.error;

/**
 * List of error codes.
 */
public enum Error {

	/**
	 * No config.properties file exists.
	 */
	NO_CONFIG(1, "No config.properties file exists."),
	/**
	 * Other file read exception on config.properties.
	 */
	CONFIG_READ(2, "Other file read exception on config.properties."),
	/**
	 * Other file read exception on config.properties.
	 */
	ALREADY_RUNNING(3, "Process already running. "),
	/**
	 * Other file read exception on config.properties.
	 */
	SOCKET_ERROR(4, "Unexpected Socket Error.");

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
	private Error(final int errorCode, final String errorDescription) {
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