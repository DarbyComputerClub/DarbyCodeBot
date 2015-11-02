/**
 * 
 */
package io.github.darbycomputerclub;

/**
 * 
 * 
 * @author Alex Shafer
 * @see http://semver.org/
 */
public enum Version {
	/**
	 * Current version info.
	 */
	CURRENT(1, 0, 3, true);
	
	/**
	 * Major version number.
	 */
	private int major;
	/**
	 * Minor version number.
	 */
	private int minor;
	/**
	 * Minor version number.
	 */
	private int patch;
	/**
	 * Is developer version.
	 */
	private boolean developerVersion;
	
	/**
	 * Creates version number.
	 * 
	 * @param majorVersion Major version
	 * @param minorVersion Minor version
	 * @param patchVersion Patch version
	 * @param isDev Is this a developer version
	 * 
	 * @see http://semver.org/
	 */
	Version(final int majorVersion, final int minorVersion, 
			final int patchVersion, final boolean isDev) {
		this.major = majorVersion;
		this.minor = minorVersion;
		this.patch = patchVersion;
		this.developerVersion = isDev;
	}

	/**
	 * @return the major
	 */
	public int getMajorNumber() {
		return major;
	}

	/**
	 * @return the minor
	 */
	public int getMinorNumber() {
		return minor;
	}

	/**
	 * @return the patch
	 */
	public int getPatchNumber() {
		return patch;
	}

	/**
	 * @return is developer version
	 */
	public boolean isDeveloperVersion() {
		return developerVersion;
	}
	
	@Override
	public String toString() {
		String versionName =  major + "." + minor + "." + patch;
		
		if (developerVersion) {
			versionName += "-ALPHA";
		}
		
		return versionName;
	}
}
