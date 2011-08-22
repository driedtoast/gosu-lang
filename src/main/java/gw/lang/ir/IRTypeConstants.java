package gw.lang.ir;

import gw.lang.reflect.IType;
import gw.lang.GosuShop;
import gw.lang.UnstableAPI;

import java.util.Iterator;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public interface IRTypeConstants {
  static IRType ITYPE = GosuShop.getIRTypeResolver().getDescriptor( IType.class );
  static IRType OBJECT = GosuShop.getIRTypeResolver().getDescriptor( Object.class );
  static IRType STRING = GosuShop.getIRTypeResolver().getDescriptor( String.class );
  static IRType pVOID = GosuShop.getIRTypeResolver().getDescriptor( void.class );
  static IRType pBOOLEAN = GosuShop.getIRTypeResolver().getDescriptor( boolean.class );
  static IRType pBYTE = GosuShop.getIRTypeResolver().getDescriptor( byte.class );
  static IRType pSHORT = GosuShop.getIRTypeResolver().getDescriptor( short.class );
  static IRType pCHAR = GosuShop.getIRTypeResolver().getDescriptor( char.class );
  static IRType pINT = GosuShop.getIRTypeResolver().getDescriptor( int.class );
  static IRType pLONG = GosuShop.getIRTypeResolver().getDescriptor( long.class );
  static IRType pFLOAT = GosuShop.getIRTypeResolver().getDescriptor( float.class );
  static IRType pDOUBLE = GosuShop.getIRTypeResolver().getDescriptor( double.class );
  static IRType ITERATOR = GosuShop.getIRTypeResolver().getDescriptor( Iterator.class );
  static IRType NUMBER = GosuShop.getIRTypeResolver().getDescriptor( Number.class );
}
