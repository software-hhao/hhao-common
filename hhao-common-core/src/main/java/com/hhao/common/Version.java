
/*
 * Copyright 2018-2021 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Version.
 * 版本信息格式如下：
 * groupId:artifactId:_majorVersion._minorVersion._patchLevel-_snapshotInfo
 *
 * @author Wang
 * @since 2021 /12/23 14:35
 */
public class Version implements Comparable<Version>, java.io.Serializable {
    protected final Logger logger = LoggerFactory.getLogger(Version.class);
    private static final long serialVersionUID = 1L;
    private final static String SEPARATOR=":";
    private final static String VERSION_SEPARATOR=".";
    private final static String VERSION_SNAPSHOT_SEPARATOR="-";

    private final static Version UNKNOWN_VERSION = new Version(0, 0, 0, null, null, null);

    /**
     * The Major version.
     */
    protected final int _majorVersion;

    /**
     * The Minor version.
     */
    protected final int _minorVersion;

    /**
     * The Patch level.
     */
    protected final int _patchLevel;

    /**
     * The Group id.
     */
    protected final String _groupId;

    /**
     * The Artifact id.
     */
    protected final String _artifactId;

    /**
     * Additional information for snapshot versions; null for non-snapshot
     * (release) versions.
     */
    protected final String _snapshotInfo;

    /**
     * Instantiates a new Version.
     *
     * @param major        the major
     * @param minor        the minor
     * @param patchLevel   the patch level
     * @param snapshotInfo the snapshot info
     * @param groupId      the group id
     * @param artifactId   the artifact id
     */
    public Version(int major, int minor, int patchLevel, String snapshotInfo,
                   String groupId, String artifactId) {
        _majorVersion = major;
        _minorVersion = minor;
        _patchLevel = patchLevel;
        _snapshotInfo = snapshotInfo;
        _groupId = (groupId == null) ? "" : groupId;
        _artifactId = (artifactId == null) ? "" : artifactId;
    }

    /**
     * Method returns canonical "not known" version, which is used as version
     * in cases where actual version information is not known (instead of null).
     *
     * @return Version instance to use as a placeholder when actual version is not known (or not relevant)
     */
    public static Version unknownVersion() {
        return UNKNOWN_VERSION;
    }

    /**
     * Is unknown version boolean.
     *
     * @return the boolean
     */
    public boolean isUnknownVersion() {
        return (this == UNKNOWN_VERSION);
    }

    /**
     * Is snapshot boolean.
     *
     * @return the boolean
     */
    public boolean isSnapshot() {
        return (_snapshotInfo != null && _snapshotInfo.length() > 0);
    }

    /**
     * Gets major version.
     *
     * @return the major version
     */
    public int getMajorVersion() {
        return _majorVersion;
    }

    /**
     * Gets minor version.
     *
     * @return the minor version
     */
    public int getMinorVersion() {
        return _minorVersion;
    }

    /**
     * Gets patch level.
     *
     * @return the patch level
     */
    public int getPatchLevel() {
        return _patchLevel;
    }

    /**
     * Gets group id.
     *
     * @return the group id
     */
    public String getGroupId() {
        return _groupId;
    }

    /**
     * Gets artifact id.
     *
     * @return the artifact id
     */
    public String getArtifactId() {
        return _artifactId;
    }

    /**
     * To full string string.
     *
     * @return the string
     */
    public String toFullString() {
        return _groupId + SEPARATOR + _artifactId + SEPARATOR + toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_majorVersion).append(VERSION_SEPARATOR);
        sb.append(_minorVersion).append(VERSION_SEPARATOR);
        sb.append(_patchLevel);
        sb.append(VERSION_SNAPSHOT_SEPARATOR);
        if (isSnapshot()) {
            sb.append(_snapshotInfo);
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return _artifactId.hashCode() ^ _groupId.hashCode() + _majorVersion - _minorVersion + _patchLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        Version other = (Version) o;
        return (other._majorVersion == _majorVersion)
                && (other._minorVersion == _minorVersion)
                && (other._patchLevel == _patchLevel)
                && other._artifactId.equals(_artifactId)
                && other._groupId.equals(_groupId);
    }

    @Override
    public int compareTo(Version other) {
        if (other == this) {
            return 0;
        }

        int diff = _groupId.compareTo(other._groupId);
        if (diff == 0) {
            diff = _artifactId.compareTo(other._artifactId);
            if (diff == 0) {
                diff = _majorVersion - other._majorVersion;
                if (diff == 0) {
                    diff = _minorVersion - other._minorVersion;
                    if (diff == 0) {
                        diff = _patchLevel - other._patchLevel;
                    }
                }
            }
        }
        return diff;
    }

    /**
     * 将字符串解析为版本信息
     * groupId:artifactId:_majorVersion._minorVersion._patchLevel-_snapshotInfo
     *
     * @param versionInfo
     * @return
     */
    public static Version of(String versionInfo){
        if (versionInfo==null || versionInfo.length()<=0){
            return Version.UNKNOWN_VERSION;
        }
        String [] parts=versionInfo.split(Version.SEPARATOR);
        if (parts==null || parts.length!=3){
            return Version.UNKNOWN_VERSION;
        }
        int majorVersion=0;
        int minorVersion=0;
        int patchLevel=0;
        String snapshotInfo=null;
        String groupId=null;
        String artifactId=null;


        try {
            if (parts[0].trim().length()>0){
                groupId=parts[0].trim();
            }
            if (parts[1].trim().length()>0){
                artifactId=parts[1].trim();
            }
            if (parts[2].trim().length()>0){
                String versionStr=parts[2].trim();
                parts=versionStr.split("\\" + VERSION_SEPARATOR);
                if (parts==null | parts.length!=3){
                    return Version.UNKNOWN_VERSION;
                }
                if (parts[0].trim().length()>0){
                    majorVersion=Integer.parseInt(parts[0].trim());
                }
                if (parts[1].trim().length()>0){
                    minorVersion=Integer.parseInt(parts[1].trim());
                }
                if (parts[2].trim().length()>0){
                    String residue=parts[2].trim();
                    parts=residue.split(VERSION_SNAPSHOT_SEPARATOR);
                    if (parts==null || parts.length<1){
                        return Version.UNKNOWN_VERSION;
                    }
                    if (parts[0].trim().length()>0){
                        patchLevel=Integer.parseInt(parts[0].trim());
                    }
                    if (parts.length>1 && parts[1].trim().length()>0){
                        snapshotInfo=parts[1].trim();
                    }
                }
            }
        } catch (NumberFormatException e) {
            return Version.unknownVersion();
        }
        return new Version(majorVersion,minorVersion,patchLevel,snapshotInfo,groupId,artifactId);
    }

    public static void main(String [] args){
        String str="::0.0.0-";
        System.out.println(Version.of(str).toFullString());
    }
}