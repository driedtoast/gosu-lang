package gw.lang.tidb.database;

import sun.nio.ch.DirectBuffer;
import sun.misc.Cleaner;

import java.util.Collection;
import java.util.List;
import java.nio.ByteBuffer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 *  Copyright 2010 Guidewire Software, Inc.
 *
 * @deprecated all files under gw.lang.tidb.* will be removed in a future release
 */
public interface IDataFile
{
  void open();

  void close();

  void create(int rowSize);

  void drop();

  int getRowSize();

  int getCapacityInRows();

  Collection<Integer> allocate(int numRows);

  ByteBuffer getBufferForRow(int row);

  void delete(int rowToDelete);

  void tableScan(DataRowVisitor vistor);

  void scanRows( List<Integer> rowNums, DataRowVisitor vistor);

  class Util
  {
    public static void freeMemoryMappedFile(ByteBuffer byteBuffer) {
      if (byteBuffer instanceof DirectBuffer ) {
        /**
         * AHK - 3/22/2010
         *
         * This is a vicious, eggregious hack.  We've got two problems to deal with.  First of all, there's no Java
         * API for unmapping a memory mapped file.  See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4724038
         * They haven't put one in for 8 years, so don't hold your breath.  So to get around that, we call directly
         * into one of their cleaner classes.  That *almost* works.
         *
         * Unfortunately, the Cleaner class, when you call clean(), simply calls System.exit(1) if anything goes wrong.
         * Occassionally in our test harness the cleaner fails due to what looks like some OS-level race condition
         * that temporarily leaves the file locked.  We'd like to retry in that case, rather than, you know, killing
         * the *entire* VM with a system.exit() call.  So we pull the thunk out of the cleaner and run it ourselves
         * if it fails.  Doing that involves all sorts of reflective fun.
         */
        try {
          Cleaner cleaner = ((DirectBuffer) byteBuffer).cleaner();
          Field thunkField = Cleaner.class.getDeclaredField("thunk");
          thunkField.setAccessible(true);
          Runnable thunk = (Runnable) thunkField.get(cleaner);

          Method removeMethod = Cleaner.class.getDeclaredMethod("remove", Cleaner.class);
          removeMethod.setAccessible(true);

          if (!((Boolean) removeMethod.invoke(null, cleaner))) {
            return;
          }

          for (int i = 0; i < 10; i++) {
            try {
              thunk.run();
              return;
            } catch (Throwable t) {
              try {
                Thread.sleep(200);
              } catch (InterruptedException e) {
                // Ignore
              }
            }
          }
        } catch (NoSuchFieldException e) {
          throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
          throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}
