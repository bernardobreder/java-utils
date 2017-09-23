package breder.util.swing;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * Classe utilitario do GridBagConstraints
 * 
 */
public class GBC extends GridBagConstraints {

  /**
   * Construtor padrão
   * 
   * @param gridx x da grade
   * @param gridy y da grade
   */
  public GBC(int gridx, int gridy) {
    this.gridx = gridx;
    this.gridy = gridy;
    this.insets = new Insets(2, 2, 2, 2);
  }

  /**
   * Expansão horizontal
   * 
   * @return owner
   */
  public GBC horizontal() {
    this.weightx = 1;
    this.fill = GridBagConstraints.HORIZONTAL;
    return this;
  }

  /**
   * Expansão vertical
   * 
   * @return owner
   */
  public GBC vertical() {
    this.weighty = 1;
    this.fill = GridBagConstraints.VERTICAL;
    return this;
  }

  /**
   * Espansão em ambos os lados
   * 
   * @return owner
   */
  public GBC both() {
    this.weightx = 1;
    this.weighty = 1;
    this.fill = GridBagConstraints.BOTH;
    return this;
  }

  /**
   * Merge de linhas e colunas
   * 
   * @param w
   * @param h
   * @return owner
   */
  public GBC gridwh(int w, int h) {
    this.gridwidth = w;
    this.gridheight = h;
    return this;
  }

  /**
   * Alinha para direita
   * 
   * @return owner
   */
  public GBC right() {
    this.anchor = GridBagConstraints.EAST;
    return this;
  }

  /**
   * Espaço
   * 
   * @param value
   * @return owner
   */
  public GBC insets(int value) {
    this.insets = new Insets(value, value, value, value);
    return this;
  }

  /**
   * Inserir horizontal
   * 
   * @param value
   * @return owner
   */
  public GBC insetsHorizontal(int value) {
    this.insets = new Insets(0, value, 0, value);
    return this;
  }

  /**
   * Inserir vertical
   * 
   * @param value
   * @return owner
   */
  public GBC insetsVertical(int value) {
    this.insets = new Insets(value, 0, value, 0);
    return this;
  }

  /**
   * Topo
   * 
   * @return owner
   */
  public GBC top() {
    this.anchor = GridBagConstraints.NORTH;
    return this;
  }

  /**
   * Baixo
   * 
   * @return owner
   */
  public GBC bottom() {
    this.anchor = GridBagConstraints.SOUTH;
    return this;
  }

  /**
   * Centro
   * 
   * @return owner
   */
  public GBC center() {
    this.anchor = GridBagConstraints.CENTER;
    return this;
  }

}
