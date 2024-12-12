package com.gabrielcsilva1.Portal_Egresso.domain.cryptography;

public interface IPasswordHasher {
  /**
   * Generates a password hash.
   * @param plain the password in plain text.
   * @return the password hash.
   */
  public String hash(String plain);

  /**
   * Compares a cleartext password to a stored hash.
   * @param plain the password in plain text.
   * @param hash the stored hash.
   * @return true if the password is valid, false otherwise.
   */
  public boolean verify(String plain, String hash);
}
