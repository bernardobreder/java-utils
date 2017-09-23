package breder.util.lookandfeel;

/**
 * LookandFell utilit�rio
 * 
 */
public interface ILookAndFeel {

  /**
   * Objecto padrao
   */
  public static final ILookAndFeel DEFAULT = LookAndFeel.getInstance();

  /**
   * Instalar o lookandfell de Narrium
   */
  public void installNimbus();

  /**
   * Instalar o lookandfell de Narrium
   */
  public void installMetal();

  /**
   * Instalar o lookandfell de Narrium
   */
  public void installNative();

  /**
   * Instalar o lookandfell de Seaglass
   */
  public void installSeaglass();

  /**
   * Instalar o lookandfell de Seaglass
   */
  public void installModern();

  /**
   * Instalar o lookandfell do GTK
   */
  public void installGtk();

  /**
   * Instala qualquer um
   */
  public void installRandom();

}
