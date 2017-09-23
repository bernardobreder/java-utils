package breder.util.lookandfeel;

import java.util.Random;

import javax.swing.UIManager;

import breder.util.so.SoUtil;

/**
 * Implementacao default de lookandfeel
 * 
 * @author Bernardo Breder
 * 
 */
public class LookAndFeel implements ILookAndFeel {

  /** Contrutor */
  private static final LookAndFeel instance = new LookAndFeel();

  /**
   * Construtor
   */
  private LookAndFeel() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void installNimbus() {
    this.installLookAndFell("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void installNative() {
    this.installLookAndFell(UIManager.getSystemLookAndFeelClassName());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void installSeaglass() {
    this.installLookAndFell("com.seaglasslookandfeel.SeaGlassLookAndFeel");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void installModern() {
    if (SoUtil.isMacOs()) {
      this.installSeaglass();
    }
    else {
      this.installNimbus();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void installGtk() {
    this.installLookAndFell("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void installMetal() {
    this.installLookAndFell("javax.swing.plaf.metal.MetalLookAndFeel");
  }

  /**
   * Instalar um lookandfell
   * 
   * @param name
   */
  private void installLookAndFell(String name) {
    try {
      UIManager.setLookAndFeel(name);
    }
    catch (Exception e) {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception e1) {
      }
    }
  }

  /**
   * Objeto singleton
   * 
   * @return instancia
   */
  public static LookAndFeel getInstance() {
    return instance;
  }

  /**
   * Instala um randomicamente
   */
  @Override
  public void installRandom() {
    switch (new Random(System.currentTimeMillis()).nextInt(5)) {
      case 0: {
        LookAndFeel.getInstance().installNative();
        break;
      }
      case 1: {
        LookAndFeel.getInstance().installMetal();
        break;
      }
      case 2: {
        LookAndFeel.getInstance().installNimbus();
        break;
      }
      case 3: {
        LookAndFeel.getInstance().installSeaglass();
        break;
      }
      case 5: {
        LookAndFeel.getInstance().installGtk();
        break;
      }
    }
  }
}
