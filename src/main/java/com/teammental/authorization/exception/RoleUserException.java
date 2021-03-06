package com.teammental.authorization.exception;

/**
 * Created by okan on 4.09.2017.
 */
public class RoleUserException extends Exception {

  private int code;
  private String label;

  /**
   * Throw ederken hızlı oluşturulması için eklenmiştir.
   */
  public RoleUserException(int code, String label) {

    this.code = code;
    this.label = label;
  }

  public RoleUserException() {

  }

  /**
   * Code alanı ile dönülen hata mesajına göre rest uyarısı oluşuturulabilir.
   */
  public int getCode() {

    return code;
  }

  /**
   * Uyarı veya hata mesajının içeriğidir.
   */
  public String getLabel() {

    return label;
  }
}