package gw.lang.reflect.gs;

import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.parser.ParserSource;
import gw.lang.reflect.ITemporaryFileProvider;

import java.io.File;
import java.io.Reader;
import java.util.List;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IFileSystemGosuClassRepository extends IGosuClassRepository
{
  List<IDirectory> getClassPath();

  void addClassPathEntry( String strQualifiedClassName, boolean test, IDirectory path );

  IFile createFileForClass(IDirectory rootDir, String qualifiedClassName, String extension);

  IDirectory getDirForNewPackage( IDirectory dirForModule, String strQualifiedPackageName );

  IDirectory findDirForPackage( String strQualifiedPackageName, IDirectory rootDirectory );

  String getClassNameFromFile( IDirectory root, IFile file, String[] fileExts );

  IClassFileInfo getOrCreateClassFileInfo(String strQualifiedClassName, IDirectory dirForModule, String extension, boolean createFile);

  void addResourcesToClassPath( List<IDirectory> resourceRootDirs );
  
  void removeResourcesFromClasspath( List<IDirectory> resourceRootDirs );

  String[] getExtensions();

  void clearPackageCache();

  void clearMissCache();

  void removeTemporaryFileProvider(ITemporaryFileProvider provider);

  void addTemporaryFileProvider(ITemporaryFileProvider provider);

  public static class ClassPathEntry
  {
    private IDirectory _path;
    private boolean _isTestResource;

    public ClassPathEntry( IDirectory path, boolean testResource )
    {
      _path = path;
      _isTestResource = testResource;
    }

    public IDirectory getPath()
    {
      return _path;
    }

    public boolean isTestResource()
    {
      return _isTestResource;
    }

    @Override
    public String toString() {
      return "" + _path;
    }

    @Override
    public boolean equals(Object obj) {
      return (obj instanceof ClassPathEntry) &&
              ((ClassPathEntry)obj)._path.equals(_path) &&
              ((ClassPathEntry)obj)._isTestResource == _isTestResource;
    }

    @Override
    public int hashCode() {
      return _path.hashCode();
    }
  }

  /**
  */
  public static interface IClassFileInfo
  {
    IDirectory getParentFile();

    IFile getFile();

    Reader getReader();

    String getFileName();

    String getNonCanonicalFileName();

    String getFilePath();

    int getClassPathLength();

    boolean hasInnerClass();

    ISourceFileHandle getSourceFileHandle();

    ClassPathEntry getEntry();

    ParserSource getSource();

    String getContent();
  }

  /**
 */
  public static interface IFileSystemSourceFileHandle extends ISourceFileHandle
  {
    IDirectory getParentFile();

    IFileSystemGosuClassRepository.IClassFileInfo getFileInfo();
  }

  class Util
  {

    public static boolean isClassFileName( String strName, String[] fileExts )
    {
      for( String strExt : fileExts )
      {
        if( strName.endsWith( strExt ) )
        {
          return true;
        }
      }
      return false;
    }

    public static void shiftInnerClassToFileName( StringBuilder innerClass, StringBuilder fileName )
    {
      if( innerClass.length() > 0 && fileName.length() > 0 )
      {
        fileName.append( File.separatorChar );
      }
      while( innerClass.length() > 0 && innerClass.charAt( 0 ) != '.' )
      {
        fileName.append( innerClass.charAt( 0 ) );
        innerClass.deleteCharAt( 0 );
      }
      if( innerClass.length() > 0 && innerClass.charAt( 0 ) == '.' )
      {
        innerClass.deleteCharAt( 0 );
      }
    }
  }
}
