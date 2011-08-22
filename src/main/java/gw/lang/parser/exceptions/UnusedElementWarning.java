package gw.lang.parser.exceptions;

import gw.lang.parser.resources.Res;
import gw.lang.parser.IParsedElement;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class UnusedElementWarning extends ParseWarning
{
  private final UsageType _usageType;
  public UnusedElementWarning( IParsedElement parsedElement, UsageType usageType) {
    super(parsedElement.getLocation().getLineNum(),
          1,
          parsedElement.getLocation().getColumn(),
          parsedElement.getLocation().getOffset(),
          parsedElement.getLocation().getExtent(),
          null,
          Res.MSG_UNUSED_VARIABLE,
          usageType);
    _usageType = usageType;
  }

  public UsageType getUsageType() {
    return _usageType;
  }

  public enum UsageType {
    VARIABLE("variable"),
    PARAMETER("parameter"),
    FUNCTION("function"),
    IDENTIFIER("identifier"),
    USES_STATEMENT("uses statement");
    private String _name;

    UsageType(String s) {
      _name = s;
    }

    @Override
    public String toString() {
      return _name;
    }
  }
}
