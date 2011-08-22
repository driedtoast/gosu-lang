package gw.lang.parser;

import gw.config.BaseService;
import gw.config.CommonServices;
import gw.lang.GosuShop;
import gw.lang.IDimension;
import gw.lang.parser.coercers.BigDecimalCoercer;
import gw.lang.parser.coercers.BigIntegerCoercer;
import gw.lang.parser.coercers.BlockCoercer;
import gw.lang.parser.coercers.BooleanCoercer;
import gw.lang.parser.coercers.BooleanHighPriorityCoercer;
import gw.lang.parser.coercers.BooleanPCoercer;
import gw.lang.parser.coercers.BooleanPHighPriorityCoercer;
import gw.lang.parser.coercers.ByteCoercer;
import gw.lang.parser.coercers.ByteHighPriorityCoercer;
import gw.lang.parser.coercers.BytePCoercer;
import gw.lang.parser.coercers.BytePHighPriorityCoercer;
import gw.lang.parser.coercers.CharCoercer;
import gw.lang.parser.coercers.CharHighPriorityCoercer;
import gw.lang.parser.coercers.CharPCoercer;
import gw.lang.parser.coercers.CharPHighPriorityCoercer;
import gw.lang.parser.coercers.DoubleCoercer;
import gw.lang.parser.coercers.DoubleHighPriorityCoercer;
import gw.lang.parser.coercers.DoublePCoercer;
import gw.lang.parser.coercers.DoublePHighPriorityCoercer;
import gw.lang.parser.coercers.FloatCoercer;
import gw.lang.parser.coercers.FloatHighPriorityCoercer;
import gw.lang.parser.coercers.FloatPCoercer;
import gw.lang.parser.coercers.FloatPHighPriorityCoercer;
import gw.lang.parser.coercers.FunctionFromInterfaceCoercer;
import gw.lang.parser.coercers.FunctionToInterfaceCoercer;
import gw.lang.parser.coercers.IdentityCoercer;
import gw.lang.parser.coercers.IntCoercer;
import gw.lang.parser.coercers.IntHighPriorityCoercer;
import gw.lang.parser.coercers.IntPCoercer;
import gw.lang.parser.coercers.IntPHighPriorityCoercer;
import gw.lang.parser.coercers.LongCoercer;
import gw.lang.parser.coercers.LongHighPriorityCoercer;
import gw.lang.parser.coercers.LongPCoercer;
import gw.lang.parser.coercers.LongPHighPriorityCoercer;
import gw.lang.parser.coercers.MetaTypeToClassCoercer;
import gw.lang.parser.coercers.NonWarningStringCoercer;
import gw.lang.parser.coercers.RuntimeCoercer;
import gw.lang.parser.coercers.ShortCoercer;
import gw.lang.parser.coercers.ShortHighPriorityCoercer;
import gw.lang.parser.coercers.ShortPCoercer;
import gw.lang.parser.coercers.ShortPHighPriorityCoercer;
import gw.lang.parser.coercers.StandardCoercer;
import gw.lang.parser.coercers.StringCoercer;
import gw.lang.parser.coercers.TypeVariableCoercer;
import gw.lang.parser.coercers.IMonitorLockCoercer;
import gw.lang.parser.exceptions.IncompatibleTypeException;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IHasJavaClass;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IPlaceholder;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableArrayType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuArrayClass;
import gw.lang.reflect.gs.IGosuArrayClassInstance;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.java.IJavaArrayType;
import gw.lang.reflect.java.IJavaType;
import gw.util.GosuExceptionUtil;
import gw.util.Pair;
import gw.util.concurrent.Cache;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class StandardCoercionManager extends BaseService implements ICoercionManager
{
  private static final DecimalFormat BIG_DECIMAL_FORMAT = new DecimalFormat();
  static {
    BIG_DECIMAL_FORMAT.setParseBigDecimal( true );
  }

  // LRUish cache of coercers
  private final TypeSystemAwareCache<Pair<IType, IType>, ICoercer> _coercerCache =
      TypeSystemAwareCache.make( "Coercer Cache", 1000,
                                 new Cache.MissHandler<Pair<IType, IType>, ICoercer>()
                                 {
                                   public final ICoercer load( Pair<IType, IType> key )
                                   {
                                     ICoercer coercer = findCoercerImpl( key.getFirst(), key.getSecond(), false );
                                     if( coercer == null )
                                     {
                                       return NullSentinalCoercer.instance();
                                     }
                                     else
                                     {
                                       return coercer;
                                     }
                                   }
                                 } )/*.logEveryNSeconds( 10, new SystemOutLogger( SystemOutLogger.LoggingLevel.INFO ) )*/;

  public final boolean canCoerce( IType lhsType, IType rhsType )
  {
    ICoercer iCoercer = findCoercer( lhsType, rhsType, false );
    return iCoercer != null;
  }

  private Object coerce( IType intrType, IType runtimeType, Object value )
  {
    ICoercer coercer = findCoercer( intrType, runtimeType, true );
    if( coercer != null )
    {
      return coercer.coerceValue( intrType, value );
    }
    return null;
  }

  private boolean hasPotentialLossOfPrecisionOrScale( IType lhsType, IType rhsType )
  {
    if( lhsType == IJavaType.pBYTE || lhsType == IJavaType.BYTE )
    {
      return rhsType != IJavaType.pBYTE && rhsType != IJavaType.BYTE;
    }
    else if( lhsType == IJavaType.pCHAR || lhsType == IJavaType.CHARACTER )
    {
      return rhsType != IJavaType.pBYTE && rhsType != IJavaType.BYTE &&
             rhsType != IJavaType.pCHAR && rhsType != IJavaType.CHARACTER;
    }
    else if( lhsType == IJavaType.pDOUBLE || lhsType == IJavaType.DOUBLE )
    {
      return rhsType != IJavaType.DOUBLE && !rhsType.isPrimitive() &&
             (IJavaType.BIGDECIMAL.isAssignableFrom( rhsType ) || IJavaType.BIGINTEGER.isAssignableFrom( rhsType ));
    }
    else if( lhsType == IJavaType.pFLOAT || lhsType == IJavaType.FLOAT )
    {
      return rhsType == IJavaType.pDOUBLE || rhsType == IJavaType.DOUBLE ||
             IJavaType.BIGDECIMAL.isAssignableFrom( rhsType ) || IJavaType.BIGINTEGER.isAssignableFrom( rhsType );
    }
    else if( lhsType == IJavaType.pINT || lhsType == IJavaType.INTEGER )
    {
      return rhsType != IJavaType.pINT && rhsType != IJavaType.INTEGER &&
             rhsType != IJavaType.pSHORT && rhsType != IJavaType.SHORT &&
             rhsType != IJavaType.pBYTE && rhsType != IJavaType.BYTE &&
             rhsType != IJavaType.pCHAR && rhsType != IJavaType.CHARACTER;
    }
    else if( lhsType == IJavaType.pLONG || lhsType == IJavaType.LONG )
    {
      return rhsType != IJavaType.pLONG && rhsType != IJavaType.LONG &&
             rhsType != IJavaType.pINT && rhsType != IJavaType.INTEGER &&
             rhsType != IJavaType.pSHORT && rhsType != IJavaType.SHORT &&
             rhsType != IJavaType.pBYTE && rhsType != IJavaType.BYTE &&
             rhsType != IJavaType.pCHAR && rhsType != IJavaType.CHARACTER;
    }
    else if( lhsType == IJavaType.pSHORT || lhsType == IJavaType.SHORT )
    {
      return rhsType != IJavaType.pSHORT && rhsType != IJavaType.SHORT &&
             rhsType != IJavaType.pBYTE && rhsType != IJavaType.BYTE &&
             rhsType != IJavaType.pCHAR && rhsType != IJavaType.CHARACTER;
    }
    else if( IJavaType.BIGINTEGER.isAssignableFrom( lhsType ) )
    {
      return rhsType != IJavaType.BIGINTEGER && hasPotentialLossOfPrecisionOrScale( IJavaType.LONG, rhsType );
    }
    return false;
  }

  public final ICoercer findCoercer( IType lhsType, IType rhsType, boolean runtime )
  {
    if( runtime )
    {
      return findCoercerImpl( lhsType, rhsType, runtime );
    }
    else
    {
      @SuppressWarnings({"unchecked"})
      ICoercer iCoercer = _coercerCache.get( new Pair( lhsType, rhsType ) );
      if( iCoercer == NullSentinalCoercer.instance() )
      {
        return null;
      }
      else
      {
        return iCoercer;
      }
    }
  }

  private ICoercer findCoercerImpl( IType lhsType, IType rhsType, boolean runtime )
  {
    ICoercer coercer = getCoercerInternal( lhsType, rhsType, runtime );
    if( coercer != null )
    {
      return coercer;
    }

    //Look at interfaces on rhsType for coercions
    List<? extends IType> interfaces = rhsType.getInterfaces();
    for ( IType anInterface1 : interfaces ) {
      coercer = findCoercer(lhsType, anInterface1, runtime);
      if (coercer != null) {
        return coercer;
      }
    }

    //Recurse to the superclass of rhsType for coercions
    if( rhsType.getSupertype() == null || isPrimitiveOrBoxed( lhsType ) )
    {
      return null;
    }
    else
    {
      return findCoercer( lhsType, rhsType.getSupertype(), runtime );
    }
  }

  /**
   * Returns a coercer from values of rhsType to values of lhsType if one exists.
   * I tried to write a reasonable spec in the comments below that indicate exactly
   * what should coerce to what.
   *
   * @param lhsType the type to coerce to
   * @param rhsType the type to coerce from
   * @param runtime true if the coercion is happening at runtime rather than compile time
   *                (note: This param should go away as we store the coercions on the parsed elements, rather than calling into the
   *                coercion manager)
   *
   * @return a coercer from the lhsType to the rhsType, or null if no such coercer exists or is needed
   */
  protected ICoercer getCoercerInternal( IType lhsType, IType rhsType, boolean runtime )
  {
    //=============================================================================
    //  Anything can be coerced to a string
    //=============================================================================
    if( IJavaType.STRING == lhsType )
    {
      if( IJavaType.pCHAR.equals( rhsType ) || IJavaType.CHARACTER.equals( rhsType ) )
      {
        return NonWarningStringCoercer.instance();
      }
      else
      {
        return StringCoercer.instance();
      }
    }

    //=============================================================================
    //  All primitives and boxed types inter-coerce, except null to true primitives
    //=============================================================================
    if( lhsType.isPrimitive() && rhsType.equals( IJavaType.pVOID ) )
    {
      return null;
    }
    if( isPrimitiveOrBoxed( lhsType ) && isPrimitiveOrBoxed( rhsType ) )
    {
      if( TypeSystem.isBoxedTypeFor( lhsType, rhsType ) ||
          TypeSystem.isBoxedTypeFor( rhsType, lhsType ) )
      {
        return getHighPriorityPrimitiveOrBoxedConverter( lhsType );
      }
      return getPrimitiveOrBoxedConverter( lhsType );
    }

    //=============================================================================
    //  Primitives coerce to things their boxed type is assignable to
    //=============================================================================
    if( rhsType.isPrimitive() )
    {
      if( lhsType.isAssignableFrom( TypeSystem.getBoxType( rhsType ) ) )
      {
        return getPrimitiveOrBoxedConverter( rhsType );
      }
    }

    //=============================================================================
    //  IMonitorLock supports java-style synchronization
    //=============================================================================
    if( !rhsType.isPrimitive() && "gw.lang.IMonitorLock".equals( lhsType.getName() ) )
    {
      return IMonitorLockCoercer.instance();
    }

    //=============================================================================
    // Class<T> <- Meta<T' instanceof JavaType>
    //=============================================================================
    if( (IJavaType.CLASS.equals( lhsType.getGenericType() ) &&
         rhsType instanceof IMetaType && ((IMetaType)rhsType).getType() instanceof IHasJavaClass) )
    {
      //TODO: refuse to coerce if T != T'
      return MetaTypeToClassCoercer.instance();
    }

    //=============================================================================
    // Numeric type unification
    //=============================================================================
    if( TypeSystem.isNumericType( lhsType ) && TypeSystem.isNumericType( rhsType ) )
    {
      //=============================================================================
      // All numeric values can be down-coerced to the primitives and boxed types
      //=============================================================================
      if( lhsType.isPrimitive() || isBoxed( lhsType ) )
      {
        return getPrimitiveOrBoxedConverter( lhsType );
      }

      //=============================================================================
      // All numeric values can be coerced to BigDecimal
      //=============================================================================
      if( lhsType.equals( IJavaType.BIGDECIMAL ))
      {
        return BigDecimalCoercer.instance();
      }

      //=============================================================================
      // All numeric values can be coerced to BigInteger
      //=============================================================================
      if( lhsType.equals( IJavaType.BIGINTEGER ))
      {
        if( hasPotentialLossOfPrecisionOrScale( lhsType, rhsType ) )
        {
          return BigIntegerCoercer.instance();
        }
        else
        {
          return BigIntegerCoercer.instance();
        }
      }
    }

    //=============================================================================
    // JavaType interface <- compatible block
    //=============================================================================
    if( rhsType instanceof IFunctionType && lhsType.isInterface() )
    {
      IFunctionType rhsFunctionType = (IFunctionType)rhsType;
      IFunctionType lhsFunctionType = FunctionToInterfaceCoercer.getRepresentativeFunctionType( lhsType );
      if( lhsFunctionType != null )
      {
        if( lhsFunctionType.isAssignableFrom( rhsFunctionType ) )
        {
          return FunctionToInterfaceCoercer.instance();
        }
        else
        {
          if( lhsFunctionType.areParamsCompatible( rhsFunctionType ) )
          {
            ICoercer coercer = findCoercer( lhsFunctionType.getReturnType(), rhsFunctionType.getReturnType(), runtime );
            if( coercer != null )
            {
              return FunctionToInterfaceCoercer.instance();
            }
          }
        }
      }
    }

    //=============================================================================
    // Coerce synthetic block classes to function types
    //=============================================================================
    if( lhsType instanceof IFunctionType && rhsType instanceof IBlockClass )
    {
      if( lhsType.isAssignableFrom( ((IBlockClass)rhsType).getBlockType() ) )
      {
        return IdentityCoercer.instance();
      }
    }
    
    //=============================================================================
    // compatible block <- JavaType interface
    //=============================================================================
    if( lhsType instanceof IFunctionType && rhsType.isInterface() &&
        FunctionFromInterfaceCoercer.areTypesCompatible( (IFunctionType)lhsType, rhsType ) )
    {
      return FunctionFromInterfaceCoercer.instance();
    }

    //=============================================================================
    // Coerce block types that are coercable in return values and contravariant in arg types
    //=============================================================================
    if( lhsType instanceof IBlockType && rhsType instanceof IBlockType )
    {
      IBlockType lBlock = (IBlockType)lhsType;
      IBlockType rBlock = (IBlockType)rhsType;
      if( lBlock.areParamsCompatible( rBlock ) )
      {
        IType leftType = lBlock.getReturnType();
        IType rightType = rBlock.getReturnType();
        if( rightType != IJavaType.pVOID )
        {
          ICoercer iCoercer = findCoercer( leftType, rightType, runtime );
          if( iCoercer != null && !coercionRequiresWarningIfImplicit( leftType, rightType ))
          {
            return BlockCoercer.instance();
          }
        }
      }
    }

    return null;
  }

  public boolean isPrimitiveOrBoxed( IType lhsType )
  {
    return lhsType.isPrimitive() || isBoxed( lhsType );
  }

  public static boolean isBoxed( IType lhsType )
  {
    return lhsType == IJavaType.BOOLEAN
           || lhsType == IJavaType.BYTE
           || lhsType == IJavaType.CHARACTER
           || lhsType == IJavaType.DOUBLE
           || lhsType == IJavaType.FLOAT
           || lhsType == IJavaType.INTEGER
           || lhsType == IJavaType.LONG
           || lhsType == IJavaType.SHORT;
  }

  protected ICoercer getPrimitiveOrBoxedConverter( IType type )
  {
    if( type == IJavaType.pBOOLEAN )
    {
      return BooleanPCoercer.instance();
    }
    else if( type == IJavaType.BOOLEAN )
    {
      return BooleanCoercer.instance();
    }
    else if( type == IJavaType.pBYTE )
    {
      return BytePCoercer.instance();
    }
    else if( type == IJavaType.BYTE )
    {
      return ByteCoercer.instance();
    }
    else if( type == IJavaType.pCHAR )
    {
      return CharPCoercer.instance();
    }
    else if( type == IJavaType.CHARACTER )
    {
      return CharCoercer.instance();
    }
    else if( type == IJavaType.pDOUBLE )
    {
      return DoublePCoercer.instance();
    }
    else if( type == IJavaType.DOUBLE )
    {
      return DoubleCoercer.instance();
    }
    else if( type == IJavaType.pFLOAT )
    {
      return FloatPCoercer.instance();
    }
    else if( type == IJavaType.FLOAT )
    {
      return FloatCoercer.instance();
    }
    else if( type == IJavaType.pINT )
    {
      return IntPCoercer.instance();
    }
    else if( type == IJavaType.INTEGER )
    {
      return IntCoercer.instance();
    }
    else if( type == IJavaType.pLONG )
    {
      return LongPCoercer.instance();
    }
    else if( type == IJavaType.LONG )
    {
      return LongCoercer.instance();
    }
    else if( type == IJavaType.pSHORT )
    {
      return ShortPCoercer.instance();
    }
    else if( type == IJavaType.SHORT )
    {
      return ShortCoercer.instance();
    }
    else if( type == IJavaType.pVOID )
    {
      return IdentityCoercer.instance();
    }
    else
    {
      return null;
    }
  }

  protected ICoercer getHighPriorityPrimitiveOrBoxedConverter( IType type )
  {
    if( type == IJavaType.pBOOLEAN )
    {
      return BooleanPHighPriorityCoercer.instance();
    }
    else if( type == IJavaType.BOOLEAN )
    {
      return BooleanHighPriorityCoercer.instance();
    }
    else if( type == IJavaType.pBYTE )
    {
      return BytePHighPriorityCoercer.instance();
    }
    else if( type == IJavaType.BYTE )
    {
      return ByteHighPriorityCoercer.instance();
    }
    else if( type == IJavaType.pCHAR )
    {
      return CharPHighPriorityCoercer.instance();
    }
    else if( type == IJavaType.CHARACTER )
    {
      return CharHighPriorityCoercer.instance();
    }
    else if( type == IJavaType.pDOUBLE )
    {
      return DoublePHighPriorityCoercer.instance();
    }
    else if( type == IJavaType.DOUBLE )
    {
      return DoubleHighPriorityCoercer.instance();
    }
    else if( type == IJavaType.pFLOAT )
    {
      return FloatPHighPriorityCoercer.instance();
    }
    else if( type == IJavaType.FLOAT )
    {
      return FloatHighPriorityCoercer.instance();
    }
    else if( type == IJavaType.pINT )
    {
      return IntPHighPriorityCoercer.instance();
    }
    else if( type == IJavaType.INTEGER )
    {
      return IntHighPriorityCoercer.instance();
    }
    else if( type == IJavaType.pLONG )
    {
      return LongPHighPriorityCoercer.instance();
    }
    else if( type == IJavaType.LONG )
    {
      return LongHighPriorityCoercer.instance();
    }
    else if( type == IJavaType.pSHORT )
    {
      return ShortPHighPriorityCoercer.instance();
    }
    else if( type == IJavaType.SHORT )
    {
      return ShortHighPriorityCoercer.instance();
    }
    else if( type == IJavaType.pVOID )
    {
      return IdentityCoercer.instance();
    }
    else
    {
      return null;
    }
  }

  protected void doInit()
  {
    //nothing
  }

  public IType verifyTypesComparable( IType lhsType, IType rhsType, boolean bBiDirectional ) throws ParseException
  {
    return verifyTypesComparable( lhsType, rhsType, bBiDirectional, null );
  }

  public IType verifyTypesComparable( IType lhsType, IType rhsType, boolean bBiDirectional, IFullParserState parserState ) throws ParseException
  {

    //==================================================================================
    // Upcasting
    //==================================================================================
    if( lhsType == rhsType )
    {
      return lhsType;
    }
    if( lhsType.equals( rhsType ) )
    {
      return lhsType;
    }
    if( lhsType.isAssignableFrom( rhsType ) )
    {
      return lhsType;
    }

    //==================================================================================
    // null/void confusion (see http://jira/jira/browse/PL-12766)
    //==================================================================================
    if( IJavaType.pVOID.equals( rhsType ) )
    {
      return lhsType;
    }
    if( IJavaType.pVOID.equals( lhsType ) )
    {
      return rhsType;
    }

    //==================================================================================
    // Error type handling
    //==================================================================================
    if( lhsType instanceof IErrorType)
    {
      return lhsType;
    }
    if( rhsType instanceof IErrorType )
    {
      return rhsType;
    }

    //==================================================================================
    // IPlaceholderType type handling
    //==================================================================================
    if( (lhsType instanceof IPlaceholder && ((IPlaceholder)lhsType).isPlaceholder()) ||
        (rhsType instanceof IPlaceholder && ((IPlaceholder)rhsType).isPlaceholder()) )
    {
      return lhsType;
    }

    //==================================================================================
    //Covariant arrays
    //==================================================================================
    if( lhsType.isArray() && rhsType.isArray() )
    {
      // Note an array of primitives and an array of non-primitives are never assignable
      if( lhsType.getComponentType().isPrimitive() == rhsType.getComponentType().isPrimitive() &&
          lhsType.getComponentType().isAssignableFrom( rhsType.getComponentType() ) )
      {
        return lhsType;
      }
    }

    //==================================================================================
    // Downcasting
    //==================================================================================
    if( bBiDirectional )
    {
      if( rhsType.isAssignableFrom( lhsType ) )
      {
        return lhsType;
      }
      if( lhsType.isArray() && rhsType.isArray() )
      {
        if( rhsType.getComponentType().isAssignableFrom( lhsType.getComponentType() ) )
        {
          return lhsType;
        }
      }
    }

    //==================================================================================
    // Coercion
    //==================================================================================
    if( canCoerce( lhsType, rhsType ) )
    {
      return lhsType;
    }

    if( bBiDirectional )
    {
      if( canCoerce( rhsType, lhsType ) )
      {
        return rhsType;
      }
    }

    String strLhs = TypeSystem.getNameWithQualifiedTypeVariables( lhsType );
    String strRhs = TypeSystem.getNameWithQualifiedTypeVariables( rhsType );
    throw new ParseException( parserState, lhsType, Res.MSG_TYPE_MISMATCH, strLhs, strRhs );
  }

  public boolean coercionRequiresWarningIfImplicit( IType lhsType, IType rhsType )
  {

    //==================================================================================
    // Upcasting
    //==================================================================================
    if( lhsType == rhsType )
    {
      return false;
    }
    if( lhsType.equals( rhsType ) )
    {
      return false;
    }
    if( lhsType.isAssignableFrom( rhsType ) )
    {
      return false;
    }
    if ( rhsType.isPrimitive() && lhsType.isAssignableFrom( TypeSystem.getBoxType( rhsType ) ) )
    {
      return false;
    }

    //==================================================================================
    // null/void confusion (see http://jira/jira/browse/PL-12766)
    //==================================================================================
    if( IJavaType.pVOID.equals( rhsType ) )
    {
      return false;
    }
    
    //==================================================================================
    // Error type handling
    //==================================================================================
    if( lhsType instanceof IErrorType )
    {
      return false;
    }
    if( rhsType instanceof IErrorType )
    {
      return false;
    }

    //==================================================================================
    // IPlaceholderType type handling
    //==================================================================================
    if( (lhsType instanceof IPlaceholder && ((IPlaceholder)lhsType).isPlaceholder()) ||
        (rhsType instanceof IPlaceholder && ((IPlaceholder)rhsType).isPlaceholder()) )
    {
      return false;
    }

    //==================================================================================
    //Covariant arrays (java semantics, which are wrong)
    //
    // (Five years later, let me totally disagree with my former self.  Java array semantics are not only right,
    //  we've decided to adopt them for generics.  Worse is better.)
    //==================================================================================
    if( lhsType.isArray() && rhsType.isArray() )
    {
      if( lhsType.getComponentType().isAssignableFrom( rhsType.getComponentType() ) )
      {
        return false;
      }
    }

    //==================================================================================
    // Coercion
    //==================================================================================
    if( TypeSystem.isNumericType( lhsType ) &&
        TypeSystem.isNumericType( rhsType ) )
    {
      return hasPotentialLossOfPrecisionOrScale( lhsType, rhsType );
    }
    else
    {
      if( TypeSystem.isBoxedTypeFor( lhsType, rhsType ) ||
               TypeSystem.isBoxedTypeFor( rhsType, lhsType ) )
      {
        return false;
      }
      else
      {
        ICoercer iCoercer = findCoercer( lhsType, rhsType, false );
        return iCoercer != null && iCoercer.isExplicitCoercion();
      }
    }
  }

  /**
   * Given a value and a target Class, return a compatible value via the target Class.
   */
  public final Object convertValue(Object value, IType intrType)
  {
    //==================================================================================
    // Null handling
    //==================================================================================
    if( intrType == null )
    {
      return null;
    }

    //## todo: This is a horrible hack
    //## todo: The only known case where this is necessary is when we have an array of parameterized java type e.g., List<String>[]
    intrType = getBoundingTypeOfTypeVariable( intrType );

    if( value == null )
    {
      return intrType.isPrimitive() ? convertNullAsPrimitive( intrType, true ) : null;
    }

    IType runtimeType = TypeSystem.getFromObject( value );

    //==================================================================================
    // IPlaceholder type handling
    //==================================================================================
    if( (intrType instanceof IPlaceholder && ((IPlaceholder)intrType).isPlaceholder()) ||
        (runtimeType instanceof IPlaceholder && ((IPlaceholder)runtimeType).isPlaceholder()) )
    {
      return value;
    }

    //==================================================================================
    // Runtime polymorphism (with java array semantics)
    //==================================================================================
    if( intrType == runtimeType )
    {
      return value;
    }
    if( intrType.equals( runtimeType ) )
    {
      return value;
    }
    if( intrType.isAssignableFrom( runtimeType ) )
    {
      value = extractObjectArray( intrType, value );
      return value;
    }
    if( intrType.isArray() && runtimeType.isArray() )
    {
      if( intrType.getComponentType().isAssignableFrom( runtimeType.getComponentType() ) )
      {
        value = extractObjectArray( intrType, value );
        return value;
      }
      else if( intrType instanceof IGosuArrayClass &&
               value instanceof IGosuObject[] )
      {
        return value;
      }
    }
    // Proxy coercion. The proxy class generated for Java classes is not a super type of the Gosu class.
    // The following check allows coercion of the Gosu class to the Gosu proxy class needed for the super call.
    if( intrType instanceof IJavaType &&
        IGosuClass.ProxyUtil.isProxy( intrType ) &&
        runtimeType instanceof IGosuClass &&
        intrType.getSupertype() != null &&
        intrType.getSupertype().isAssignableFrom( runtimeType ) )
    {
      return value;
    }

    // Check Java world types
    //noinspection deprecation
    if( intrType instanceof IJavaType &&
        ((IJavaType)intrType).getIntrinsicClass().isAssignableFrom( value.getClass() ) )
    {
      return value;
    }

    //==================================================================================
    // Coercion
    //==================================================================================
    Object convertedValue = coerce( intrType, runtimeType, value );
    if( convertedValue != null )
    {
      return convertedValue;
    }
    else
    {
      //If the null arose from an actual coercion, return it
      if( canCoerce( intrType, runtimeType ) )
      {
        return convertedValue;
      }
      else
      {
        //otherwise, return the value itself uncoerced (See comment above)
        if( !runtimeType.isArray() )
        {
          StringBuilder sb = new StringBuilder( "Cannot coerce " + runtimeType.getName() + " to " + intrType.getName() );
          if ( runtimeType instanceof IJavaType )
          {
            Class backingClass = ( (IJavaType) runtimeType ).getBackingClass();
            if ( Proxy.isProxyClass( backingClass ) )
            {
              // add additional Proxy-related information which might be useful
              sb.append( '\n' );
              sb.append( "  This proxy instance has the following attributes:\n" );
              sb.append( "    Implemented Interfaces: " );
              int idx = 0;
              for ( Class ifaceClass : backingClass.getInterfaces() )
              {
                if ( idx++ > 0 )
                {
                  sb.append( ", " );
                }
                sb.append( ifaceClass.getName() );
              }
              if ( idx == 0 )
              {
                sb.append( "<none>" );
              }
              sb.append( '\n' );
              sb.append( "    Invocation Handler Class: " );
              sb.append( Proxy.getInvocationHandler( value ).getClass().getName() );
            }
          }
          throw new IncompatibleTypeException( sb.toString() );
        }
        return value;
      }
    }
  }

  private IType getBoundingTypeOfTypeVariable( IType intrType )
  {
    int i = 0;
    while( intrType instanceof ITypeVariableArrayType )
    {
      i++;
      intrType = intrType.getComponentType();
    }
    if( intrType instanceof ITypeVariableType )
    {
      intrType = ((ITypeVariableType)intrType).getBoundingType();
      while( i-- > 0 )
      {
        intrType = intrType.getArrayType();
      }
    }
    return intrType;
  }

  private Object extractObjectArray( IType intrType, Object value )
  {
    if( intrType.isArray() &&
        intrType instanceof IJavaArrayType &&
        value instanceof IGosuArrayClassInstance )
    {
      value = ((IGosuArrayClassInstance)value).getObjectArray();
    }
    return value;
  }

  public Object convertNullAsPrimitive( IType intrType, boolean isForBoxing )
  {
    if( intrType == null )
    {
      return null;
    }

    if( !intrType.isPrimitive() )
    {
      throw GosuShop.createEvaluationException( intrType.getName() + " is not a primitive type." );
    }

    if( intrType == IJavaType.pBYTE )
    {
      return (byte) 0;
    }
    if( intrType == IJavaType.pCHAR )
    {
      return '\0';
    }
    if( intrType == IJavaType.pDOUBLE )
    {
      return isForBoxing ? IGosuParser.NaN : (double) 0;
    }
    if( intrType == IJavaType.pFLOAT )
    {
      return isForBoxing ? Float.NaN : (float) 0;
    }
    if( intrType == IJavaType.pINT )
    {
      return 0;
    }
    if( intrType == IJavaType.pLONG )
    {
      return (long) 0;
    }
    if( intrType == IJavaType.pSHORT )
    {
      return (short) 0;
    }
    if( intrType == IJavaType.pBOOLEAN )
    {
      return Boolean.FALSE;
    }
    if( intrType == IJavaType.pVOID )
    {
      return null;
    }
    throw GosuShop.createEvaluationException( "Unexpected primitive type: " + intrType.getName() );
  }

  public ICoercer resolveCoercerStatically( IType typeToCoerceTo, IType typeToCoerceFrom )
  {
    if( typeToCoerceTo == null || typeToCoerceFrom == null )
    {
      return null;
    }
    else if( typeToCoerceTo == typeToCoerceFrom )
    {
      return null;
    }
    else if( typeToCoerceTo.equals( typeToCoerceFrom ) )
    {
      return null;
    }
    else if( typeToCoerceTo instanceof IErrorType || typeToCoerceFrom instanceof IErrorType )
    {
      return null;
    }
    else if( typeToCoerceTo instanceof ITypeVariableArrayType )
    {
      return RuntimeCoercer.instance();
    }
    else if( typeToCoerceTo instanceof ITypeVariableType )
    {
      return TypeVariableCoercer.instance();
    }
    else if( typeToCoerceTo.isAssignableFrom( typeToCoerceFrom ) )
    {
      return null;
    }
    else
    {
      ICoercer coercerInternal = findCoercerImpl( typeToCoerceTo, typeToCoerceFrom, false );
      if( coercerInternal == null )
      {
        if( typeToCoerceFrom.isAssignableFrom( typeToCoerceTo ) && !IJavaType.pVOID.equals( typeToCoerceTo ) )
        {
          if( areJavaClassesAndAreNotAssignable( typeToCoerceTo, typeToCoerceFrom ) )
          {
            return RuntimeCoercer.instance();
          }
          return identityOrRuntime( typeToCoerceTo, typeToCoerceFrom );
        }
        else if( (typeToCoerceFrom.isInterface() || typeToCoerceTo.isInterface()) &&
                 !typeToCoerceFrom.isPrimitive() && !typeToCoerceTo.isPrimitive() )
        {
          return identityOrRuntime( typeToCoerceTo, typeToCoerceFrom );
        }
      }
      return coercerInternal;
    }
  }

  private boolean areJavaClassesAndAreNotAssignable( IType typeToCoerceTo, IType typeToCoerceFrom )
  {
    if( typeToCoerceFrom instanceof IJavaType && typeToCoerceTo instanceof IJavaType )
    {
      if( !((IJavaType)typeToCoerceFrom).getClassInfo().isAssignableFrom( ((IJavaType)typeToCoerceTo).getClassInfo() ) )
      {
        return true;
      }
    }
    return false;
  }

  private ICoercer identityOrRuntime( IType typeToCoerceTo, IType typeToCoerceFrom )
  {
    if( TypeSystem.isBytecodeType( typeToCoerceFrom ) &&
        TypeSystem.isBytecodeType( typeToCoerceTo ) )
    {
      return IdentityCoercer.instance(); // (perf) class-to-class downcast can use checkcast bytecode
    }
    return RuntimeCoercer.instance();
  }


  public Double makeDoubleFrom( Object obj )
  {
    if( obj == null )
    {
      return null;
    }

    if( obj instanceof IDimension)
    {
      obj = ((IDimension)obj).toNumber();
    }

    if( obj instanceof Double )
    {
      return (Double)obj;
    }

    double d;

    if( obj instanceof Number )
    {
      d = ((Number)obj).doubleValue();
    }
    else if( obj instanceof Boolean )
    {
      return (Boolean) obj ? IGosuParser.ONE : IGosuParser.ZERO;
    }
    else if( obj instanceof Date )
    {
      return (double)((Date)obj).getTime();
    }
    else if( obj instanceof Character )
    {
      return (double) ((Character) obj).charValue();
    }
    else if( CommonServices.getCoercionManager().canCoerce( IJavaType.NUMBER,
                                                            TypeSystem.getFromObject( obj ) ) )
    {
      Number num = (Number)CommonServices.getCoercionManager().convertValue( obj, IJavaType.NUMBER );
      return num.doubleValue();
    }
    else
    {
      String strValue = obj.toString();
      return makeDoubleFrom( parseNumber( strValue ) );
    }

    if( d >= 0D && d <= 9D )
    {
      int i = (int)d;

      if( ((double)i == d) && (i >= 0 && i <= 9) )
      {
        return IGosuParser.DOUBLE_DIGITS[i];
      }
    }

    return d;
  }

  public int makePrimitiveIntegerFrom( Object obj )
  {
    if( obj == null )
    {
      return 0;
    }
    else
    {
      return makeIntegerFrom( obj );
    }
  }
  public Integer makeIntegerFrom( Object obj )
  {
    if( obj == null )
    {
      return null;
    }

    if( obj instanceof IDimension )
    {
      obj = ((IDimension)obj).toNumber();
    }

    if( obj instanceof Integer )
    {
      return (Integer)obj;
    }

    if( obj instanceof Number )
    {
      return ( (Number) obj ).intValue();
    }
    else if( obj instanceof Boolean )
    {
      return (Boolean) obj
             ? IGosuParser.ONE.intValue()
             : IGosuParser.ZERO.intValue();
    }
    else if( obj instanceof Date )
    {
      return (int) ((Date) obj).getTime();
    }
    else if( obj instanceof Character )
    {
      return (int) ((Character) obj).charValue();
    }
    else if( CommonServices.getCoercionManager().canCoerce( IJavaType.NUMBER,
                                                            TypeSystem.getFromObject( obj ) ) )
    {
      Number num = (Number)CommonServices.getCoercionManager().convertValue( obj, IJavaType.NUMBER );
      return num.intValue();
    }
    else
    {
      String strValue = obj.toString();
      return makeIntegerFrom( parseNumber( strValue ) );
    }
  }

  public long makePrimitiveLongFrom( Object obj )
  {
    if( obj == null )
    {
      return 0;
    }
    else
    {
      return makeLongFrom( obj );
    }
  }

  public Long makeLongFrom( Object obj )
  {
    if( obj == null )
    {
      return null;
    }

    if( obj instanceof IDimension )
    {
      obj = ((IDimension)obj).toNumber();
    }

    if( obj instanceof Long )
    {
      return (Long)obj;
    }

    if( obj instanceof Number )
    {
      return ((Number)obj).longValue();
    }
    else if( obj instanceof Boolean )
    {
      return (Boolean) obj
             ? IGosuParser.ONE.longValue()
             : IGosuParser.ZERO.longValue();
    }
    else if( obj instanceof Date )
    {
      return ((Date)obj).getTime();
    }
    else if( obj instanceof Character )
    {
      return (long)((Character)obj).charValue();
    }
    else if( CommonServices.getCoercionManager().canCoerce( IJavaType.NUMBER,
                                                            TypeSystem.getFromObject( obj ) ) )
    {
      Number num = (Number)CommonServices.getCoercionManager().convertValue( obj , IJavaType.NUMBER );
      return num.longValue();
    }
    else
    {
      String strValue = obj.toString();
      return makeLongFrom( parseNumber( strValue ) );
    }
  }

  public BigDecimal makeBigDecimalFrom( Object obj )
  {
    if( obj == null )
    {
      return null;
    }

    if( obj instanceof IDimension )
    {
      obj = ((IDimension)obj).toNumber();
    }

    if( obj instanceof BigDecimal )
    {
      return (BigDecimal)obj;
    }

    if( obj instanceof String )
    {
      try
      {
        return (BigDecimal)BIG_DECIMAL_FORMAT.parse( obj.toString() );
      }
      catch( java.text.ParseException e )
      {
        throw GosuExceptionUtil.convertToRuntimeException( e );
      }
    }

    if( obj instanceof Integer )
    {
      return new BigDecimal( (Integer)obj );
    }
    else if( obj instanceof BigInteger )
    {
      return new BigDecimal( (BigInteger)obj );
    }
    else if( obj instanceof Long )
    {
      return new BigDecimal( (Long)obj );
    }
    else if (obj instanceof Short) {
      return new BigDecimal(((Short) obj).intValue());
    }
    else if (obj instanceof Byte) {
      return new BigDecimal(((Byte) obj).intValue());
    }
    else if (obj instanceof Float) {
      // Convert a float directly to a BigDecimal via the String value; don't
      // up-convert it to a double first, since converting a double can be lossy
      return new BigDecimal( obj.toString());
    }
    else if( obj instanceof Number )
    {
      // Make a double from any type of number that we haven't yet dealt with
      Double d = makeDoubleFrom( obj );
      return new BigDecimal( d.toString() );
    }
    else if( obj instanceof Boolean )
    {
      return (Boolean) obj ? BigDecimal.ONE : BigDecimal.ZERO;
    }
    else if( obj instanceof Date )
    {
      return new BigDecimal( ((Date)obj).getTime() );
    }
    else if( obj instanceof Character )
    {
      return new BigDecimal( (Character) obj );
    }
    else if( CommonServices.getCoercionManager().canCoerce( IJavaType.NUMBER, TypeSystem.getFromObject( obj ) ) )
    {
      Number num = (Number)CommonServices.getCoercionManager().convertValue( obj, IJavaType.NUMBER );
      return makeBigDecimalFrom( num );
    }
    else
    {
      // As a last resort, convert it to a double and then convert that to a big decimal
      Double d = makeDoubleFrom( obj );
      return new BigDecimal( d.toString() );
    }
  }

  public BigInteger makeBigIntegerFrom( Object obj )
  {
    if( obj == null )
    {
      return null;
    }

    if( obj instanceof IDimension )
    {
      obj = ((IDimension)obj).toNumber();
    }

    if( obj instanceof String )
    {
      String strValue = (String)obj;
      return makeBigIntegerFrom( parseNumber( strValue ) );
    }

    BigDecimal d = makeBigDecimalFrom( obj );
    return d.toBigInteger();
  }

  public double makePrimitiveDoubleFrom( Object obj )
  {
    if( obj == null )
    {
      return Double.NaN;
    }
    else
    {
      return makeDoubleFrom( obj );
    }
  }

  public Float makeFloatFrom( Object obj )
  {
    if( obj == null )
    {
      return Float.NaN;
    }

    if( obj instanceof IDimension )
    {
      obj = ((IDimension)obj).toNumber();
    }

    if( obj instanceof Number )
    {
      return ((Number)obj).floatValue();
    }
    else if( obj instanceof Boolean )
    {
      return (Boolean) obj ? 1f : 0f;
    }
    else if( obj instanceof Date )
    {
      return (float) ((Date) obj).getTime();
    }
    else if( obj instanceof Character )
    {
      return (float) ((Character) obj).charValue();
    }
    else
    {
      try
      {
        return Float.parseFloat( obj.toString() );
      }
      catch( Throwable t )
      {
        // Nonparsable floating point numbers have a NaN value (a la JavaScript)
        return Float.NaN;
      }
    }
  }

  public float makePrimitiveFloatFrom( Object obj )
  {
    if( obj == null )
    {
      return Float.NaN;
    }
    else
    {
      return makeFloatFrom( obj );
    }
  }

  public String makeStringFrom( Object obj )
  {
    return obj == null ? null : obj.toString();
  }

  /**
   * @return A Boolean for an arbitrary object.
   */
  public boolean makePrimitiveBooleanFrom( Object obj )
  {
    //noinspection SimplifiableIfStatement
    if( obj == null )
    {
      return false;
    }
    else
    {
      return Boolean.TRUE.equals( makeBooleanFrom( obj ) );
    }
  }

  public Boolean makeBooleanFrom( Object obj )
  {
    if( obj == null )
    {
      return null;
    }

    if( obj instanceof IDimension )
    {
      obj = ((IDimension)obj).toNumber();
    }

    if( obj instanceof Boolean )
    {
      return (Boolean)obj;
    }

    if( obj instanceof String )
    {
      return Boolean.valueOf( (String)obj );
    }

    if( obj instanceof Number )
    {
      return ((Number)obj).doubleValue() == 0D ? Boolean.FALSE : Boolean.TRUE;
    }

    return Boolean.valueOf( obj.toString() );
  }

  /**
   * Returns a new Date instance representing the object.
   */
  public Date makeDateFrom( Object obj )
  {
    if( obj == null )
    {
      return null;
    }

    if( obj instanceof IDimension )
    {
      obj = ((IDimension)obj).toNumber();
    }

    if( obj instanceof Date )
    {
      return (Date)obj;
    }

    if( obj instanceof Number )
    {
      return new Date( ((Number)obj).longValue() );
    }

    if( obj instanceof Calendar)
    {
      return ((Calendar)obj).getTime();
    }

    if( !(obj instanceof String) )
    {
      obj = obj.toString();
    }

    try
    {
      return parseDateTime( (String)obj );
    }
    catch( Exception e )
    {
      //e.printStackTrace();
    }

    return null;
  }

  @Override
  public boolean isDateTime( String str ) throws java.text.ParseException
  {
    return parseDateTime( str ) != null;
  }

  /**
   * Produce a date from a string using standard DateFormat parsing.
   */
  public Date parseDateTime( String str ) throws java.text.ParseException
  {
    if( str == null )
    {
      return null;
    }

    // Use canonical format that doesn't rely on locale so that code written in one locale will parse in another

    String[] typeNames = { "gw.xml.xsd.types.XSDDateTime", "gw.xml.xsd.types.XSDDate", "gw.xml.xsd.types.XSDTime" };

    java.text.ParseException firstException = null;

    for ( String typeName : typeNames )
    {
      try
      {
        IType xsdDateTimeType = TypeSystem.getByFullName( typeName );
        Object xsdDateTime = xsdDateTimeType.getTypeInfo().getConstructor( IJavaType.STRING ).getConstructor().newInstance( str );
        Calendar cal = (Calendar) xsdDateTimeType.getTypeInfo().getMethod("toCalendar", TypeSystem.get(TimeZone.class)).getCallHandler().handleCall(xsdDateTime, TimeZone.getDefault());
        return cal.getTime();
      }
      catch ( Exception ex )
      {
        Throwable pe = ex;
        while ( pe != null )
        {
          if ( pe instanceof java.text.ParseException )
          {
            if ( firstException == null )
            {
              firstException = (java.text.ParseException) pe;
            }
            break;
          }
          pe = pe.getCause();
        }
        if ( pe == null )
        {
          // ParseException not found
          throw GosuExceptionUtil.forceThrow( ex );
        }
      }
    }
    assert firstException != null;
    throw firstException;
  }

  /**
   * Convert a string to an array of specified type.
   * @param strValue the string to convert
   * @param intrType the array component type
   * @return the string converted to an array
   */
  public static Object makeArrayFromString( String strValue, IType intrType )
  {
    if( IJavaType.pCHAR == intrType )
    {
      return strValue.toCharArray();
    }

    if( IJavaType.CHARACTER == intrType )
    {
      Character[] characters = new Character[strValue.length()];
      for( int i = 0; i < characters.length; i++ )
      {
        characters[i] = strValue.charAt(i);
      }

      return characters;
    }

    if( IJavaType.STRING == intrType )
    {
      String[] strings = new String[strValue.length()];
      for( int i = 0; i < strings.length; i++ )
      {
        strings[i] = String.valueOf( strValue.charAt( i ) );
      }

      return strings;
    }

    throw GosuShop.createEvaluationException( "The type, " + intrType.getName() + ", is not supported as a coercible component type to a String array." );
  }

  public String formatDate( Date value, String strFormat )
  {
    DateFormat df = new SimpleDateFormat( strFormat );
    return df.format( value );
  }

  public String formatTime( Date value, String strFormat )
  {
    DateFormat df = new SimpleDateFormat( strFormat );
    return df.format( value );
  }

  public String formatNumber( Double value, String strFormat )
  {
    NumberFormat nf = new DecimalFormat( strFormat );
    return nf.format( value.doubleValue() );
  }

  public Number parseNumber( String strValue )
  {
    try
    {
      return Double.parseDouble( strValue );
    }
    catch( Exception e )
    {
      // Nonparsable floating point numbers have a NaN value (a la JavaScript)
      return IGosuParser.NaN;
    }
  }

  private static class NullSentinalCoercer extends StandardCoercer
  {
    private static final NullSentinalCoercer INSTANCE = new NullSentinalCoercer();
    @Override
    public Object coerceValue( IType typeToCoerceTo, Object value )
    {
      throw new IllegalStateException( "This is the null sentinal coercer, and is used only to " +
                                       "represent a miss in the coercer cache.  It should never " +
                                       "be returned for actual use" );
    }
    public static NullSentinalCoercer instance()
    {
      return INSTANCE;
    }
  }
}
