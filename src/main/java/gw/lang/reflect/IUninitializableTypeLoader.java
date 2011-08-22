package gw.lang.reflect;

/**
 * Allows clearing static typeloader data. Do not use lightly.
 *
 * @author dpetrusca
 */
public interface IUninitializableTypeLoader {
  void uninitialize();
}
