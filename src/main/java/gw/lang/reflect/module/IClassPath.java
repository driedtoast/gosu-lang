package gw.lang.reflect.module;

import gw.util.fingerprint.FP64;
import gw.lang.UnstableAPI;

import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * NOTE: This API is in flux.  This class may be moved to a different package, renamed, or have its methods change
 * in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IClassPath
{
  String GW_API_PREFIX = "gw.";
  String GW_INTERNAL_PREFIX = "gw.internal.";
  String SUN_CLASS_PREFIX = "sun.";
  String COM_SUN_CLASS_PREFIX = "com.sun.";

  String PLACEHOLDER_FOR_PACKAGE = "PLACEHOLDER";

  ClassPathFilter ALLOW_ALL_FILTER =
    new ClassPathFilter()
    {
      public boolean acceptClass( String className )
      {
        // Do not expose Sun's classes. We shouldn't encourage their use and some
        // of them behave badly during static initialization, which interferes
        // with the typeinfo database.
        return !className.startsWith( SUN_CLASS_PREFIX ) &&
               !className.startsWith( COM_SUN_CLASS_PREFIX );
      }
    };

  ClassPathFilter ONLY_API_CLASSES =
    new ClassPathFilter()
    {
      public boolean acceptClass( String className )
      {
        return className.startsWith( GW_API_PREFIX ) && !className.startsWith(GW_INTERNAL_PREFIX);
      }
    };

  String[] getPaths();

  ClassPathInfo loadClasspathInfo( ClassPathFilter filter, boolean includeAllNames );

  public static interface ClassPathFilter
  {
    public boolean acceptClass( String className );
  }

  public static class ClassPathInfo
  {
    Map<String, FileInfo> _fileInfos = new HashMap<String, FileInfo>();
    private Set<Long> _allFingerprints = new HashSet<Long>( );
    private boolean _collectFingerprints;

    public ClassPathInfo( boolean collectFingerprints )
    {
      _collectFingerprints = collectFingerprints;
    }

    public Map<String, FileInfo> getFilteredFileInfos()
    {
      return _fileInfos;
    }

    public boolean isCollectFingerprints()
    {
      return _collectFingerprints;
    }

    public Set<String> getFilteredClassNames()
    {
      return _fileInfos.keySet();
    }

    public void visitClassName( String className )
    {
      if( _collectFingerprints )
      {
        _allFingerprints.add( new FP64(className).getRawFingerprint() );
      }
    }

    public boolean isPossibleValidClassName( String name )
    {
      return !_collectFingerprints ||
              _allFingerprints.contains( new FP64( name ).getRawFingerprint() ) ||
              name.startsWith( GW_INTERNAL_PREFIX ); // have to include internal classes since we filter those out
                                                     // in the default environment
    }
  }

  public static class FileInfo {
    private boolean _isFiltered;
    private File file;
    private long _lastModDate;
    private int _size;

    public FileInfo(boolean filtered, File file) {
      _isFiltered = filtered;
      this.file = file;
      _lastModDate = -1;
    }

    public boolean isFiltered() {
      return _isFiltered;
    }

    public FP64 getFingerprint() {
      if(_lastModDate == -1) {
        _lastModDate = file.lastModified();
        _size = (int) file.length();
      }
      FP64 fp64 = new FP64();
      fp64.extend(_lastModDate);
      fp64.extend(_size);
      return fp64;
    }
  }
}
