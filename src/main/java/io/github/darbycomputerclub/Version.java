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
	CURRENT(1, 1, 0, "beta.1");
	
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
	private String releaseType;
	
	/**
	 * Creates version number.
	 * 
	 * @param majorVersion Major version
	 * @param minorVersion Minor version
	 * @param patchVersion Patch version
	 * @param releaseName Release type
	 * 
	 * @see http://semver.org/
	 */
	Version(final int majorVersion, final int minorVersion, 
			final int patchVersion, final String releaseName) {
		this.major = majorVersion;
		this.minor = minorVersion;
		this.patch = patchVersion;
		this.releaseType = releaseName;
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
	public String getReleaseType() {
		return releaseType;
	}
	
	@Override
	public String toString() {
		String versionName =  major + "." + minor + "." + patch;
		
		if (releaseType != null || !releaseType.equals("")) {
			versionName += "-" + releaseType;
		}
		
		return versionName;
	}
}
