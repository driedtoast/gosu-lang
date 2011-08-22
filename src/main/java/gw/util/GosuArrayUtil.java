package gw.util;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 */
public class GosuArrayUtil {

  /**
   * <p>Checks if the object is in the given array.</p>
   *
   * <p>The method returns <code>false</code> if a <code>null</code> array is passed in.</p>
   *
   * @param array  the array to search through
   * @param objectToFind  the object to find
   * @return <code>true</code> if the array contains the object
   */
  public static boolean contains(Object[] array, Object objectToFind) {
      return indexOf(array, objectToFind) != -1;
  }

  /**
   * <p>Find the index of the given object in the array.</p>
   *
   * <p>This method returns <code>-1</code> if <code>null</code> array input.</p>
   *
   * @param array  the array to search through for the object, may be <code>null</code>
   * @param objectToFind  the object to find, may be <code>null</code>
   * @return the index of the object within the array,
   *  <code>-1</code> if not found or <code>null</code> array input
   */
  public static int indexOf(Object[] array, Object objectToFind) {
      return indexOf(array, objectToFind, 0);
  }

  /**
   * <p>Find the index of the given object in the array starting at the given index.</p>
   *
   * <p>This method returns <code>-1</code> if <code>null</code> array input.</p>
   *
   * <p>A negative startIndex is treated as zero. A startIndex larger than the array
   * length will return <code>-1</code>.</p>
   *
   * @param array  the array to search through for the object, may be <code>null</code>
   * @param objectToFind  the object to find, may be <code>null</code>
   * @param startIndex  the index to start searching at
   * @return the index of the object within the array starting at the index,
   *  <code>-1</code> if not found or <code>null</code> array input
   */
  public static int indexOf(Object[] array, Object objectToFind, int startIndex) {
      if (array == null) {
          return -1;
      }
      if (startIndex < 0) {
          startIndex = 0;
      }
      if (objectToFind == null) {
          for (int i = startIndex; i < array.length; i++) {
              if (array[i] == null) {
                  return i;
              }
          }
      } else {
          for (int i = startIndex; i < array.length; i++) {
              if (objectToFind.equals(array[i])) {
                  return i;
              }
          }
      }
      return -1;
  }
  
}