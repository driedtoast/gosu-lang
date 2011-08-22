package gw.lang.parser.expressions;

import gw.lang.reflect.gs.IGosuClass;
import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.IDynamicSymbol;
import gw.lang.reflect.IErrorType;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IIdentifierExpression extends IExpression
{
  ISymbol getSymbol();

  void setSymbol( ISymbol symbol, ISymbolTable symTable );
  
  boolean isLocalVariable();

  static final class FeatureInfoWrapper
  {
    public IFeatureInfo owningFeatureInfo;

    public FeatureInfoWrapper() {
    }

    public FeatureInfoWrapper(IFeatureInfo owningFeatureInfo) {
      this.owningFeatureInfo = owningFeatureInfo;
    }

    public static <T extends IParsedElement> T findEldestAncestor( Class<T> type, IParsedElement parsedElement )
    {
      T ancestor = null;
      IParsedElement parent = parsedElement.getParent();
      while( parent != null && parent.getParent() != null )
      {
        parent = parent.getParent();
        if( type.isAssignableFrom( parent.getClass() ) )
        {
          //noinspection unchecked
          ancestor = (T)parent;
        }
      }
      return ancestor;
    }

    public static FeatureInfoWrapper findQualifyingOwningFeatureInfo( IParsedElement parsedElement, ISymbol symbol )
    {
      CaseInsensitiveCharSequence identifierName = CaseInsensitiveCharSequence.get( symbol.getDisplayName() );
      IParsedElementWithAtLeastOneDeclaration statement = parsedElement.findDeclaringStatement( parsedElement, identifierName );
      FeatureInfoWrapper wrapper;
      if( statement == null )
      {
        IClassStatement classStatement = findEldestAncestor( IClassStatement.class, parsedElement ); // Expect a class.
        if( (classStatement == null) ||
            (symbol.getType() instanceof IErrorType) ||
            (symbol.getName().equalsIgnoreCase( "Libraries" )) ||
            (symbol.getName().equalsIgnoreCase( "perm" )) ||
            (symbol.getName().equalsIgnoreCase( "print" )) ||
            (symbol.getName().equalsIgnoreCase( "now" )) ||
            (symbol.getName().equalsIgnoreCase( "error" )) )
        {
          wrapper = new FeatureInfoWrapper();
          if( symbol instanceof IDynamicSymbol )
          {
            IScriptPartId scriptPart = ((IDynamicSymbol)symbol).getScriptPart();
            if( scriptPart != null )
            {
              IType type = scriptPart.getContainingType();
              if( type != null )
              {
                wrapper.owningFeatureInfo = type.getTypeInfo();
              }
              else
              {
                wrapper.owningFeatureInfo = null;
              }
            }
            else
            {
              wrapper.owningFeatureInfo = null;
            }
          }
          else
          {
            wrapper.owningFeatureInfo = null;
          }
        }
        else
        {
          IGosuClass gosuClass = classStatement.getGosuClass();
          IPropertyInfo propertyInfo = gosuClass.getTypeInfo().getProperty( gosuClass, symbol.getCaseInsensitiveName() );
          // If it's a property on the class
          if( propertyInfo != null )
          {
            wrapper = new FeatureInfoWrapper();
            wrapper.owningFeatureInfo = propertyInfo.getOwnersType().getTypeInfo();
          }
          else
          {
            if( symbol instanceof IDynamicFunctionSymbol )
            {
              // it must be a method or a constructor
              IAttributedFeatureInfo methodOrConstructorInfo = ((IDynamicFunctionSymbol)symbol).getMethodOrConstructorInfo();
              if (methodOrConstructorInfo != null) {
                wrapper = new FeatureInfoWrapper();
                wrapper.owningFeatureInfo = methodOrConstructorInfo.getOwnersType().getTypeInfo();
              } else {
                // No feature info for this function
                wrapper = null;
              }
            }
            else if( symbol instanceof IDynamicPropertySymbol)
            {
              // it could be a property
              IDynamicPropertySymbol dynamicPropertySymbol = (IDynamicPropertySymbol)symbol;
              propertyInfo = dynamicPropertySymbol.getGosuClass() == null ? null : dynamicPropertySymbol.getGosuClass().getTypeInfo().getProperty( identifierName );
              if( propertyInfo != null )
              {
                wrapper = new FeatureInfoWrapper();
                wrapper.owningFeatureInfo = propertyInfo.getOwnersType().getTypeInfo();
              }
              else
              {
                wrapper = null;
              }
            }
            else if( symbol instanceof IDynamicSymbol )
            {
              // it could be a property
              IDynamicSymbol dynamicSymbol = (IDynamicSymbol)symbol;
              propertyInfo = dynamicSymbol.getGosuClass().getTypeInfo().getProperty( identifierName );
              if( propertyInfo != null )
              {
                wrapper = new FeatureInfoWrapper();
                wrapper.owningFeatureInfo = propertyInfo.getOwnersType().getTypeInfo();
              }
              else
              {
                wrapper = null;
              }
            }
            else
            {
              // e.g. "this"
              wrapper = null;
            }
          }
        }
      }
      else
      {
        wrapper = new FeatureInfoWrapper();
        wrapper.owningFeatureInfo = statement.findOwningFeatureInfoOfDeclaredSymbols( identifierName );
      }
      return wrapper;
    }
  }
}
