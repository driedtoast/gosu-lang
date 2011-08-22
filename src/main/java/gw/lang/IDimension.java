package gw.lang;

/**
 * A Dimension is a measurement of size or capacity of something. According to
 * the International System of Units (SI) there are seven base physical dimensions
 * from which all other physical dimensions are derived. These are (in no
 * particular order): Length, Mass, Time, Current, Temperature, Luminosity, and
 * Amount. Of course non-physical dimensions are not limited to this set. For
 * instance, Cost is a dimension which has no basis in physics, yet is often
 * a critical dimension in business calculations involving physical quantities.
 * <p>
 * The unit of measurement for any particular type of dimension can be specified
 * using any number of measure systems. For instance, a Length dimension could
 * be specified in Metric units (kilometers, millimeters, etc.) or Imperial
 * units (miles, inches, etc.) or what have you.
 * <p>
 * The stability of a dimension determines how it will be implemented in terms of
 * this interface. Essentially a dimension is either stable or volatile. A stable
 * dimension is one that does not change over time. The standard physical dimensions
 * such as Length are stable because, for example, an inch today is the same length
 * as an inch tomorrow, relative to physical objects in our universe. Also the logic
 * to compare two different units of length is stable e.g., 1 inch always equals
 * 2.54 centimeters. Thus, a stable dimension can be "pegged" on any given measure
 * unit. As a consequence there is no need to store a unit type within a stable
 * dimension instance e.g., all Length instances could be stored as millimeters
 * in a BigDecimal -- the unit of measure is only a user interface concern.
 * <p>
 * Conversely, a volatile dimension is one that may change over time. For instance,
 * the units of a Cost dimension change over time relative to one another e.g.,
 * a US Dollar today may not equal the same number of British Pounds tomorrow.
 * Essentially, the exchange rates between currency systems is based on purchasing
 * power and the balance of trade between systems, very fuzzy calculations. Thus,
 * an instance of a Cost dimension must store not only the amount, but also the
 * unit of measure e.g., US Dollars. For this reason it is recommended that for a
 * given volatile dimension separate dimensions should be specified for each type
 * of measure system. For instance, two distinct Dimensions should be defined for
 * USDollar and Euro. E.g.,
 * <pre>
 *   abstract class Cost&lt;C extends Cost&lt;C&gt;&gt; implements IDimension&lt;C, BigDecimal&gt; {...}
 *   class Dollar extends Cost&lt;Dollar&gt; {...}
 *   class Euro extends Cost&lt;Euro&gt; {...}
 * </pre>
 * Note a dimension can be an operand in any Gosu arithmetic expression: + - * / %<br>
 * as well as in any relational or equality expression: == != < <= > >= <br>
 * <p>
 * The default behavior for these operations follows:<br>
 * For addition and subtraction both operands must be of the same exact type.
 * e.g., adding inches to dollars has no practical meaning; nor does adding
 * a plain number to a dimension. Likewise for relational expressions.
 * For multiplication, division, and modulo only one operand may be a dimension
 * i.e., multiplication between dimensions is undefined.
 * <p>
 * To override default arithmetic operations a dimension may provide one or
 * more methods with the following format:
 * <pre>
 *   &lt;resulting-dimension-type&gt; &lt;operator-name&gt;( &lt;rhs-dimension-type&gt; rhsValue );
 * </pre>
 * Where &lt;operation-name&gt; is one of the following:
 * <pre>
 * add
 * subtract
 * multiply
 * divide
 * modulo
 * </pre>
 * Examples:
 * <pre>
 * class Length implements IDimension<Length, BigDecimal>
 * {
 *   ...
 *
 *   // Defines a method for producing a Rate dimension when dividing by a Time dimension.
 *   // E.g., the following divide operation is legal now (provided dimensions are defined for Rate and Time)
 *   //   var rate = distance / time
 *   public Rate divide( Time rhsValue )
 *   {
 *     return new Rate( _value.divide( rhsValue.toNumber() ) );
 *   }
 * }
 * </pre>
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
public interface IDimension<S extends IDimension<S,T>, T extends Number>
{
  /**
   * The Gosu runtime call this method when performing default operations.
   * For instance, when adding two of the same dimension types, Gosu calls
   * this method on each operand, adds the numbers, and then calls fromNumber()
   * for the result.
   *
   * @return the number of units for this dimension instance.
   */
  T toNumber();

  /**
   * Returns a separate instance of this type with the given number of units.
   * <p>
   * The Gosu runtime call this method when performing default operations.
   * For instance, when adding two of the same dimension types, Gosu calls
   * toNumber() on each operand, adds the numbers, and then calls fromNumber()
   * for the result.
   *
   * @return a separate instance of this type given the number of units.
   */
  S fromNumber( T units );

  /**
   * @return The static Number derivation for this class. Must be the same
   *   as the T parameter.  Note this is only useful in Java where the type
   *   is lost at runtime due to type erasure. In Gosu the type parameters
   *   are preserved due to type reification. 
   */
  Class<T> numberType();
}